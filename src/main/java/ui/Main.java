package ui;

import java.util.Scanner;

import service.CLIService;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        CLIService cliService = CLIService.getInstance();

        System.out.println("Welcome to CDB. Type 'help' for a list of commands.");

        boolean isQuitting = false; // exit condition

        while (!isQuitting) {

            String userInput = scanner.nextLine(); // Read user input
            String[] arr = userInput.split(" ", 2); // Splits the user input
                                                    // into 2 arrays
            String command = arr[0]; // First word of the user input

            switch (command) {

                case "help" :
                    cliService.help();
                    break;

                case "computers" : // TODO: deprecate this command
                    cliService.computers();
                    break;

                case "page" : // TODO: improve this command
                    cliService.page(arr);
                    break;

                case "companies" :
                    cliService.companies();
                    break;

                case "computerinfo" :
                    cliService.computerInfo(arr);
                    break;

                case "create" :
                    cliService.create();
                    break;

                case "update" :
                    cliService.update();
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
