package ui;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import model.Company;
import model.Computer;
import model.ComputerPage;
import persistence.CompanyDAO;
import persistence.ComputerDAO;
import service.CDBException;
import service.DatabaseConnection;
import service.PageNumberException;

public class Main {

	private static String helpMessage = String.join("\n", "List of commands:", "help: shows this message",
			"computers: shows the list of all computers", "page <nb>: shows the nb-th page the computer list",
			"companies: shows the list of all companies",
			"computerinfo <id>: shows all details pertaining to a given computer", "create: create a computer",
			"update: update the data of a given computer", "delete <id>: delete a given computer",
			"quit: exit the program");

	private static Scanner scanner = new Scanner(System.in);
	private static DatabaseConnection dbConnection = new DatabaseConnection();
	// Note: Does not actually create a connection to the database
	// TODO: ALWAYS DISCONNECT FROM THE DATABASE

	public static void main(String[] args) throws NumberFormatException, CDBException {

		System.out.println("Welcome to CDB. Type 'help' for a list of commands.");
		// TODO: password prompt?

		ComputerDAO computerDAO = ComputerDAO.getInstance();
		computerDAO.setConnection(dbConnection.connect());
		CompanyDAO companyDAO = CompanyDAO.getInstance();
		companyDAO.setConnection(dbConnection.connect());

		boolean isQuitting = false; // exit condition

		while (!isQuitting) {

			String userInput = scanner.nextLine(); // Read user input
			String[] arr = userInput.split(" ", 2); // Splits the user input into 2 arrays
			String command = arr[0]; // First word of the user input

			switch (command) {

				case "help" :

					System.out.println(helpMessage);
					break;

				case "computers" :

					for (Computer computer : computerDAO.listAll())
						System.out.println(computer);
					break;

				case "page" :

					if (arr.length >= 2) {

						if (!isStringNonZeroPositiveInt(arr[1])) {
							System.out.println("Page number must be an non-zero positive integer!");
							break;
						}

						ComputerPage page;
						try {
							page = new ComputerPage(Integer.valueOf(arr[1]), dbConnection.connect());
						} catch (PageNumberException e) {
							System.out.println("Error: page number is too high.");
							break;
						}
						for (Computer computer : page.getList())
							System.out.println(computer);

					} else
						System.out.println("Please include the id of the page you're looking for, e.g.: 'page 5'.");

					break;

				case "companies" :

					for (Company company : companyDAO.listAll())
						System.out.println(company);
					break;

				case "computerinfo" :

					if (arr.length >= 2) {

						if (!isComputerIDStringValid(arr[1])) {
							System.out.println(
									"Computer ID must be a positive integer between 1 and the number of entries");
							break;
						} else {
							int computerID = Integer.valueOf(arr[1]);
							System.out.println(computerDAO.find(computerID));
						}

					} else
						System.out.println(
								"Please include the id of the computer you're looking for, e.g.: 'computerinfo 13'.");
					break;

				case "create" :
					create(computerDAO);
					break;

				case "update" :
					update(computerDAO);
					break;

				case "delete" :

					if (arr.length >= 2) {
						if (!isComputerIDStringValid(arr[1])) {
							System.out.println(
									"Computer ID must be a positive integer between 1 and the number of entries");
							break;
						} else {
							computerDAO.delete(Integer.valueOf(arr[1]));
						}
					} else
						System.out.println(
								"Please include the id of the computer you want to delete, e.g.: 'delete 54'.");
					break;

				case "quit" :
					isQuitting = true;
					break;

				default :
					System.out.println("Please enter a valid command, such as 'help'.");
			}
		}

		// Exiting

		System.out.println("Exiting...");
		dbConnection.disconnect();
		scanner.close();
	}

	/**
	 * create command logic: prompts the user and adds a corresponding entry to
	 * the database
	 * 
	 * @param computerDAO
	 *            a (Singleton) computerDAO object
	 * @throws CDBException
	 */
	private static void create(ComputerDAO computerDAO) throws CDBException {

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

		// If both dates have been given, we must check that the discontinuation comes later
		if (introducedDate != null) {
			while (discontinuedDate != null && discontinuedDate.before(introducedDate)) {
				System.out.println("The date of discontinuation must be after the date of introduction.");
				discontinuedDate = askForDate("discontinuation");
			}
		}

		// Company ID field
		// TODO: check this company ID exists

		System.out.println("Please enter the id of the company of the computer (optional):");
		String companyIDString = scanner.nextLine();
		Integer companyID = companyIDString.equals("") ? null : Integer.valueOf(companyIDString);

		computerDAO.add(computerName, introducedDate, discontinuedDate, companyID);

	}

