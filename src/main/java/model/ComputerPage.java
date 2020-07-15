package model;

/**
 * @author Victor Gambier
 *
 *         This class represents a page. It is, however, merely an abstraction, as it isn't meant to
 *         store any information about any computer
 */
public class ComputerPage {

    private static int maxItemsPerPage = 50;
    private static int nbPages;
    private int pageNumber;

    public ComputerPage(int nbEntries, int pageNumber) throws ModelException {

        // Checking the database to count the number of entries

        nbPages = nbEntries / maxItemsPerPage;
        if (nbEntries % maxItemsPerPage != 0) { // Ensuring the last page exists even when it's full
            nbPages++;
        }
        nbPages = Math.max(1, nbPages); // Always at least one page

        // Checking if the input page number is valid
        checkPageNumber(pageNumber);
        this.pageNumber = pageNumber;
    }

    /**
     * Checks if the given page number is smaller than the total number of pages.
     *
     * @param pageNumber
     *            the page number we want to check
     * @throws ModelException
     *             if the page number is not valid
     */
    private static void checkPageNumber(int pageNumber) throws ModelException {

        if (pageNumber > nbPages) {
            StringBuilder str = new StringBuilder();
            str.append("Invalid page number. With the current database, there are only ")
                    .append(nbPages).append(" pages.");
            throw new ModelException(str.toString());
        }
    }

    // Getters and setters

    public static int getNbPages() {
        return nbPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public static int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    public static void setMaxItemsPerPage(int maxItemsPerPage) {
        ComputerPage.maxItemsPerPage = maxItemsPerPage;
    }
}
