package hometask_jdbc.db;

import hometask_jdbc.entity.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hometask_jdbc.db.Connector.*;

public class CompaniesDao implements AbstractDao<Company, Long> {

    private PreparedStatement savePS;
    private PreparedStatement getByIdPS;
    private PreparedStatement updatePS;
    private PreparedStatement getAllPS;
    private Statement statement;
    private Connection connection;

    public CompaniesDao() {

        try {
            initDriver();
            setConnector();

            String projectTable = "CREATE TABLE IF NOT EXISTS companies \n" +
                    "(id int AUTO_INCREMENT  PRIMARY KEY,\n" +
                    "name VARCHAR(300) NOT NULL,\n" +
                    "address LONGTEXT)";
            statement.executeUpdate(projectTable);

            savePS = connection.prepareStatement("INSERT INTO companies (name, address) VALUES (?,?)");
            getByIdPS = connection.prepareStatement("SELECT name, address FROM companies WHERE id =? ");
            getAllPS = connection.prepareStatement("SELECT id, name, address FROM companies");
            updatePS = connection.prepareStatement("UPDATE companies SET name=? , address=? WHERE id =?");
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
    public void save(Company company) throws SQLException {
        savePS.setString(1, company.getName());
        savePS.setString(2, company.getAddress());
        savePS.executeUpdate();

        long maxCompanyId = getMaxFieldValue("companies", "id");
        company.setId(maxCompanyId);
    }

    @Override
    public Company getById(Long id) {
        try {
            getByIdPS.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (ResultSet rs = getByIdPS.executeQuery()) {
            if (rs.first()) {
                return new Company(id,
                        rs.getString("name"),
                        rs.getString("address"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Company> getAll() {
        List<Company> companyList = new ArrayList<>();
        try (ResultSet rs = getAllPS.executeQuery()) {
            while (rs.next()) {
                companyList.add(new Company(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("address")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return companyList;
    }

    @Override
    public void deleteById(Long id) {
        try {
            statement.executeUpdate("DELETE FROM companies WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Company company) {
        try {
            updatePS.setString(1, company.getName());
            updatePS.setString(2, company.getAddress());
            updatePS.setLong(3, company.getId());
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
