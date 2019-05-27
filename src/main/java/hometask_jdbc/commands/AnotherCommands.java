package hometask_jdbc.commands;

import hometask_jdbc.db.DevelopersDao;
import hometask_jdbc.db.ProjectsDao;
import hometask_jdbc.db.SkillsDao;
import hometask_jdbc.entity.Developer;
import hometask_jdbc.entity.Project;

import java.util.List;
import java.util.Scanner;

public class AnotherCommands {
    private Scanner scanner = new Scanner(System.in);
    private ProjectsDao projectsDao = new ProjectsDao();
    private DevelopersDao developersDao = new DevelopersDao();
    private SkillsDao skillsDao = new SkillsDao();

    public AnotherCommands() {
        desc();
    }

    public void desc() {
        System.out.println(" \n -- Menu with additional commands --  \n" );

        System.out.println("   Choose a number of command from the list, please : ");
        System.out.println("1. Show the salary (amount) of all developers of a separate project ");
        System.out.println("2. Show the list of developers of a separate project");
        System.out.println("3. Show the list of all Java developers ");
        System.out.println("4. Show list of all Middle developers");
        System.out.println("5. Show amount of developers of all projects");
        System.out.println("6. Go to main menu ");
        System.out.println("7. Exit ");

        String command = scanner.next();
        switch (command) {
            case "1":
                salaryDevelopersOfProject();
                desc();
                break;
            case "2":
                listDevelopersOfProject();
                desc();
                break;
            case "3":
                listJavaDevelopers();
                desc();
                break;
            case "4":
                listMiddleDevelopers();
                desc();
                break;
            case "5":
                amountDevelopersOfProject();
                desc();
                break;
            case "6":
                new MainCommands();
                break;
            case "7":
                System.out.println(" --------- Exit ---------");
                projectsDao.close();
                developersDao.close();
                skillsDao.close();
                break;
            default:
                System.out.println("Unknown command. Please, try again. ");
                desc();
        }
    }

    private void salaryDevelopersOfProject() {

        List<Developer> developerList = developersDao.getAll();
        List<Project> projectList = projectsDao.getAll();
        System.out.println("Enter project ID to show all developers salary from the list");
        if (projectList != null) {
            projectList.forEach(project -> {
                System.out.println(project);
            });
        } else {
            System.out.println("Projects table is empty");
        }
        System.out.print("ID - ");
        long id = scanner.nextInt();
        Project project = projectsDao.getById(id);

        if (project != null) {
            if (developerList != null) {
                int sum = developersDao.getSumSalaryDevelopers(id);
                System.out.println("Sum salary for " + developerList.size() + "developers id " +sum);
            } else {
                System.out.println("Developers table is empty");
            }
        } else {
            System.out.println("Project with ID " + id + " not exist");
        }
    }

    private void listDevelopersOfProject() {
        System.out.println("Enter project ID to show all developers in this project from the list");
        List<Project> projectList = projectsDao.getAll();

        if (projectList != null) {
            projectList.forEach(project -> { System.out.println(project); });
        } else {
            System.out.println("Projects table is empty");
        }
        System.out.print("ID - ");
        long id = scanner.nextInt();
        Project project = projectsDao.getById(id);
        List<Developer> developerList = developersDao.getAllDevelopersOfProject(id);

        if (project != null) {
            if (developerList != null) {
                System.out.println("");
                developerList.forEach(developer -> { System.out.println(developer);});
            } else {
                System.out.println("Developers table is empty");
            }
        } else {
            System.out.println("Project with ID " + id + " not exist");
        }
    }

    private void listJavaDevelopers() {
        List <Developer> developerList = developersDao.getAllJavaDevelopers();
        System.out.println(" -- All Java developers -- ");
        if(developerList != null) {
            developerList.forEach(developer -> { System.out.println(developer); });
        } else {
            System.out.println("Java developers not exist");
        }
    }

    private void listMiddleDevelopers() {
        List <Developer> developerList = developersDao.getAllMiddleDevelopers();
        if(developerList != null) {
            developerList.forEach(developer -> { System.out.println(developer); });
        } else {
            System.out.println("Middle developers not exist");
        }
    }


    private void amountDevelopersOfProject() {
        List projectList = developersDao.getListProjectAmountDevelopers();
        if (projectList != null) {
            projectList.forEach(project -> { System.out.println(project);});
        } else {
            System.out.println("Projects table is empty");
        }
    }
}
