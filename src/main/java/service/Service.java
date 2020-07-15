package service;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import model.Company;
import model.Computer;
import model.ComputerPage;
import persistence.CompanyDAO;
import persistence.ComputerDAO;
import persistence.PersistenceException;
import validator.Validator;

// TODO: split into two Service classes

@Component
public class Service {

    private ComputerDAO computerDAO;
    private CompanyDAO companyDAO;
    private Validator validator;

    public Service(ComputerDAO computerDAO, CompanyDAO companyDAO, Validator validator) {
        super();
        this.computerDAO = computerDAO;
        this.companyDAO = companyDAO;
        this.validator = validator;
    }

    public List<Computer> listAllComputers() {
        return computerDAO.listAll();
    }

    public List<Company> listAllCompanies() {
        return companyDAO.listAll();
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

    public void deleteCompany(Integer companyID) {
        companyDAO.delete(companyID);
    }

    public boolean doesComputerEntryExist(int id) {
        return computerDAO.doesEntryExist(id);
    }

    public boolean doesCompanyEntryExist(int id) {
        return companyDAO.doesEntryExist(id);
    }

    public int countComputerEntries() {
        return computerDAO.countEntries();
    }

    public int countComputerEntriesWhere(String searchTerm) {
        return computerDAO.countEntriesWhere(searchTerm);
    }

    public void fill(ComputerPage computerPage) throws PersistenceException {
        fill(computerPage, "", "computer_id");
    }

    public void fill(ComputerPage computerPage, String searchTerm, String orderBy)
            throws PersistenceException {

        int maxItemsPerPage = ComputerPage.getMaxItemsPerPage();

        // Putting computers in the page
        List<Computer> computers = computerDAO.listSomeMatching(maxItemsPerPage,
                (computerPage.getPageNumber() - 1) * maxItemsPerPage, searchTerm, orderBy);

        computerPage.setComputers(computers);
    }

    public Validator getValidator() {
        return validator;
    }
}
