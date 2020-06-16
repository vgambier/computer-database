package ui;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import persistence.ComputerDAO;
import service.DatabaseConnection;
import service.QueryHub;

public class Main {

	private static String helpMessage = String.join("\n", "List of commands:", "help: shows this message",
			"computers: shows the list of all computers", "companies: shows the list of all companies",
			"computerinfo <id>: shows all details pertaining to a given computer", "create: create a computer",
			"update: update the data of a given computer", "delete <id>: delete a given computer",
			"quit: exit the program");

	private static Scanner scanner = new Scanner(System.in);
	static DatabaseConnection dbConnection = new DatabaseConnection();
	// Note: Does not actually create a connection
	// TODO remove this useless object?

	public static void main(String[] args) {

		System.out.println("Welcome to CDB. Type 'help' for a list of commands.");

		ComputerDAO computerDAO = new ComputerDAO(dbConnection.connect());

		while (true) {

			String userInput = scanner.nextLine(); // Read user input
			String[] arr = userInput.split(" ", 2); // Splits the user input
													// into 2 arrays
			String command = arr[0]; // First word of the user input

			switch (command) {

				case "help" :
					System.out.println(helpMessage);
					break;

				case "computers" :
					QueryHub.listComputers(dbConnection);
					break;

				case "companies" :
					QueryHub.listCompanies(dbConnection);
					break;

				// TODO: check the id exists
				case "computerinfo" :
					if (arr.length >= 2)
						System.out.println(computerDAO.find(Integer.valueOf(arr[1])));
					else
						System.out.println(
								"Please include the id of the computer you're looking for, e.g.: 'computerinfo 13'.");
					break;

				case "create" :

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

					// If both dates have been given, we must check that the
					// discontinuation comes later
					if (introducedDate != null) {
						while (discontinuedDate != null && discontinuedDate.before(introducedDate)) {
							System.out.println("The date of discontinuation must be after the date of introduction.");
							discontinuedDate = askForDate("discontinuation");
						}
					}

					// Company ID field

					System.out.println("Please enter the id of the company of the computer (optional):");
					String companyIDString = scanner.nextLine();
					Integer companyID = companyIDString.equals("") ? null : Integer.valueOf(companyIDString);

					computerDAO.add(computerName, introducedDate, discontinuedDate, companyID);

					break;

				case "update" :

					// ID field

					String computerIDString = "";
					while (computerIDString.equals("")) {
						System.out.println("Please enter the ID of the computer you wish to update:");
						computerIDString = scanner.nextLine();
					}
					Integer computerID = Integer.valueOf(computerIDString);

					// Displaying current info

					System.out.println("Here is the current data on record:");
					System.out.println(computerDAO.find(computerID));
					System.out
							.println("Now, please enter the new data. Leave fields blank if you wish to remove them.");

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
					// TODO: check this ID exists

					System.out.println("Please enter the id of the company of the computer:");
					String newCompanyIDString = scanner.nextLine();
					Integer newCompanyID = newCompanyIDString.equals("") ? null : Integer.valueOf(newCompanyIDString);

					computerDAO.update(computerID, newComputerName, newIntroducedDate, newDiscontinuedDate,
							newCompanyID);

					break;

				case "delete" :
					if (arr.length >= 2)
						computerDAO.delete(Integer.valueOf(arr[1]));
					else
						System.out.println(
								"Please include the id of the computer you want to delete, e.g.: 'delete 54'.");
					break;

				case "quit" :
					System.out.println("Exiting...");
					scanner.close();
					return; // Exit the program

				default :
					System.out.println("Please enter a valid command, such as 'help'.");
			}
		}
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
}
