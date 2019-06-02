package hometask_jdbc.db;

import hometask_jdbc.entity.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hometask_jdbc.db.Connector.*;

public class SkillsDao implements AbstractDao <Skill, Long>{

    private PreparedStatement savePS;
    private PreparedStatement getByIdPS;
    private PreparedStatement updatePS;
    private PreparedStatement getAllPS;
    private Statement statement;
    private Connection connection;


    public SkillsDao() {

        try {
            initDriver();
            setConnector();

            String skillsTable = "CREATE TABLE IF NOT EXISTS skills \n" +
                    "(id int AUTO_INCREMENT  PRIMARY KEY,\n" +
                    "name VARCHAR(300) NOT NULL,\n" +
                    "level VARCHAR(300) NOT NULL);";

            statement.executeUpdate(skillsTable);

            savePS = connection.prepareStatement("INSERT INTO skills (name, level) VALUES (?,?)");
            getByIdPS = connection.prepareStatement("SELECT name, level FROM skills WHERE id =? ");
            updatePS = connection.prepareStatement("UPDATE skills SET name =?, level=? WHERE id=?");
            getAllPS = connection.prepareStatement("SELECT id, name, level FROM skills");
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
    public void save(Skill skill) throws SQLException {
        savePS.setString(1, skill.getName());
        savePS.setString(2, skill.getLevel());

        savePS.executeUpdate();
        long maxSkillId = getMaxFieldValue("skills", "id");
        skill.setId(maxSkillId);
    }


    @Override
    public Skill getById(Long id) {
        try {
            getByIdPS.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (ResultSet rs = getByIdPS.executeQuery()) {
            while (rs.first()) {
                return new Skill(id,
                        rs.getString("name"),
                        rs.getString("level"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Skill> getAll() {
        List<Skill> skillList = new ArrayList<>();
        try (ResultSet rs = getAllPS.executeQuery()) {
            while (rs.next()) {
                skillList.add(new Skill(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("level")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return skillList;
    }

    @Override
    public void deleteById(Long id) {
        try {
            statement.executeUpdate("DELETE FROM skills WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Skill skill) {
        try {
            updatePS.setString(1, skill.getName());
            updatePS.setString(2, skill.getLevel());
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
