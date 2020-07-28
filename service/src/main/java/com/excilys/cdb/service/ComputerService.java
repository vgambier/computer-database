package com.excilys.cdb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.ComputerDAO;
import com.excilys.cdb.dao.PersistenceException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerPage;

/**
 * @author Victor Gambier
 *
 */
@Service("computerServiceBean")
public class ComputerService {

    private ComputerDAO computerDAO;

    @Autowired
    public ComputerService(ComputerDAO computerDAO) {
        this.computerDAO = computerDAO;
    }

    public Computer getComputer(int computerID) {
        return computerDAO.find(computerID);
    }

    public void addComputer(Computer computer) {
        computerDAO.add(computer);
    }

    public void updateComputer(Computer computer) {
        computerDAO.update(computer);
    }

    public void deleteComputer(Computer computer) {
        computerDAO.delete(computer);
    }

    public boolean doesComputerEntryExist(int id) {
        return computerDAO.doesEntryExist(id);
    }

    public int countComputerEntries() {
        return computerDAO.countEntries();
    }

    public int countComputerEntriesWhere(String searchTerm) {
        return computerDAO.countEntriesMatching(searchTerm);
    }

    public List<Computer> getPageComputers(ComputerPage computerPage) throws PersistenceException {
        return getPageComputers(computerPage, "", "computer.id");
    }

    public List<Computer> getPageComputers(ComputerPage computerPage, String searchTerm,
            String orderBy) throws PersistenceException {

        int maxItemsPerPage = ComputerPage.getMaxItemsPerPage();

        // Getting all computers that are in the page
        return computerDAO.findMatchesWithinRange(maxItemsPerPage,
                (computerPage.getPageNumber() - 1) * maxItemsPerPage, searchTerm, orderBy);

        // These query parameters work even for the last page which only has
        // (nbEntries % MAX_ITEMS_PER_PAGE) entries, because of the way "LIMIT" works in SQL
    }
}
