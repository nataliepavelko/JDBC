package hometask_jdbc.commands;

import java.util.Scanner;

public class MainCommands {
    private Scanner scanner = new Scanner(System.in);

    public MainCommands() {
        desc();
    }

    private void desc() {
        System.out.println(" --------- Main menu --------- \n");

        System.out.println("Choose a number of command from the list, please : ");
        System.out.println("1. Go to table Developers ");
        System.out.println("2. Go to table Companies ");
        System.out.println("3. Go to table Customers ");
        System.out.println("4. Go to table Projects ");
        System.out.println("5. Go to table Skills ");
        System.out.println("6. Go to another commands ");
        System.out.println("7. Exit ");


        String command = scanner.next();
        switch (command) {
            case "1":
                new DeveloperCommands();
                break;
            case "2":
                new CompanyCommands();
                break;
            case "3":
                new CustomerCommands();
                break;
            case "4":
                new ProjectCommands();
                break;
            case "5":
                new SkillsCommands();
                break;
            case "6":
                new AnotherCommands();
                break;
            case "7":
                System.out.println(" --------- Exit ---------");
                break;
            default:
                System.out.println("Unknown command. Try again. ");
                desc();
        }
    }
}
