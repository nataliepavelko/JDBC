package hometask_jdbc.db;

import hometask_jdbc.entity.Developer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hometask_jdbc.db.Connector.*;

public class DevelopersProjectsDao {
    private PreparedStatement savePS;
    private PreparedStatement getByIdPS;
    private Statement statement;
    private Connection connection;

    public DevelopersProjectsDao() {
        try {
            initDriver();
            setConnector();

            String developersProjectsTable = "CREATE TABLE IF NOT EXISTS developers_projects \n" +
                    "(id_developer int, \n" +
                    " id_project int,\n" +
                    " primary key (id_developer, id_project))";
            statement.executeUpdate(developersProjectsTable);

            savePS = connection.prepareStatement("INSERT INTO developers_projects (id_developer, id_project) VALUES (?,?)");
            getByIdPS = connection.prepareStatement("SELECT id_developer, id_project FROM developers_projects WHERE id_project =? ");

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

    public void save(long id_developer, long id_project) throws SQLException {
        savePS.setLong(1, id_developer);
        savePS.setLong(2, id_project);
        savePS.executeUpdate();
    }

    public List<Developer> getDevelopersByIdProject(long id_project) {
        List<Developer> developerList = new ArrayList<>();
        DevelopersDao developersDao = new DevelopersDao();
        try {
            getByIdPS.setLong(1, id_project);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (ResultSet rs = getByIdPS.executeQuery()) {
            while (rs.next())
                developerList.addAll(developersDao.getAllDevelopersOfProject(id_project));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return developerList;
    }

    public void setDevelopersInProject(List<Long> list, long id_project) {
        try {
            for (Long id_developer : list) {
                deleteById(id_developer, id_project);
                save(id_developer, id_project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(long id_dev, long id_project) {
        try {
            statement.executeUpdate("delete from developers_projects\n" +
                    "where id_developer = " + id_dev + "\n" +
                    "and id_project = " + id_project);

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
