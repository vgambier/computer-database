package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mapper.MapperException;
import persistence.ComputerDAO;
import persistence.PersistenceException;

public class ComputerPage extends Page<Computer> {

    private static int maxItemsPerPage = 50;
    private static int nbPages;

    private List<Computer> computers = new ArrayList<Computer>();

    public ComputerPage(int pageNumber)
            throws ModelException, IOException, MapperException, PersistenceException {

        // Checking the database to count the number of entries
        int nbEntries = ComputerDAO.getInstance().countEntries();

        nbPages = nbEntries / maxItemsPerPage;
        if (nbEntries % maxItemsPerPage != 0) {
            nbPages++;
        }

        // Checking if the input page number is valid
        checkPageNumber(pageNumber);

        // Putting computers in the page
        computers = ComputerDAO.getInstance().listSome(maxItemsPerPage,
                (pageNumber - 1) * maxItemsPerPage);
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

    public static int getNbPages() {
        return nbPages;
    }

    public static int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    public static void setMaxItemsPerPage(int maxItemsPerPage) {
        ComputerPage.maxItemsPerPage = maxItemsPerPage;
    }

}
