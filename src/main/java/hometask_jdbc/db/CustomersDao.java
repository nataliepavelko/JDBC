package hometask_jdbc.db;

import hometask_jdbc.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hometask_jdbc.db.Connector.*;

public class CustomersDao implements AbstractDao <Customer, Long>{

    private PreparedStatement savePS;
    private PreparedStatement getByIdPS;
    private PreparedStatement updatePS;
    private PreparedStatement getAllPS;
    private Statement statement;
    private Connection connection;


    public CustomersDao() {

        try {
            initDriver();
            setConnector();

            String customerTable = "CREATE TABLE IF NOT EXISTS customers \n" +
                    "  (id int AUTO_INCREMENT  PRIMARY KEY,\n" +
                    "  name VARCHAR(300) NOT NULL,\n" +
                    "  phone VARCHAR (25))";
            statement.executeUpdate(customerTable);

            savePS = connection.prepareStatement("INSERT INTO customers (name, phone) VALUES (?, ?) ");
            getByIdPS = connection.prepareStatement("SELECT name, phone FROM customers WHERE id =? ");
            updatePS = connection.prepareStatement("UPDATE customers SET name=?, phone=? WHERE id =?");
            getAllPS = connection.prepareStatement("SELECT id, name, phone FROM customers ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initDriver() {
        try {
            Class.forName(fullDriverClassname);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setConnector() throws SQLException {
        connection = DriverManager.getConnection(DB_Path, USER, PASSWORD);
        statement = connection.createStatement();
    }

    @Override
    public void save(Customer customer) throws SQLException {
        savePS.setString(1, customer.getName());
        savePS.setString(2, customer.getPhone());

        savePS.executeUpdate();

        long maxCustId = getMaxFieldValue("customers", "id");
        customer.setId(maxCustId);
    }

    @Override
    public Customer getById(Long id) {
        try {
            getByIdPS.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (ResultSet rs = getByIdPS.executeQuery()) {
            while (rs.first()) {
                return new Customer(id,
                        rs.getString("name"),
                        rs.getString("phone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customerList = new ArrayList<>();
        try (ResultSet rs = getAllPS.executeQuery()) {
            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }

    @Override
    public void deleteById(Long id) {
        try {
            statement.executeUpdate("DELETE FROM customers WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Customer customers) {
        try {
            updatePS.setString(1, customers.getName());
            updatePS.setString(2, customers.getPhone());
            updatePS.setLong(3, customers.getId());
            updatePS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected long getMaxFieldValue(String table, String field) throws SQLException {
        String sql = "select max(${field}) from " + table;
        sql = sql.replace("${field}", field);

        try (ResultSet rs = statement.executeQuery(sql)) {
            boolean hasFirst = rs.first();
            if (hasFirst) {
                String fieldName = "max(" + field + ")";
                long maxId = rs.getLong(fieldName);
                return maxId;
            } else {
                return -1;
            }
        }
    }

    @Override
    public void close() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
