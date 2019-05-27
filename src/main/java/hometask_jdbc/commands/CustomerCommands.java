package hometask_jdbc.commands;

import hometask_jdbc.db.CustomersDao;
import hometask_jdbc.entity.Company;
import hometask_jdbc.entity.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CustomerCommands implements Commands {
    private Scanner scanner = new Scanner(System.in);
    private CustomersDao customersDao = new CustomersDao();

    public CustomerCommands() {
        desc();
    }

    @Override
    public void desc() {

        System.out.println(" \n -- Menu of table Customers -- \n");

        System.out.println("   Choose a number of command from the list, please : ");
        System.out.println("1. Create new company ");
        System.out.println("2. Show info about company ");
        System.out.println("3. Update info for company ");
        System.out.println("4. Show all companies ");
        System.out.println("5. Delete company ");
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
                customersDao.close();
                break;
            default:
                System.out.println("Unknown command. Please, try again. ");
                desc();
        }
    }

    @Override
    public void add() {
        System.out.println("Enter info for a new customer");
        System.out.print("Name - ");
        String name = scanner.next();
        scanner.nextLine();
        System.out.print("Phone - ");
        String phone = scanner.nextLine();

        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        try {
            customersDao.save(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Added new customer!");
    }

    @Override
    public void getByID() {
        System.out.println("Enter id customer");
        long id = scanner.nextInt();
        Customer customer = customersDao.getById(id);

        if (customer != null) {
            System.out.println(customer);
        }else {
            System.out.println("Customer not exist");
        }
    }

    @Override
    public void update() {
        System.out.println("Enter information for customer update");
        System.out.print("ID -  ");
        long id = scanner.nextInt();
        System.out.print("Name - ");
        String name = scanner.next();
        scanner.nextLine();
        System.out.print("Phone - ");
        String phone = scanner.nextLine();

        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setId(id);
        if (customersDao.getById(id) != null) {
            customersDao.update(customer);
            System.out.println("Customer with id + " + id + " is updated");
        } else{
            System.out.println("Customer with ID " + id + " not exist");
        }
    }

    @Override
    public void showAll() {
        List<Customer> customerList = customersDao.getAll();

        if(customerList != null){
            customerList.forEach(customer -> { System.out.println(customer); });
        } else {
            System.out.println("Customers table is empty");
        }
    }

    @Override
    public void deleteByID() {
        System.out.println("Enter ID to delete company ");
        System.out.print("ID - ");
        long id = scanner.nextInt();
        if (customersDao.getById(id) != null) {
            customersDao.deleteById(id);
            System.out.println("Customer with ID + " + id + " was deleted");
        } else{
            System.out.println("Customer with ID " + id + " not exist");

        }
    }
}
