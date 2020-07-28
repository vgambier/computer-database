package com.excilys.cdb.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.ComputerPage;
import com.excilys.cdb.model.ModelException;

// TODO: move error messages to CLI

/**
 * @author Victor Gambier
 *
 */
@Component("validatorBean")
public class Validator {

    private static DateFormat formatter;

    @Autowired
    public Validator() {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false); // allows stricter format check
    }

    /**
     * @param nbEntries
     *            the total number of entries in the database
     * @param pageID
     *            the id of the company entry we want to check, as a String
     * @return true if and only if the input corresponds to a page in the pagination system
     * @throws NumberFormatException
     */
    public boolean isPageIDStringValid(int nbEntries, String pageID) throws NumberFormatException {

        boolean isPageNumberOk = true;

        if (!isStringInteger(pageID) || Integer.parseInt(pageID) < 1) {
            System.out.println("Page number must be an non-zero positive integer!");
            isPageNumberOk = false;
        } else {
            try {
                new ComputerPage(nbEntries, Integer.valueOf(pageID));
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
    public boolean isStringInteger(String s) {

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
