package hometask_jdbc.commands;

import hometask_jdbc.db.CompaniesDao;
import hometask_jdbc.db.CompaniesProjectsDao;
import hometask_jdbc.db.ProjectsDao;
import hometask_jdbc.entity.Company;
import hometask_jdbc.entity.Project;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompanyCommands implements Commands {

    private CompaniesDao companiesDao = new CompaniesDao();
    private Scanner scanner = new Scanner(System.in);
    private ProjectsDao projectsDao = new ProjectsDao();
    private CompaniesProjectsDao companysProjectsDao = new CompaniesProjectsDao();

    public CompanyCommands() {
        desc();
    }

    @Override
    public void desc() {
        System.out.println(" \n -- Menu of table Companies -- \n");

        System.out.println("   Choose a number of command from the list, please : ");
        System.out.println("1. Create new company ");
        System.out.println("2. Show info about company ");
        System.out.println("3. Update info for company ");
        System.out.println("4. Show all companies ");
        System.out.println("5. Delete company ");
        System.out.println("6. Show company by id project ");
        System.out.println("7. Show all projects of company ");
        System.out.println("8. Insert projects to company ");
        System.out.println("9. Go to main menu ");

        System.out.println("10. Exit ");


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
                getCompanyByIdProject();
                desc();
                break;
            case "7":
                getAllProjectsOfCompany();
                desc();
                break;
            case "8":
                insertProjectsToCompany();
                desc();
                break;
            case "9":
                new MainCommands();
                break;
            case "10":
                System.out.println(" --------- Exit ---------");
                companiesDao.close();
                companysProjectsDao.close();
                projectsDao.close();
                scanner.close();
                break;
            default:
                System.out.println("Unknown command. Please, try again. ");
                desc();
        }
    }

    @Override
    public void add() {
        System.out.println("Enter info for a new company");
        System.out.println("Name - ");
        String name = scanner.next();
        scanner.nextLine();
        System.out.println("Address - ");
        String address = scanner.nextLine();

        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        try {
            companiesDao.save(company);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Added new company!");
    }

    @Override
    public void getByID() {
        System.out.println("Enter id company");
        long id = scanner.nextInt();
        Company company = companiesDao.getById(id);

        if (company != null) {
            System.out.println(company);
        } else {
            System.out.println("Company not exist");
        }
    }

    @Override
    public void update() {
        System.out.println("Enter information for company update");
        System.out.println("ID -  ");
        long id = scanner.nextInt();
        System.out.println("Name - ");
        String name = scanner.next();
        scanner.nextLine();
        System.out.println("Address - ");
        String address = scanner.nextLine();

        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        company.setId(id);
        if (companiesDao.getById(id) != null) {
            companiesDao.update(company);
            System.out.println("Company with id " + id + " is updated");
        } else {
            System.out.println("Company with ID " + id + " not exist");
        }
    }

    @Override
    public void showAll() {
        List<Company> companyList = companiesDao.getAll();
        if (companyList != null) {
            companyList.forEach(company -> { System.out.println(company); });
        } else {
            System.out.println("Companies table is empty");
        }
    }
    private void getCompanyByIdProject() {
        showProjects();
        System.out.println("Enter id project");
        System.out.println("ID - ");
        long id = scanner.nextInt();
        Company company  = companysProjectsDao.getCompanyByIdProject(id);
        System.out.println(company);
    }


    private void getAllProjectsOfCompany() {
        showAll();
        System.out.println("Enter id company");
        System.out.println("ID - ");
        long id = scanner.nextInt();
        List <Project> projectList = companysProjectsDao.getProjectsByIdCompany(id);
        if(projectList != null){
            projectList.forEach(project -> { System.out.println(project); });
        } else {
            System.out.println("There are no projects by id company " + id);
        }
    }

    private void insertProjectsToCompany() {
        showProjects();
        System.out.println("Enter ID's of projects");
        scanner.nextLine();
        String strings = scanner.nextLine();
        String[] arrayId = strings.split(" ");
        List<Long> projectsIds = new ArrayList<>();

        for (String ID : arrayId){
            projectsIds.add(Long.parseLong(ID));
        }
        showAll();
        System.out.println("Enter ID of a company");
        long id_company = scanner.nextInt();

        companysProjectsDao.setProjectsToDCompany(projectsIds, id_company);
        companysProjectsDao.getProjectsByIdCompany(id_company);
    }

    private void showProjects() {
        List <Project> projectList = projectsDao.getAll();
        if (projectList != null) {
            projectList.forEach(project -> { System.out.println(project); });
        } else {
            System.out.println("Projects table is empty");
        }
    }

    @Override
    public void deleteByID() {
        System.out.println("Enter ID to delete company ");
        System.out.println("ID -  ");
        long id = scanner.nextInt();
        if (companiesDao.getById(id) != null) {
            companiesDao.deleteById(id);
            System.out.println("Company with ID + " + id + " was deleted");
        } else {
            System.out.println("Company with ID " + id + " not exist");
        }
    }
}
