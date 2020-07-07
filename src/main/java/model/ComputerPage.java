package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mapper.MapperException;
import persistence.ComputerDAO;
import persistence.PersistenceException;

public class ComputerPage extends Page<Computer> {

    private static int maxItemsPerPage = 50;
    private int nbPages;

    private List<Computer> computers = new ArrayList<Computer>();

    public ComputerPage(int pageNumber)
            throws ModelException, IOException, MapperException, PersistenceException {
        this(pageNumber, "", "computer_id");
    }

    public ComputerPage(int pageNumber, String searchTerm)
            throws ModelException, IOException, MapperException, PersistenceException {
        this(pageNumber, searchTerm, "computer_id");
    }

    public ComputerPage(int pageNumber, String searchTerm, String orderBy)
            throws ModelException, IOException, MapperException, PersistenceException {
        // Checking the database to count the number of entries
        int nbEntries = ComputerDAO.getInstance().countEntriesWhere(searchTerm);

        nbPages = nbEntries / maxItemsPerPage;
        if (nbEntries % maxItemsPerPage != 0) {
            nbPages++;
        }
        nbPages = Math.max(1, nbPages); // Always at least one page

        // Checking if the input page number is valid
        checkPageNumber(pageNumber);

        // Putting computers in the page
        computers = ComputerDAO.getInstance().listSomeWhere(maxItemsPerPage,
                (pageNumber - 1) * maxItemsPerPage, searchTerm, orderBy);
    }

    /**
     * Checks if the given page number is smaller than the total number of pages.
     *
     * @param pageNumber
     *            the page number we want to check
     * @throws ModelException
     *             if the page number is not valid
     */
    private void checkPageNumber(int pageNumber) throws ModelException {

        if (pageNumber > nbPages) {
            StringBuilder str = new StringBuilder();
            str.append("Invalid page number. With the current database, there are only ")
                    .append(nbPages).append(" pages.");
            throw new ModelException(str.toString());
        }
    }

    // Getters

    public List<Computer> getComputers() {
        return computers;
    }

    public int getNbPages() {
        return nbPages;
    }

    public static void setMaxItemsPerPage(int maxItemsPerPage) {
        ComputerPage.maxItemsPerPage = maxItemsPerPage;
    }

}
