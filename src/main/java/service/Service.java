package service;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import mapper.MapperException;
import model.Company;
import model.Computer;
import model.ComputerPage;
import model.ModelException;
import persistence.CompanyDAO;
import persistence.ComputerDAO;
import persistence.PersistenceException;
import validator.Validator;

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

    public List<Computer> listAllComputers() throws PersistenceException {
        return computerDAO.listAll();
    }

    public List<Company> listAllCompanies() throws PersistenceException {
        return companyDAO.listAll();
    }

    public Computer getComputer(int computerID) throws PersistenceException, MapperException {
        return computerDAO.find(computerID);
    }

    public void addComputer(String computerName, Date introducedDate, Date discontinuedDate,
            Integer companyID) throws PersistenceException {
        computerDAO.add(computerName, introducedDate, discontinuedDate, companyID);
    }

    public void updateComputer(Integer computerID, String newComputerName, Date newIntroducedDate,
            Date newDiscontinuedDate, Integer newCompanyID) throws PersistenceException {
        computerDAO.update(computerID, newComputerName, newIntroducedDate, newDiscontinuedDate,
                newCompanyID);
    }

    public void deleteComputer(Integer computerID) throws PersistenceException {
        computerDAO.delete(computerID);
    }

    public void deleteCompany(Integer companyID) throws PersistenceException {
        companyDAO.delete(companyID);
    }

    public boolean doesComputerEntryExist(int id) throws PersistenceException {
        return computerDAO.doesEntryExist(id);
    }

    public boolean doesCompanyEntryExist(int id) throws PersistenceException {
        return companyDAO.doesEntryExist(id);
    }

    public int countComputerEntries() throws PersistenceException {
        return computerDAO.countEntries();
    }

    public int countComputerEntriesWhere(String searchTerm) throws PersistenceException {
        return computerDAO.countEntriesWhere(searchTerm);
    }

    public void fill(ComputerPage computerPage)
            throws ModelException, MapperException, PersistenceException {
        fill(computerPage, "", "computer_id");
    }

    public void fill(ComputerPage computerPage, String searchTerm, String orderBy)
            throws ModelException, MapperException, PersistenceException {

        int maxItemsPerPage = ComputerPage.getMaxItemsPerPage();

        // Putting computers in the page
        List<Computer> computers = computerDAO.listSomeWhere(maxItemsPerPage,
                (computerPage.getPageNumber() - 1) * maxItemsPerPage, searchTerm, orderBy);

        computerPage.setComputers(computers);
    }

    public Validator getValidator() {
        return validator;
    }
}
