package service;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import mapper.MapperException;
import model.Company;
import model.Computer;
import persistence.CompanyDAO;
import persistence.ComputerDAO;
import persistence.PersistenceException;

public class Service {

    private ComputerDAO computerDAO;
    private CompanyDAO companyDAO;

    public Service(ComputerDAO computerDAO, CompanyDAO companyDAO) {
        super();
        this.computerDAO = computerDAO;
        this.companyDAO = companyDAO;
    }

    public List<Computer> listAllComputers() throws PersistenceException {
        return computerDAO.listAll();
    }

    public List<Company> listAllCompanies() throws PersistenceException {
        return companyDAO.listAll();
    }

    public Computer getComputer(int computerID)
            throws PersistenceException, IOException, MapperException {
        return computerDAO.find(computerID);
    }

    public void addComputer(String computerName, Date introducedDate, Date discontinuedDate,
            Integer companyID) throws PersistenceException, IOException {
        computerDAO.add(computerName, introducedDate, discontinuedDate, companyID);
    }

    public void updateComputer(Integer computerID, String newComputerName, Date newIntroducedDate,
            Date newDiscontinuedDate, Integer newCompanyID)
            throws PersistenceException, IOException {
        computerDAO.update(computerID, newComputerName, newIntroducedDate, newDiscontinuedDate,
                newCompanyID);
    }

    public void deleteComputer(Integer computerID) throws PersistenceException, IOException {
        computerDAO.delete(computerID);
    }

    public void deleteCompany(Integer companyID) throws PersistenceException {
        companyDAO.delete(companyID);
    }

    public boolean doesComputerEntryExist(int id) throws PersistenceException, IOException {
        return computerDAO.doesEntryExist(id);
    }

    public boolean doesCompanyEntryExist(int id) throws PersistenceException, IOException {
        return companyDAO.doesEntryExist(id);
    }

    public int countComputerEntries() throws PersistenceException {
        return computerDAO.countEntries();
    }

    public Object countComputerEntriesWhere(String searchTerm) throws PersistenceException {
        return computerDAO.countEntriesWhere(searchTerm);
    }
}
