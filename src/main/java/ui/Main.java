package ui;

import java.util.Scanner;

import model.Company;
import model.Computer;
import persistence.CompanyDAO;
import persistence.ComputerDAO;
import persistence.DatabaseConnection;
import service.CDBException;
import service.CLIService;

public class Main {

	private static String helpMessage = String.join("\n", "List of commands:", "help: shows this message",
			"computers: shows the list of all computers", "page <nb>: shows the nb-th page the computer list",
			"companies: shows the list of all companies",
			"computerinfo <id>: shows all details pertaining to a given computer", "create: create a computer",
			"update: update the data of a given computer", "delete <id>: delete a given computer",
			"quit: exit the program");

	// TODO: remove this attribute
	private static Scanner scanner = new Scanner(System.in);
	private static DatabaseConnection dbConnection = DatabaseConnection.getInstance();
	// Note: Does not actually create a connection to the database
	// TODO: ALWAYS DISCONNECT FROM THE DATABASE

	public static void main(String[] args) throws NumberFormatException, CDBException {

		CLIService cliService = new CLIService();

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
					cliService.page(arr);
					break;

				case "companies" :
					for (Company company : companyDAO.listAll())
						System.out.println(company);
					break;

				case "computerinfo" :
					cliService.computerInfo(arr);
					break;

				case "create" :
					cliService.create(computerDAO);
					break;

				case "update" :
					cliService.update(computerDAO);
					break;

				case "delete" :
					cliService.delete(arr);
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
		System.out.println("Thank you for using CDB!");
	}

}
