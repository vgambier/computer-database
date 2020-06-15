package ui;

import java.util.Scanner;

public class Main {

	private static String helpMessage = String.join("\n", "List of commands:",
			"help: shows this message",
			"computers: shows the list of all computers");

	public static void main(String[] args) {

		// TODO: Full interface

		Scanner scanner = new Scanner(System.in); // Create a Scanner object

		System.out
				.println("Welcome to CDB. Type 'help' for a list of commands.");

		while (true) {

			String userInput = scanner.nextLine(); // Read user input

			switch (userInput) {
				case "help" :
					System.out.println(helpMessage);
					break;
				case "computers" :
					System.out.println("Here is the list of all computers:");
					// TODO call a function / SQL query
					break;
				default :
					System.out
							.println("Please enter a command, such as 'help'.");
			}

		}

	}

}
