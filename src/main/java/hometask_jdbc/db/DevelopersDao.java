package hometask_jdbc.db;

import hometask_jdbc.entity.Developer;
import hometask_jdbc.entity.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hometask_jdbc.db.Connector.*;


public class DevelopersDao {
    private PreparedStatement savePS;
    private PreparedStatement getByIdPS;
    private PreparedStatement updatePS;
    private PreparedStatement getAllPS;
    private Connection connection;
    private Statement statement;

    public DevelopersDao() {
        try {
            setConnector();

            initDriver();
            String developerTable = "CREATE TABLE IF NOT EXISTS developers \n" +
                    "  (id int AUTO_INCREMENT  PRIMARY KEY,\n" +
                    "  name VARCHAR(300) NOT NULL,\n" +
                    "  surname VARCHAR(300) NOT NULL,\n" +
                    "  sex VARCHAR (10) DEFAULT NULL,\n" +
                    "  salary int(10)) ";
            statement.executeUpdate(developerTable);

            savePS = connection.prepareStatement("INSERT INTO developers (name, surname, sex, salary) VALUES (?,?,?,?)");
            getByIdPS = connection.prepareStatement("SELECT name, surname, sex, salary FROM " +
                    "developers WHERE id =? ");
            updatePS = connection.prepareStatement("UPDATE developers SET name =? , surname =? , sex =? , salary =? " +
                    " WHERE id = ?");
            getAllPS = connection.prepareStatement("SELECT id, name, surname, sex, salary FROM developers");
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

    public void save(Developer developer) throws SQLException {
        savePS.setString(1, developer.getName());
        savePS.setString(2, developer.getSurname());
        savePS.setString(3, developer.getSex());
        savePS.setLong(4, developer.getSalary());

        savePS.executeUpdate();

        long maxDevId = getMaxFieldValue("developers", "id");
        developer.setId(maxDevId);
    }

    public Developer getById(Long id) {
        try {
            getByIdPS.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (ResultSet rs = getByIdPS.executeQuery()) {
            if (rs.first()) {
                return new Developer(id,
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("sex"),
                        rs.getInt("salary"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Developer> getAll() {
        List<Developer> developerList = new ArrayList<>();
        try (ResultSet rs = getAllPS.executeQuery()) {
            while (rs.next()) {
                developerList.add(new Developer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("sex"),
                        rs.getInt("salary")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return developerList;
    }

    public void deleteById(Long id) {
        try {
            statement.executeUpdate("DELETE FROM developers WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Developer developer) {
        try {
            updatePS.setString(1, developer.getName());
            updatePS.setString(2, developer.getSurname());
            updatePS.setString(3, developer.getSex());
            updatePS.setLong(4, developer.getSalary());
            updatePS.setLong(5, developer.getId());
            updatePS.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSumSalaryDevelopers(long id) {
        int sum = 0;
        String sql = "SELECT sum(salary) \n" +
                "FROM developers \n" +
                "WHERE id IN \n" +
                "(SELECT id_developer FROM developers_projects \n " +
                "WHERE id_project IN  \n" +
                "(SELECT id FROM projects \n" +
                "WHERE id = " + id + "\n" +
                "))";
        try (ResultSet rs = statement.executeQuery(sql)) {
            rs.first();
            sum = rs.getInt("sum(salary)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sum;
    }

    public List<Developer> getAllDevelopersOfProject(long id) {
        List<Developer> developerList = new ArrayList<>();
        String sql = "SELECT  d.id, d.name, d.surname, d.sex, d.salary  \n" +
                "FROM developers d, projects p, developers_projects dp\n" +
                "WHERE d.id = dp.id_developer\n" +
                "AND p.id = dp.id_project\n" +
                "AND p.id = " + id;
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                developerList.add(new Developer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("sex"),
                        rs.getInt("salary")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return developerList;
    }

    public List<Developer> getAllJavaDevelopers() {
        List<Developer> developerList = new ArrayList<>();
        String sql = "SELECT d.id, d.name, d.surname, d.sex, d.salary \n" +
                "FROM developers d, skills s, developers_skills ds\n" +
                "WHERE d.id = ds.id_developer\n" +
                "AND s.id = ds.id_skill\n" +
                "AND s.id in (1, 7, 8);";
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                developerList.add(new Developer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("sex"),
                        rs.getInt("salary")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return developerList;
    }

    public List<Developer> getAllMiddleDevelopers() {
        List<Developer> developerList = new ArrayList<>();
        String sql = "SELECT d.id, d.name, d.surname, d.sex, d.salary \n" +
                "FROM developers d, skills s, developers_skills ds\n" +
                "WHERE d.id = ds.id_developer\n" +
                "AND s.id = ds.id_skill\n" +
                "AND s.id in (5, 7);";
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                developerList.add(new Developer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("sex"),
                        rs.getInt("salary")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return developerList;
    }

    public List<Project> getListProjectAmountDevelopers() {
        List projectList = new ArrayList<>();
        String sql = "SELECT p.name, p.date, count(d.name)\n" +
                "FROM projects p, developers d, developers_projects dp\n" +
                "WHERE d.id = dp.id_developer\n" +
                "AND p.id = dp.id_project\n" +
                "GROUP BY p.name\n" +
                "ORDER BY count(d.name) DESC";
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                projectList.add(new DevelopersCountOfProjects(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectList;
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


