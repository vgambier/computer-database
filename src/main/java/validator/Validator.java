package validator;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.AppConfiguration;
import config.JdbcConfiguration;
import mapper.MapperException;
import model.ComputerPage;
import model.ModelException;
import persistence.PersistenceException;
import service.Service;

public class Validator {

    private static Validator instance = null;

    private Validator() {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false); // allows stricter format check
    }

    // Singleton instance getter
    public static Validator getInstance() {
        if (instance == null) {
            instance = new Validator();
        }
        return instance;
    }

    private static DateFormat formatter;

    /**
     * @param pageID
     *            the id of the company entry we want to check, as a String
     * @return true if and only if the input corresponds to a page in the pagination system
     * @throws NumberFormatException
     * @throws IOException
     * @throws MapperException
     * @throws PersistenceException
     */
    public boolean isPageIDStringValid(String pageID)
            throws NumberFormatException, MapperException, PersistenceException {

        boolean isPageNumberOk = true;

        if (!isStringInteger(pageID) || Integer.parseInt(pageID) < 1) {
            System.out.println("Page number must be an non-zero positive integer!");
            isPageNumberOk = false;
        } else {
            try {
                new ComputerPage(Integer.valueOf(pageID));
                isPageNumberOk = true; // previous line didn't throw an
                                       // exception, so it must be ok
            } catch (@SuppressWarnings("unused") ModelException e) {
                System.out.println("Error: page number is too high.");
                isPageNumberOk = false;
            }
        }

        return isPageNumberOk;

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
    public boolean isComputerIDStringValid(String stringID) throws PersistenceException {

        boolean isValid = false;

        if (isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            // TODO pas un deuxième contexte
            ApplicationContext context = new AnnotationConfigApplicationContext(
                    AppConfiguration.class, JdbcConfiguration.class);
            Service service = (Service) context.getBean("serviceBean");
            isValid = service.doesComputerEntryExist(id);
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
    public boolean isCompanyIDStringValid(String stringID) throws PersistenceException {

        boolean isValid = false;

        if (isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            // TODO pas un deuxième contexte
            ApplicationContext context = new AnnotationConfigApplicationContext(
                    AppConfiguration.class, JdbcConfiguration.class);
            Service service = (Service) context.getBean("serviceBean");
            isValid = service.doesCompanyEntryExist(id);
        }

        return isValid;
    }

    /**
     * Checks if a given String is a valid YYYY-MM-DD date.
     *
     * @param date
     *            the date to check
     * @return true if and only if the String is valid
     */
    public boolean isDateStringValid(String date) {

        boolean isDateValid = true;

        try {
            formatter.parse(date); // throws a ParseException if the input is not properly formatted
        } catch (@SuppressWarnings("unused") ParseException e) {
            System.out.println("Wrong format!");
            isDateValid = false;
        }

        return isDateValid;
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

}
