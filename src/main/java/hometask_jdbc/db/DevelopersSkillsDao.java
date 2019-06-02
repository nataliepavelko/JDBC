package hometask_jdbc.db;

import hometask_jdbc.entity.Developer;
import hometask_jdbc.entity.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static hometask_jdbc.db.Connector.*;

public class DevelopersSkillsDao {
    private PreparedStatement savePS;

    private Statement statement;
    private Connection connection;

    public DevelopersSkillsDao() {
        try {
            initDriver();
            setConnector();

            String developersSkillsTable = "CREATE TABLE IF NOT EXISTS developers_skills \n" +
                    "(id_developer int, \n" +
                    " id_skill int,\n" +
                    " primary key (id_developer, id_skill))";
            statement.executeUpdate(developersSkillsTable);

            savePS = connection.prepareStatement("INSERT INTO developers_skills (id_developer, id_skill) VALUES (?,?)");

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

    public void save(long id_developer, long id_skill) throws SQLException {
        savePS.setLong(1, id_developer);
        savePS.setLong(2, id_skill);
        savePS.executeUpdate();
    }

    public Developer getDeveloperByIdSkill(long id_skill) {

        String sql = "SELECT d.id, d.name, d.surname, d.sex, d.salary \n" +
                "FROM developers d, skills s, developers_skills ds\n" +
                "WHERE d.id = ds.id_developer\n" +
                "AND ds.id_skill = " + id_skill + "\n" +
                "group by d.name";

        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.first()) {
                 return  new Developer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("sex"),
                        rs.getInt("salary")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List <Skill> getSkillsByIdDeveloper(long id_developer) {
        List <Skill> skillList = new ArrayList<>();

        String sql = "SELECT s.id, s.name, s.level \n" +
                "FROM developers d, skills s, developers_skills ds\n" +
                "WHERE ds.id_developer = " + id_developer + "\n" +
                "AND ds.id_skill = s.id \n" +
                "and d.id = "  + id_developer;

        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                skillList.add(new Skill(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("level")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skillList;
    }

    public void setSkillsToDeveloper(List<Long> list, long id_developer) {
        try {
            for (Long id_skill : list) {
                deleteById(id_developer, id_skill);
                save(id_developer, id_skill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteById(long id_dev, long id_skill) {
        try {
            statement.executeUpdate("delete from developers_skills\n" +
                    "where id_developer = " + id_dev + "\n" +
                    "and id_skill = " + id_skill);

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
