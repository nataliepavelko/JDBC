package hometask_jdbc;

import hometask_jdbc.commands.MainCommands;

import java.sql.SQLException;

public class App {
    public static void main( String[] args ) throws SQLException {

        new MainCommands();
       /* DevelopersFactory factory = new DevelopersFactory();

        Developer[] developer = factory.createDevelopers();
        for (int i = 0; i < developer.length; i++) {
            database.developDao.save(developer[i]);

            System.out.println(developer[i]);
        }*/
       /* DevelopersDao developersDao = new DevelopersDao();

        DevelopersFactory factory = new DevelopersFactory();


        Developer[] developer = factory.createDevelopers();
        for (int i = 0; i < developer.length; i++) {
            developersDao.save(developer[i]);
            System.out.println(developer[i]);
        }*/

       /* Developer developers = database.developDao.getById((long) 1);
        System.out.println("getById " + developers);
        */

        /*SkillsFactory skillsFactory = new SkillsFactory();
        Skill[] skills = skillsFactory.createSkills();
        for (int i = 0; i < skills.length ; i++){
            database.skillDao.save(skills[i]);
            System.out.println(skills[i]);
        }*/

     /* database.developDao.getAll().forEach(developers1->{
          System.out.println(developers1);
      });
*/
        System.out.println( "Good bye!" );
    }
}
