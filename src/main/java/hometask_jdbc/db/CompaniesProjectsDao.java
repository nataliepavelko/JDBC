package hometask_jdbc.db;

import hometask_jdbc.entity.Company;
import hometask_jdbc.entity.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hometask_jdbc.db.Connector.*;

public class CompaniesProjectsDao {
    private PreparedStatement savePS;
    private Statement statement;
    private Connection connection;

    public CompaniesProjectsDao() {
        try {
            initDriver();
            setConnector();

            String companiesProjectsTable = "CREATE TABLE IF NOT EXISTS companies_projects \n" +
                    "(id_companies  int, \n" +
                    " id_project  int,\n" +
                    " primary key (id_companies, id_project ))";
            statement.executeUpdate(companiesProjectsTable);

            savePS = connection.prepareStatement("INSERT INTO companies_projects (id_companies, id_project) VALUES (?,?)");
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
    public void save(long id_companies, long id_project) throws SQLException {
        savePS.setLong(1, id_companies);
        savePS.setLong(2, id_project);
        savePS.executeUpdate();
    }

    public Company getCompanyByIdProject (long id_project){
        String sql = "SELECT c.id, c.name, c.address \n" +
                "FROM companies c, projects p, companies_projects cp\n" +
                "WHERE c.id = cp.id_companies\n" +
                "AND cp.id_project = " + id_project + "\n" +
                "group by c.name";

        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.first()) {
                return  new Company(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("address")
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<Project> getProjectsByIdCompany (long id_company){
        List <Project> projectList = new ArrayList<>();

        String sql = "SELECT p.id, p.name, p.date, p.cost \n" +
                "FROM projects p, companies c, companies_projects cp\n" +
                "WHERE cp.id_companies = " + id_company + "\n" +
                "AND cp.id_project = p.id\n" +
                "AND c.id = " + id_company;
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
    public void setProjectsToDCompany(List<Long> list, long id_company) {
        try {
            for (Long id_project : list) {
                deleteById(id_company, id_project);
                save(id_company, id_project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteById(long id_companies, long id_project) {
        try {
            statement.executeUpdate("DELETE FROM companies_projects\n" +
                    "WHERE id_companies = " + id_companies + "\n" +
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
