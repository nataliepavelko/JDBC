package hometask_jdbc.db;

import hometask_jdbc.entity.Customer;
import hometask_jdbc.entity.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hometask_jdbc.db.Connector.*;

public class CustomersProjectsDao {
    private PreparedStatement savePS;
    private Statement statement;
    private Connection connection;

    public CustomersProjectsDao() {

        try {
            initDriver();
            setConnector();
            String customersProjectsTable = "CREATE TABLE IF NOT EXISTS customers_projects \n" +
                    "(id_customer  int, \n" +
                    " id_project  int,\n" +
                    " primary key (id_customer, id_project ))";
            statement.executeUpdate(customersProjectsTable);

            savePS = connection.prepareStatement("INSERT INTO customers_projects (id_customer, id_project) VALUES (?,?)");

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

    public void save(long id_customer, long id_project) throws SQLException {
        savePS.setLong(1,id_customer);
        savePS.setLong(2, id_project);
        savePS.executeUpdate();
    }

    public Customer getCustomerByIdProject (long id_project){
        String sql = "SELECT c.id, c.name, c.phone\n" +
                "FROM customers c, projects p, customers_projects cp\n" +
                "WHERE c.id = cp.id_customer\n" +
                "AND cp.id_project = " + id_project + "\n" +
                "group by c.name;";
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.first()) {
                return  new Customer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Project> getProjectsByIdCustomer (long id_customer){
        List <Project> projectList = new ArrayList<>();
        String sql = "select p.id, p.name, p.date, p.cost, c.id\n" +
                "from projects p, customers c, customers_projects cp\n" +
                "where cp.id_customer = " + id_customer + "\n" +
                "and cp.id_project = p.id \n" +
                "and c.id = " + id_customer;

        try (ResultSet rs = statement.executeQuery(sql)){
            while (rs.next()){
                projectList.add(new Project(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("cost"),
                        rs.getString("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectList;
    }

    public  void setProjectsToCustomer (List <Long> list, long id_customer){
        try {
            for (Long id_project : list) {
                deleteById(id_customer, id_project);
                save(id_customer, id_project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteById(long id_companies, long id_project) {
        try {
            statement.executeUpdate("DELETE FROM customers_projects\n" +
                    "WHERE id_customer = " + id_companies + "\n" +
                    "AND id_project = " + id_project);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
