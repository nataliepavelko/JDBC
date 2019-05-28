package hometask_jdbc.commands;

import hometask_jdbc.db.ProjectsDao;
import hometask_jdbc.entity.Project;

//import java.sql.Date;
import java.sql.SQLException;

import java.util.List;
import java.util.Scanner;

public class ProjectCommands implements Commands {
    private ProjectsDao projectsDao = new ProjectsDao();
    private Scanner scanner = new Scanner(System.in);

    public ProjectCommands() {
        desc();
    }

    @Override
    public void desc() {
        System.out.println(" \n -- Menu of table Projects -- \n");

        System.out.println("   Choose a number of command from the list, please : ");
        System.out.println("1. Create new project ");
        System.out.println("2. Show info about project ");
        System.out.println("3. Update info for project ");
        System.out.println("4. Show all projects ");
        System.out.println("5. Delete project ");
        System.out.println("6. Go to main menu ");
        System.out.println("7. Exit ");

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
                new MainCommands();
                break;
            case "7":
                System.out.println(" --------- Exit ---------");
                projectsDao.close();
                break;
            default:
                System.out.println("Unknown command. Please, try again. ");
                desc();
        }
    }

    @Override
    public void add() {
        System.out.println("Enter info for a new project");
        System.out.println("Name - ");
        String name = scanner.next();
        System.out.println("Cost - ");
        double cost = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Date - ");
        String dateString = scanner.nextLine();

        Project project = new Project();
        project.setName(name);
        project.setCost(cost);
        project.setDate(dateString);
        try {
            projectsDao.save(project);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Added new project!");
    }

    @Override
    public void getByID() {
        System.out.println("Enter id company");
        long id = scanner.nextInt();
        Project project = projectsDao.getById(id);

        if (project != null) {
            System.out.println(project);
        } else {
            System.out.println("Project not exist");
        }
    }

    @Override
    public void update() {
        System.out.println("Enter information for project update");
        System.out.println("ID -  ");
        long id = scanner.nextInt();
        System.out.println("Name - ");
        String name = scanner.next();
        System.out.println("Cost - ");
        double cost = scanner.nextDouble();

        Project project = new Project();
        project.setName(name);
        project.setCost(cost);
        project.setId(id);
        if (projectsDao.getById(id) != null) {
            projectsDao.update(project);
            System.out.println("Project with id " + id + " is updated");
        } else {
            System.out.println("Project with ID " + id + " not exist");
        }
    }

    @Override
    public void showAll() {
        List<Project> projectList = projectsDao.getAll();
        if (projectList != null) {
            projectList.forEach(project -> { System.out.println(project); });
        } else {
            System.out.println("Projects table is empty");
        }
    }

    @Override
    public void deleteByID() {
        System.out.println("Enter ID to delete project ");
        System.out.print("ID -  ");
        long id = scanner.nextInt();
        if (projectsDao.getById(id) != null) {
            projectsDao.deleteById(id);
            System.out.println("Project with ID + " + id + " was deleted");
        } else {
            System.out.println("Project with ID " + id + " not exist");
        }
    }
}
