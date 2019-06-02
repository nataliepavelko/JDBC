package hometask_jdbc.commands;

import hometask_jdbc.db.DevelopersDao;
import hometask_jdbc.db.DevelopersProjectsDao;
import hometask_jdbc.db.DevelopersSkillsDao;
import hometask_jdbc.db.SkillsDao;
import hometask_jdbc.entity.Developer;
import hometask_jdbc.entity.Skill;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DeveloperCommands implements Commands {
    private Scanner scanner = new Scanner(System.in);
    private DevelopersDao developersDao = new DevelopersDao();
    private DevelopersSkillsDao developersSkillsDao = new DevelopersSkillsDao();
    private SkillsDao skillsDao = new SkillsDao();

    public DeveloperCommands() {
        desc();
    }

    @Override
    public void desc() {
        System.out.println(" \n -- Menu of table Developers -- \n");

        System.out.println("   Choose a number of command from the list, please : ");
        System.out.println("1. Create new developer ");
        System.out.println("2. Show info about developer ");
        System.out.println("3. Update info for developer ");
        System.out.println("4. Show all developers ");
        System.out.println("5. Delete developer ");

        System.out.println("6. Show all skills of developer ");
        System.out.println("7. Show developer by id skill");
        System.out.println("8. Insert skills to developer");
        System.out.println("9. Go to main menu ");
        System.out.println("10. Exit");


        String command = scanner.next();
        switch (command) {
            case "1":
                add();
                desc();
                break;
            case "2":
                getByID();
                desc();
                break;
            case "3":
                update();
                desc();
                break;
            case "4":
                showAll();
                desc();
                break;
            case "5":
                deleteByID();
                desc();
                break;
            case "6":
                showAllSkillsOfDeveloper();
                desc();
                break;
            case "7":
                getDeveloperBySkillId();
                desc();
                break;
            case "8":
                insertSkillsToDeveloper();
                desc();
                break;
            case "9":
                new MainCommands();
                break;
            case "10":
                System.out.println(" --------- Exit ---------");
                developersDao.close();
                developersSkillsDao.close();
                skillsDao.close();
                scanner.close();
                break;
            default:
                System.out.println("Unknown command. Please, try again. ");
                desc();
        }
    }


    @Override
    public void add() {
        System.out.println("Enter info for a new developer");
        System.out.println("Name - ");
        String name = scanner.next();
        System.out.println("Surname - ");
        String surname = scanner.next();
        System.out.println("Sex - ");
        String sex = scanner.next();
        System.out.println("Salary -  ");
        int salary = scanner.nextInt();

        Developer developer = new Developer();
        developer.setName(name);
        developer.setSurname(surname);
        developer.setSex(sex);
        developer.setSalary(salary);
        try {
            developersDao.save(developer);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Added new developer!");
    }

    @Override
    public void getByID() {
        System.out.println("Enter id developer");
        long id = scanner.nextInt();
        Developer developer = developersDao.getById(id);


        if (developer != null) {
            System.out.println(developer);
        } else {
            System.out.println("Developer with id " + id + " not exist");
        }
    }

    @Override
    public void update() {
        System.out.println("Enter information for  developer update");
        System.out.println("ID -  ");
        long id = scanner.nextInt();
        System.out.println("Name - ");
        String name = scanner.next();
        System.out.println("Surname - ");
        String surname = scanner.next();
        System.out.println("Sex - ");
        String sex = scanner.next();
        System.out.println("Salary -  ");
        int salary = scanner.nextInt();

        Developer developer = new Developer();
        developer.setName(name);
        developer.setSurname(surname);
        developer.setSex(sex);
        developer.setSalary(salary);
        developer.setId(id);
        if (developersDao.getById(id) != null) {
            developersDao.update(developer);
            System.out.println("Developer with id " + id + " is updated");
        } else {
            System.out.println("Developer with ID " + id + " not exist");
        }
    }

    @Override
    public void showAll() {

        List<Developer> developerList = developersDao.getAll();
        if (developerList != null) {
            developerList.forEach(developer -> {
                System.out.println(developer);
            });
        } else {
            System.out.println("Developers table is empty");
        }
    }

    @Override
    public void deleteByID() {
        System.out.println("Enter ID to delete developer ");
        System.out.print("ID -  ");
        long id = scanner.nextInt();
        if (developersDao.getById(id) != null) {
            developersDao.deleteById(id);
            System.out.println("Developer with ID + " + id + " was deleted");
        } else {
            System.out.println("Developer with ID " + id + " not exist");
        }
    }

    private void getDeveloperBySkillId() {
        showSkills();
        System.out.println("Enter id skill");
        System.out.println("ID - ");
        long id = scanner.nextInt();
        Developer developer = developersSkillsDao.getDeveloperByIdSkill(id);
        System.out.println(developer);
    }

    private void showAllSkillsOfDeveloper() {
        showAll();
        System.out.println("Enter id developer");
        System.out.println("ID - ");
        long id = scanner.nextInt();
        List<Skill> skillList = developersSkillsDao.getSkillsByIdDeveloper(id);
        if (skillList != null) {
            skillList.forEach(skill -> {
                System.out.println(skill);
            });
        } else {
            System.out.println("There are no skills by id developer " + id);
        }

    }

    public void insertSkillsToDeveloper() {
        // scanner.useDelimiter(",");
        showSkills();
        System.out.println("Enter ID's of skills");
        scanner.nextLine();
        String strings = scanner.nextLine();
        String[] arrayId = strings.split(" ");


        List<Long> skillsIDList = new ArrayList<>();
        for (String ID : arrayId) {
            skillsIDList.add(Long.parseLong(ID));
        }
        showAll();
        System.out.println("Enter ID of a developer");
        long id_developer = scanner.nextInt();

        developersSkillsDao.setSkillsToDeveloper(skillsIDList, id_developer);
        developersSkillsDao.getSkillsByIdDeveloper(id_developer);

    }

    public void showSkills() {
        List<Skill> skillList = skillsDao.getAll();
        if (skillList != null) {
            skillList.forEach(skill -> {
                System.out.println(skill);
            });
        } else {
            System.out.println("Skills table is empty");
        }
    }
}
