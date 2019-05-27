package hometask_jdbc.db;

import hometask_jdbc.entity.Developer;
import hometask_jdbc.entity.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hometask_jdbc.db.Connector.*;

public class ProjectsDao {

    private PreparedStatement savePS;
    private PreparedStatement getByIdPS;
    private PreparedStatement updatePS;
    private PreparedStatement getAllPS;
    private Statement statement;
    private Connection connection;


    public ProjectsDao() {
        try {
            initDriver();
            setConnector();

            String projectTable = "CREATE TABLE IF NOT EXISTS projects \n" +
                    "(id int AUTO_INCREMENT  PRIMARY KEY,\n" +
                    "name VARCHAR(300) NOT NULL,\n" +
                    "cost double,\n" +
                    "date VARCHAR(300))";
            statement.executeUpdate(projectTable);

            savePS = connection.prepareStatement("INSERT INTO projects (name, cost, date) VALUES (?, ?, ?)");
            getByIdPS = connection.prepareStatement("SELECT name, cost, date FROM projects WHERE id =? ");
            updatePS = connection.prepareStatement("UPDATE projects SET name =?, cost=?, date=? WHERE id=?");
            getAllPS = connection.prepareStatement("SELECT id, name, cost, date FROM projects");
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

    public void save(Project project) throws SQLException {
        savePS.setString(1, project.getName());
        savePS.setDouble(2, project.getCost());
        savePS.setString(3, project.getDate());
        savePS.executeUpdate();

        long maxProjectId = getMaxFieldValue("projects", "id");
        project.setId(maxProjectId);
    }

    public Project getById(Long id) {
        try {
            getByIdPS.setLong(1, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (ResultSet rs = getByIdPS.executeQuery()) {
            while (rs.first()) {
                return new Project(id,
                        rs.getString("name"),
                        rs.getInt("cost"),
                        rs.getString("date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Project> getAll() {
        List<Project> projectList = new ArrayList<>();
        try (ResultSet rs = getAllPS.executeQuery()) {
            while (rs.next()) {
                projectList.add(new Project(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("cost"),
                        rs.getString("date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return projectList;
    }

    public void deleteById(Long id) {
        try {
            statement.executeUpdate("DELETE FROM projects WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Project project) {
        try {
            updatePS.setString(1, project.getName());
            updatePS.setDouble(2, project.getCost());
            updatePS.setString(3, project.getDate());
            updatePS.setLong(4, project.getId());
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

    public void close() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
