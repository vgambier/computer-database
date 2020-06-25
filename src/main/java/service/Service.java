package service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import model.Company;
import model.Computer;
import model.ComputerPage;
import model.ModelException;
import persistence.CompanyDAO;
import persistence.ComputerDAO;

/* This class uses the Singleton pattern */

public class Service {

    private Scanner scanner;
    private static String helpMessage = String.join("\n", "List of commands:",
            "help: shows this message", "computers: shows the list of all computers",
            "page <nb>: shows the nb-th page the computer list",
            "companies: shows the list of all companies",
            "computerinfo <id>: shows all details pertaining to a given computer",
            "create: create a computer", "update: update the data of a given computer",
            "delete <id>: delete a given computer", "quit: exit the program");
    // TODO: change messages from hardcoded Strings to attributes
    // TODO: factorize sanity checks, move to new package
    private static Service instance = null;

    private Service() {
        scanner = new Scanner(System.in);
    }

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    // User service methods

    /**
     * help command logic: displays a help message showing the list of all commands.
     */
    public void help() {
        System.out.println(helpMessage);
    }

    /**
     * computers command logic: displays the list of all computers.
     *
     * @throws Exception
     */
    public void computers() throws Exception {
        for (Computer computer : ComputerDAO.getInstance().listAll()) {
            System.out.println(computer);
        }
    }

    /**
     * page command logic: shows the user the desired computer page, without further prompting them.
     *
     * @param arr
     *            the user input
     * @throws Exception
     */
    public void page(String[] arr) throws Exception {

        if (arr.length >= 2) { // if a second argument has been given
            if (!isStringInteger(arr[1])) {
                System.out.println("Page number must be an non-zero positive integer!");
            } else {
                ComputerPage page = null;
                boolean isPageNumberOk = false;

                try {
                    page = new ComputerPage(Integer.valueOf(arr[1]));
                    isPageNumberOk = true; // previous line didn't throw an
                                           // exception, so it must be ok
                } catch (@SuppressWarnings("unused") ModelException e) {
                    System.out.println("Error: page number is too high.");
                }

                if (isPageNumberOk) {
                    for (Computer computer : page.getComputers()) {
                        System.out.println(computer);
                    }
                }
            }

        } else {
            System.out.println(
                    "Please include the id of the page you're looking for, e.g.: 'page 5'.");
        }
    }

    /**
     * companies command logic: displays the list of all computers.
     *
     * @throws Exception
     */
    public void companies() throws Exception {
        for (Company company : CompanyDAO.getInstance().listAll()) {
            System.out.println(company);
        }
    }

    /**
     * computerinfo command logic: shows the user the desired entry, without further prompting them.
     *
     * @param arr
     *            the user input
     *
     * @throws Exception
     */
    public void computerInfo(String[] arr) throws Exception {

        if (arr.length >= 2) {

            if (!isComputerIDStringValid(arr[1])) {
                System.out.println(
                        "Computer ID must be a positive integer and correspond to an existing entry.");
            } else {
                int computerID = Integer.valueOf(arr[1]);
                System.out.println(ComputerDAO.getInstance().find(computerID));
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
    public void create() throws Exception {

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

        ComputerDAO.getInstance().add(computerName, introducedDate, discontinuedDate, companyID);

    }

    /**
     * update command logic: prompts the user and updates the corresponding entry to the database.
     *
     * @throws Exception
     */
    public void update() throws Exception {

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
        ComputerDAO computerDAO = ComputerDAO.getInstance();
        System.out.println("Here is the current data on record:");
        System.out.println(computerDAO.find(computerID));
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

        computerDAO.update(computerID, newComputerName, newIntroducedDate, newDiscontinuedDate,
                newCompanyID);

    }

    /**
     * delete command logic: deletes the corresponding entry to the database without further
     * prompting the user.
     *
     * @param arr
     *            the user input
     * @throws Exception
     */
    public void delete(String[] arr) throws Exception {

        if (arr.length >= 2) {
            if (!isComputerIDStringValid(arr[1])) {
                System.out.println(
                        "Computer ID must be a positive integer and correspond to an existing entry.");
            } else {
                ComputerDAO.getInstance().delete(Integer.valueOf(arr[1]));
            }
        } else {
            System.out.println(
                    "Please include the id of the computer you want to delete, e.g.: 'delete 54'.");
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
    private Date askForDate(String string) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false); // allows stricter format check

        boolean isDateValid = false;
        Date date = null;
        while (!isDateValid) {

            try {
                System.out.println("Please enter the date of " + string
                        + " of the computer (YYYY-MM-DD) (optional):");
                String userInput = scanner.nextLine();

                if (userInput.equals("")) {
                    isDateValid = true;
                } else {
                    formatter.parse(userInput); // throws a ParseException if
                                                // the input is not properly
                                                // formatted
                    isDateValid = true;
                    date = java.sql.Date.valueOf(userInput);
                }

            } catch (@SuppressWarnings("unused") ParseException e) {
                System.out.println("Wrong format!");
            }
        }

        return date;
    }

    /**
     * Checks if a string represents an integer of any value.
     *
     * @param s
     *            A String, possibly representing a number
     * @return true if and only the string represents an integer
     */
    private static boolean isStringInteger(String s) {

        boolean isValid = false;

        try {
            Integer.parseInt(s);
            isValid = true;
        } catch (@SuppressWarnings("unused") NumberFormatException e) {
            // do nothing
        }

        return isValid;
    }

    /**
     * Checks if a given String is a valid computer ID, using isStringInteger() and
     * computerDAO.doesEntryExist().
     *
     * @param stringID
     *            the id of the computer entry we want to check, as a String
     * @return true if and only if the id is valid
     * @throws Exception
     */
    private static boolean isComputerIDStringValid(String stringID) throws Exception {

        boolean isValid = false;

        if (isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            isValid = ComputerDAO.getInstance().doesEntryExist(id);
        }

        return isValid;
    }

    /**
     * Checks if a given String is a valid company ID, using isStringInteger() and
     * companyDAO.doesEntryExist().
     *
     * @param stringID
     *            the id of the company entry we want to check, as a String
     * @return true if and only if the id is valid
     * @throws Exception
     */
    private static boolean isCompanyIDStringValid(String stringID) throws Exception {

        boolean isValid = false;

        if (isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            isValid = CompanyDAO.getInstance().doesEntryExist(id);
        }

        return isValid;
    }
}
