package hometask_jdbc.commands;

import hometask_jdbc.db.CustomersDao;
import hometask_jdbc.db.CustomersProjectsDao;
import hometask_jdbc.db.DevelopersProjectsDao;
import hometask_jdbc.db.ProjectsDao;
import hometask_jdbc.entity.Customer;
import hometask_jdbc.entity.Developer;
import hometask_jdbc.entity.Project;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProjectCommands implements Commands {
    private Scanner scanner = new Scanner(System.in);
    private ProjectsDao projectsDao = new ProjectsDao();
    private DevelopersProjectsDao developersProjectsDao = new DevelopersProjectsDao();
    private CustomersProjectsDao customersProjectsDao = new CustomersProjectsDao();
    private CustomersDao customersDao = new CustomersDao();

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
        System.out.println("5. Show all developers of project");
        System.out.println("6. Update developers in project");

        System.out.println("7. Show customer  by id project");
        System.out.println("8. Show all projects of customer");
        System.out.println("9. Insert projects to customer");

        System.out.println("10. Delete project ");
        System.out.println("11. Go to main menu ");
        System.out.println("12. Exit ");

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
                showAllDevelopersFromProject();
                desc();
                break;
            case "6":
                insertDevelopersInProject();
                desc();
                break;
            case "7":
                getCustomerByProjectId();
                desc();
                break;
            case "8":
                getAllProjectsByCustomerId();
                desc();
                break;
            case "9":
                insertProjectsToCustomer();
                desc();
                break;
            case "10":
                deleteByID();
                break;
            case "11":
                new MainCommands();
                break;
            case "12":
                System.out.println(" --------- Exit ---------");
                projectsDao.close();
                developersProjectsDao.close();
                customersProjectsDao.close();
                customersDao.close();
                scanner.close();
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
            projectList.forEach(project -> {
                System.out.println(project);
            });
        } else {
            System.out.println("Projects table is empty");
        }
    }

    public void showAllDevelopersFromProject() {
        showAll();
        System.out.println("Enter project id to see all project developers.");
        System.out.println("ID - ");
        long id = scanner.nextInt();

        List<Developer> developerList = developersProjectsDao.getDevelopersByIdProject(id);
        if (developerList != null) {
            developerList.forEach(developer -> {
                System.out.println(developer);
            });
        } else {
            System.out.println("Developers table is empty");
        }
    }

    public void insertDevelopersInProject() {
        System.out.println("Enter ID's of Developers");
        scanner.nextLine();
        String strings = scanner.nextLine();
        String[] arrayId = strings.split(" ");


        List<Long> developerIDList = new ArrayList<>();
        for (String ID : arrayId) {
            developerIDList.add(Long.parseLong(ID));
        }
        System.out.println("Enter ID of project");
        long id_project = scanner.nextInt();

        developersProjectsDao.setDevelopersInProject(developerIDList, id_project);
        developersProjectsDao.getDevelopersByIdProject(id_project);

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

    private void getCustomerByProjectId() {
        showAll();
        System.out.println("Enter id project");
        System.out.println("ID - ");
        long id = scanner.nextInt();
        Customer customer = customersProjectsDao.getCustomerByIdProject(id);
        System.out.println(customer);
    }

    private void getAllProjectsByCustomerId() {
        showCustomers();
        System.out.println("Enter id customer");
        System.out.println("ID - ");
        long id = scanner.nextInt();
        List<Project> projectList = customersProjectsDao.getProjectsByIdCustomer(id);
        if (projectList != null) {
            projectList.forEach(project -> {
                System.out.println(project);
            });
        } else {
            System.out.println("There are no skills by id developer " + id);
        }


    }

    private void insertProjectsToCustomer() {
        showAll();
        System.out.println("Enter ID's of projects");
        scanner.nextLine();
        String strings = scanner.nextLine();
        String[] arrayId = strings.split(" ");
        List<Long> projectsIDs = new ArrayList<>();
        for (String ID : arrayId) {
            projectsIDs.add(Long.parseLong(ID));
        }
        showCustomers();

        System.out.println("Enter ID of a customer");
        long id_customer = scanner.nextInt();
        customersProjectsDao.setProjectsToCustomer(projectsIDs, id_customer);
        customersProjectsDao.getProjectsByIdCustomer(id_customer);

    }


    private void showCustomers() {
        List<Customer> customerList = customersDao.getAll();
        if (customerList != null) {
            customerList.forEach(customer -> {
                System.out.println(customer);
            });
        } else {
            System.out.println("Skills table is empty");
        }
    }
}
