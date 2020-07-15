package ui;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.spring.AppConfiguration;
import config.spring.JdbcConfiguration;
import model.Computer;
import model.ComputerPage;
import persistence.PersistenceException;
import service.CompanyService;
import service.ComputerService;
import validator.Validator;

/**
 * @author Victor Gambier
 *
 */
public class CLI {

    private static String helpMessage = String.join("\n", "List of commands:",
            "help: shows this message", "computers: shows the list of all computers",
            "page <nb>: shows the nb-th page the computer list",
            "companies: shows the list of all companies",
            "computerinfo <id>: shows all details pertaining to a given computer",
            "create: create a computer", "update: update the data of a given computer",
            "delete <id>: delete a given computer",
            "deletecompany <id>: delete a given company and all associated computers",
            "quit: exit the program");
    private static Scanner scanner = new Scanner(System.in);

    private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            AppConfiguration.class, JdbcConfiguration.class); // Only necessary to instantiate beans
    // This needs to be an attribute so that it can be closed at the end of the program - otherwise,
    // it would be inaccessible

    private static ComputerService computerService = (ComputerService) context
            .getBean("computerServiceBean");
    private static CompanyService companyService = (CompanyService) context
            .getBean("companyServiceBean");
    private static Validator validator = (Validator) context.getBean("validatorBean");

    public static void main(String[] args) throws Exception {

        System.out.println("Welcome to CDB. Type 'help' for a list of commands.");

        boolean isQuitting = false; // exit condition

        while (!isQuitting) {

            String userInput = scanner.nextLine(); // Read user input
            String[] arr = userInput.split(" ", 2); // Splits the user input into 2 arrays
            String command = arr[0]; // First word of the user input

            switch (command) {

                case "help" :
                    help();
                    break;

                case "computers" : // could be deprecated
                    computers();
                    break;

                case "page" :
                    page(arr);
                    break;

                case "companies" :
                    companies();
                    break;

                case "computerinfo" :
                    computerInfo(arr);
                    break;

                case "create" :
                    create();
                    break;

                case "update" :
                    update();
                    break;

                case "delete" :
                    delete(arr);
                    break;

                case "deletecompany" :
                    deletecompany(arr);
                    break;

                case "quit" :
                    isQuitting = true;
                    break;

                default :
                    System.out.println("Please enter a valid command, such as 'help'.");
            }
        }

        // Exiting

        scanner.close();
        ((ConfigurableApplicationContext) context).close(); // Closing Spring Beans context
        System.out.println("Thank you for using CDB!");
    }

    /**
     * help command logic: displays a help message showing the list of all commands.
     */
    private static void help() {
        System.out.println(helpMessage);
    }

    /**
     * computers command logic: displays the list of all computers.
     *
     * @throws PersistenceException
     *
     * @throws Exception
     */
    private static void computers() {
        computerService.listAllComputers().forEach(computer -> System.out.println(computer));
    }

    /**
     * page command logic: shows the user the desired computer page, without further prompting them.
     *
     * @param arr
     *            the user input
     * @throws Exception
     */
    private static void page(String[] arr) throws Exception {

        if (arr.length >= 2) { // if a second argument has been given

            int nbEntries = computerService.countComputerEntries();
            if (validator.isPageIDStringValid(nbEntries, arr[1])) {
                ComputerPage computerPage = new ComputerPage(nbEntries, Integer.valueOf(arr[1]));
                List<Computer> computers = computerService.getPageComputers(computerPage);
                computers.forEach(computer -> System.out.println(computer));

            }

        } else { // if a second argument hasn't been given
            System.out.println(
                    "Please include the id of the page you're looking for, e.g.: 'page 5'.");
        }
    }

    /**
     * companies command logic: displays the list of all computers.
     *
     * @throws Exception
     */
    private static void companies() {
        companyService.listAllCompanies().forEach(company -> System.out.println(company));
    }

    /**
     * computerinfo command logic: shows the user the desired entry, without further prompting them.
     *
     * @param arr
     *            the user input
     *
     * @throws Exception
     */
    private static void computerInfo(String[] arr) throws Exception {

        if (arr.length >= 2) {

            if (!isComputerIDStringValid(arr[1])) {
                System.out.println(
                        "Computer ID must be a positive integer and correspond to an existing entry.");
            } else {
                int computerID = Integer.valueOf(arr[1]);
                System.out.println(computerService.getComputer(computerID));
            }

        } else {
            System.out.println(
                    "Please include the id of the computer you're looking for, e.g.: 'computerinfo 13'.");
        }
    }

    /**
     * create command logic: prompts the user and adds a corresponding entry to the database.
     *
     * @throws Exception
     */
    private static void create() throws Exception {

        // Name field
        String computerName = "";
        while (computerName.equals("")) {
            System.out.println("Please enter the name of the new computer (mandatory):");
            computerName = scanner.nextLine();
        }

        // Introduced field
        Date introducedDate = askForDate("introduction");

        // Discontinued field
        Date discontinuedDate = askForDate("discontinuation");

        // If and only if both dates have been given, we must check that the
        // discontinuation comes later
        if (introducedDate != null) {
            while (discontinuedDate != null && discontinuedDate.before(introducedDate)) {
                System.out.println(
                        "The date of discontinuation must be after the date of introduction.");
                discontinuedDate = askForDate("discontinuation");
            }
        }

        // Company ID field - must be either empty or a valid ID
        String companyIDString = "invalid"; // initializing with invalid value
        boolean isCompanyIDValid = false;
        while (!isCompanyIDValid) {
            System.out.println("Please enter the id of the company of the computer (optional):");
            companyIDString = scanner.nextLine();

            if (!companyIDString.equals("") && !isCompanyIDStringValid(companyIDString)) {
                System.out.println(
                        "Company ID must be a positive integer and correspond to an existing entry.");
            } else {
                isCompanyIDValid = true;
            }
        }
        Integer companyID = companyIDString.equals("") ? null : Integer.valueOf(companyIDString);

        computerService.addComputer(computerName, introducedDate, discontinuedDate, companyID);
        System.out.println("Entry added.");
    }

    /**
     * update command logic: prompts the user and updates the corresponding entry to the database.
     *
     * @throws Exception
     */
    private static void update() throws Exception {

        // ID field
        String computerIDString = "";
        while (computerIDString.equals("")) {
            System.out.println("Please enter the ID of the computer you wish to update:");
            computerIDString = scanner.nextLine();

            if (!isComputerIDStringValid(computerIDString)) {
                System.out.println(
                        "Computer ID must be a positive integer and correspond to an existing entry.");
                computerIDString = ""; // Reject input
            }

        }
        Integer computerID = Integer.valueOf(computerIDString);

        // Displaying current info
        System.out.println("Here is the current data on record:");
        System.out.println(computerService.getComputer(computerID));
        System.out.println(
                "Now, please enter the new data. Leave fields blank if you wish to remove them.");

        // Name field
        String newComputerName = "";
        while (newComputerName.equals("")) {
            System.out.println("Please enter the name of the computer:");
            newComputerName = scanner.nextLine();
        }

        // Introduced field
        Date newIntroducedDate = askForDate("introduction");

        // Discontinued field
        Date newDiscontinuedDate = askForDate("discontinuation");

        // If and only if both dates have been given, we must check that the
        // discontinuation comes later
        if (newIntroducedDate != null) {
            while (newDiscontinuedDate != null && newDiscontinuedDate.before(newIntroducedDate)) {
                System.out.println(
                        "The date of discontinuation must be after the date of introduction.");
                newDiscontinuedDate = askForDate("discontinuation");
            }
        }

        // Company ID field - must be either empty or a valid ID
        String newCompanyIDString = "invalid"; // initializing with invalid
                                               // value
        boolean isCompanyIDValid = false;
        while (!isCompanyIDValid) {
            System.out.println("Please enter the id of the company of the computer (optional):");
            newCompanyIDString = scanner.nextLine();

            if (!newCompanyIDString.equals("") && !isCompanyIDStringValid(newCompanyIDString)) {
                System.out.println(
                        "Company ID must be a positive integer and correspond to an existing entry.");
            } else {
                isCompanyIDValid = true;
            }
        }

        Integer newCompanyID = newCompanyIDString.equals("")
                ? null
                : Integer.valueOf(newCompanyIDString);

        computerService.updateComputer(computerID, newComputerName, newIntroducedDate,
                newDiscontinuedDate, newCompanyID);
        System.out.println("Entry updated.");

    }

    /**
     * delete command logic: deletes the corresponding entry to the database without further
     * prompting the user.
     *
     * @param arr
     *            the user input
     * @throws Exception
     */
    private static void delete(String[] arr) throws Exception {

        if (arr.length >= 2) {
            if (!isComputerIDStringValid(arr[1])) {
                System.out.println(
                        "Computer ID must be a positive integer and correspond to an existing entry.");
            } else {
                computerService.deleteComputer(Integer.valueOf(arr[1]));
                System.out.println("Entry deleted.");
            }
        } else {
            System.out.println(
                    "Please include the id of the computer you want to delete, e.g.: 'delete 54'.");
        }
    }

    private static void deletecompany(String[] arr) throws NumberFormatException {
        if (arr.length >= 2) {
            if (!isCompanyIDStringValid(arr[1])) {
                System.out.println(
                        "Company ID must be a positive integer and correspond to an existing entry.");
            } else {
                companyService.deleteCompany(Integer.valueOf(arr[1]));
                System.out.println("Company succesfully deleted.");
            }
        } else {
            System.out.println(
                    "Please include the id of the company you want to delete, e.g.: 'deletecompany 12'.");
        }
    }

    /**
     * Asks the user for a date and parses it to verify it is a correct format (until the input is
     * either empty or correct) and returns it as a Date object. If the input is empty at any point,
     * the method returns null, which is intended behavior.
     *
     * @param string
     *            the name of the date that will be displayed to the user
     * @return the corresponding Date object
     */
    private static Date askForDate(String string) {

        boolean isDateValid = false;
        Date date = null;
        String userInput = null;

        while (!isDateValid) {

            System.out.println("Please enter the date of " + string
                    + " of the computer (YYYY-MM-DD) (optional):");

            userInput = scanner.nextLine();
            isDateValid = userInput.equals("") || validator.isDateStringValid(userInput);
        }

        date = userInput.equals("") ? null : java.sql.Date.valueOf(userInput);

        return date;
    }

    /**
     * Checks if a given String is a valid computer ID, using isStringInteger() and
     * service.doesComputerEntryExist().
     *
     * @param stringID
     *            the id of the computer entry we want to check, as a String
     * @return true if and only if there is a computer with this id in the database
     * @throws PersistenceException
     */
    private static boolean isComputerIDStringValid(String stringID) {

        boolean isValid = false;

        if (validator.isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            isValid = computerService.doesComputerEntryExist(id);
        }

        return isValid;
    }

    /**
     * Checks if a given String is a valid company ID, using isStringInteger() and
     * companyDAO.doesEntryExist().
     *
     * @param stringID
     *            the id of the company entry we want to check, as a String
     * @return true if and only if there is a company with this id in the database
     * @throws PersistenceException
     */
    private static boolean isCompanyIDStringValid(String stringID) {

        boolean isValid = false;

        if (validator.isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            isValid = companyService.doesCompanyEntryExist(id);
        }

        return isValid;
    }

}
