package service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import model.Computer;
import model.ComputerPage;
import model.ModelException;
import persistence.CompanyDAO;
import persistence.ComputerDAO;
import persistence.DatabaseConnection;

public class CLIService {

	private Scanner scanner;

	public CLIService() {
		scanner = new Scanner(System.in);
	}

	public void page(String[] arr) throws Exception {

		if (arr.length >= 2) {

			if (!isStringNonZeroPositiveInt(arr[1])) {
				System.out.println("Page number must be an non-zero positive integer!");
				return; // TODO: bad return, and other similar code too
			}

			ComputerPage page;
			try {
				page = new ComputerPage(Integer.valueOf(arr[1]), DatabaseConnection.getInstance().connect());
			} catch (ModelException e) {
				System.out.println("Error: page number is too high.");
				return;
			}
			for (Computer computer : page.getList())
				System.out.println(computer);

		} else
			System.out.println("Please include the id of the page you're looking for, e.g.: 'page 5'.");

	}

	public void computerInfo(String[] arr) throws Exception {

		if (arr.length >= 2) {

			if (!isComputerIDStringValid(arr[1])) {
				System.out.println("Computer ID must be a positive integer between 1 and the number of entries");
				return;
			} else {
				int computerID = Integer.valueOf(arr[1]);
				System.out.println(ComputerDAO.getInstance().find(computerID));
			}

		} else
			System.out.println("Please include the id of the computer you're looking for, e.g.: 'computerinfo 13'.");

	}

	public void delete(String[] arr) throws Exception {

		if (arr.length >= 2) {
			if (!isComputerIDStringValid(arr[1])) {
				System.out.println("Computer ID must be a positive integer between 1 and the number of entries");
				return;
			} else {
				ComputerDAO.getInstance().delete(Integer.valueOf(arr[1]));
			}
		} else
			System.out.println("Please include the id of the computer you want to delete, e.g.: 'delete 54'.");

	}

	/**
	 * create command logic: prompts the user and adds a corresponding entry to
	 * the database
	 * 
	 * @param computerDAO
	 *            a (Singleton) computerDAO object
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

		ComputerDAO.getInstance().add(computerName, introducedDate, discontinuedDate, companyID);

	}

	/**
	 * update command logic: prompts the user and adds a corresponding entry to
	 * the database
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
				System.out.println("Computer ID must be a positive integer between 1 and the number of entries");
				computerIDString = ""; // Reject input
			}

		}
		Integer computerID = Integer.valueOf(computerIDString);

		// Displaying current info

		ComputerDAO computerDAO = ComputerDAO.getInstance();
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

		System.out.println("Please enter the id of the company of the computer (optional):");
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
	private Date askForDate(String string) {

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
	 * @throws Exception
	 */
	private static boolean isComputerIDValid(int id) throws Exception {

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
	 * @throws Exception
	 */
	private static boolean isComputerIDStringValid(String stringID) throws Exception {

		boolean isValid = false;

		if (isStringNonZeroPositiveInt(stringID)) {
			int id = Integer.valueOf(stringID);
			isValid = isComputerIDValid(id);
		}

		return isValid;
	}

	/**
	 * Checks if a company id is greater than 0 and lesser than the total number
	 * of pages
	 * 
	 * @param id
	 *            the id of the company entry we want to check
	 * @return true if and only if the id is valid
	 * @throws Exception
	 */
	private static boolean isCompanyIDValid(int id) throws Exception {

		int nbEntries = CompanyDAO.getInstance().countCompanyEntries();
		return 0 <= id && id <= nbEntries;
	}

	// TODO: don't just check range, check if it exists via SQL
	/**
	 * Checks if a given String is a valid company ID, using
	 * isStringNonZeroPositiveInt() and isCompanyIDValid()
	 * 
	 * @param id
	 *            the id of the computer entry we want to check
	 * @return true if and only if the id is valid
	 * @throws Exception
	 */
	private static boolean isCompanyIDStringValid(String stringID) throws Exception {

		boolean isValid = false;

		if (isStringNonZeroPositiveInt(stringID)) {
			int id = Integer.valueOf(stringID);
			isValid = isCompanyIDValid(id);
		}

		return isValid;
	}

}
