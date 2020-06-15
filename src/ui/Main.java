package ui;

import java.util.Scanner;

import service.DatabaseConnection;
import service.QueryHub;

public class Main {

	private static String helpMessage = String.join("\n", "List of commands:", "help: shows this message",
			"computers: shows the list of all computers", "companies: shows the list of all companies",
			"computerinfo <id>: shows all details pertaining to a given computer",
			"create <data>: create a computer using the input data",
			"update <id> <data>: update the data of a given computer", "delete <id>: delete a given computer",
			"quit: exit the program");

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in); // Create a Scanner object

		DatabaseConnection dbConnection = new DatabaseConnection(); // Does not
																	// actually
																	// create a
																	// connection

		System.out.println("Welcome to CDB. Type 'help' for a list of commands.");

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

				case "computerinfo" :
					if (arr.length >= 2)
						QueryHub.computerInfo(dbConnection, Integer.valueOf(arr[1]));
					else
						System.out.println(
								"Please include the id of the computer you're looking for, e.g.: 'computerinfo 13'.");
					break;

				case "create" :
					// TODO
					break;

				case "update" :
					// TODO
					break;

				case "delete" :
					// TODO
					break;

				case "quit" :
					return; // Exit the program

				default :
					System.out.println("Please enter a valid command, such as 'help'.");
			}

		}

	}

}