	/**
	 * update command logic: prompts the user and adds a corresponding entry to
	 * the database
	 * 
	 * @param computerDAO
	 *            a (Singleton) computerDAO object
	 * @throws CDBException
	 */
	private static void update(ComputerDAO computerDAO) throws CDBException {

		// ID field

		String computerIDString = "";
		while (computerIDString.equals("")) {
			System.out.println("Please enter the ID of the computer you wish to update:");
			computerIDString = scanner.nextLine();

			if (!isComputerIDStringValid(computerIDString)) {
				System.out.println("Computer ID must be a positive integer between 1 and the number of entries");
				computerIDString = ""; // Reject input
			}

		}
		Integer computerID = Integer.valueOf(computerIDString);

		// Displaying current info

		System.out.println("Here is the current data on record:");
		System.out.println(computerDAO.find(computerID));
		System.out.println("Now, please enter the new data. Leave fields blank if you wish to remove them.");

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

		// Company ID field
		// TODO: check this company ID exists

		System.out.println("Please enter the id of the company of the computer:");
		String newCompanyIDString = scanner.nextLine();
		Integer newCompanyID = newCompanyIDString.equals("") ? null : Integer.valueOf(newCompanyIDString);

		computerDAO.update(computerID, newComputerName, newIntroducedDate, newDiscontinuedDate, newCompanyID);

	}

	/**
	 * Asks the user for a date and parses it to verify it is a correct format
	 * (until the input is either empty or correct) and returns it as a Date
	 * object. If the input is empty at any point, the method returns null,
	 * which is intended behavior.
	 * 
	 * @param string
	 *            the name of the date that will be displayed to the user
	 * @return the corresponding Date object
	 */
	private static Date askForDate(String string) {

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setLenient(false); // allows stricter format check

		boolean isDateValid = false;
		Date date = null;
		while (isDateValid == false) {

			try {

				System.out.println("Please enter the date of " + string + " of the computer (YYYY-MM-DD) (optional):");

				String userInput = scanner.nextLine();

				if (userInput.equals(""))
					break;

				formatter.parse(userInput); // throws a ParseException if the
											// input is not properly formatted
				isDateValid = true;
				date = java.sql.Date.valueOf(userInput);

			} catch (ParseException e) {
				System.out.println("Wrong format!");
			}
		}

		return date;

	}

	/**
	 * Checks if a string represents a non-zero positive integer
	 * 
	 * @param s
	 *            A string, possibly representing a number
	 * @return true if and only the string represents a non-zero positive
	 *         integer
	 */
	private static boolean isStringNonZeroPositiveInt(String s) {

		boolean isValid = false;

		try {
			Integer.parseInt(s);
			isValid = true;
		} catch (NumberFormatException e) {
			// do nothing
		}

		return isValid && Integer.valueOf(s) > 0;
	}

	/**
	 * Checks if a computer id is greater than 0 and lesser than the total
	 * number of pages
	 * 
	 * @param id
	 *            the id of the computer entry we want to check
	 * @return true if and only if the id is valid
	 * @throws CDBException
	 */
	private static boolean isComputerIDValid(int id) throws CDBException {

		int nbEntries = ComputerDAO.getInstance().countComputerEntries();
		return 0 <= id && id <= nbEntries;
	}

	/**
	 * Checks if a given String is a valid computer ID, using
	 * isStringNonZeroPositiveInt() and isComputerIDValid()
	 * 
	 * @param id
	 *            the id of the computer entry we want to check
	 * @return true if and only if the id is valid
	 * @throws CDBException
	 */
	private static boolean isComputerIDStringValid(String stringID) throws CDBException {

		boolean isValid = false;

		if (isStringNonZeroPositiveInt(stringID)) {
			int id = Integer.valueOf(stringID);
			isValid = isComputerIDValid(id);
		}

		return isValid;

	}

}
