package service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Computer;
import model.ComputerPage;
import persistence.ComputerDAO;
import persistence.PersistenceException;

/**
 * @author Victor Gambier
 *
 */
@Component
public class ComputerService {

    private ComputerDAO computerDAO;

    @Autowired
    public ComputerService(ComputerDAO computerDAO) {
        this.computerDAO = computerDAO;
    }

    public List<Computer> listAllComputers() {
        return computerDAO.listAll();
    }

    public Computer getComputer(int computerID) {
        return computerDAO.find(computerID);
    }

    public void addComputer(String computerName, Date introducedDate, Date discontinuedDate,
            Integer companyID) {
        computerDAO.add(computerName, introducedDate, discontinuedDate, companyID);
    }

    public void updateComputer(Integer computerID, String newComputerName, Date newIntroducedDate,
            Date newDiscontinuedDate, Integer newCompanyID) {
        computerDAO.update(computerID, newComputerName, newIntroducedDate, newDiscontinuedDate,
                newCompanyID);
    }

    public void deleteComputer(Integer computerID) {
        computerDAO.delete(computerID);
    }

    public boolean doesComputerEntryExist(int id) {
        return computerDAO.doesEntryExist(id);
    }

    public int countComputerEntries() {
        return computerDAO.countEntries();
    }

    public int countComputerEntriesWhere(String searchTerm) {
        return computerDAO.countEntriesWhere(searchTerm);
    }

    public List<Computer> getPageComputers(ComputerPage computerPage) throws PersistenceException {
        return getPageComputers(computerPage, "", "computer_id");
    }

    public List<Computer> getPageComputers(ComputerPage computerPage, String searchTerm,
            String orderBy) throws PersistenceException {

        int maxItemsPerPage = ComputerPage.getMaxItemsPerPage();

        // Getting all computers that are in the page
        return computerDAO.listSomeMatching(maxItemsPerPage,
                (computerPage.getPageNumber() - 1) * maxItemsPerPage, searchTerm, orderBy);

        // These query parameters work even for the last page which only has
        // (nbEntries % MAX_ITEMS_PER_PAGE) entries, because of the way "LIMIT" works in SQL
    }
}
