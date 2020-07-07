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

/* This class uses the Singleton pattern */

public class Service {

    private static Service instance = null;

    private Service() {
    }

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public List<Computer> listAllComputers() throws PersistenceException {
        return ComputerDAO.getInstance().listAll();
    }

    public List<Company> listAllCompanies() throws PersistenceException {
        return CompanyDAO.getInstance().listAll();
    }

    public Computer getComputer(int computerID)
            throws PersistenceException, IOException, MapperException {
        return ComputerDAO.getInstance().find(computerID);
    }

    public void addComputer(String computerName, Date introducedDate, Date discontinuedDate,
            Integer companyID) throws PersistenceException, IOException {
        ComputerDAO.getInstance().add(computerName, introducedDate, discontinuedDate, companyID);
    }

    public void updateComputer(Integer computerID, String newComputerName, Date newIntroducedDate,
            Date newDiscontinuedDate, Integer newCompanyID)
            throws PersistenceException, IOException {
        ComputerDAO.getInstance().update(computerID, newComputerName, newIntroducedDate,
                newDiscontinuedDate, newCompanyID);
    }

    public void deleteComputer(Integer computerID) throws PersistenceException, IOException {
        ComputerDAO.getInstance().delete(computerID);
    }

    public void deleteCompany(Integer companyID) throws PersistenceException, IOException {
        CompanyDAO.getInstance().delete(companyID);
    }

    public boolean doesComputerEntryExist(int id) throws PersistenceException, IOException {
        return ComputerDAO.getInstance().doesEntryExist(id);
    }

    public boolean doesCompanyEntryExist(int id) throws PersistenceException, IOException {
        return CompanyDAO.getInstance().doesEntryExist(id);
    }

    public int countComputerEntries() throws PersistenceException {
        return ComputerDAO.getInstance().countEntries();
    }

    public Object countComputerEntriesWhere(String searchTerm) throws PersistenceException {
        return ComputerDAO.getInstance().countEntriesWhere(searchTerm);

    }
}
