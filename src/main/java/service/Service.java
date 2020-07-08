package service;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mapper.MapperException;
import model.Company;
import model.Computer;
import persistence.CompanyDAO;
import persistence.ComputerDAO;
import persistence.PersistenceException;

public class Service {

    public List<Computer> listAllComputers() throws PersistenceException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        ComputerDAO computerDAO = (ComputerDAO) context.getBean("computerDAOBean");
        ((ConfigurableApplicationContext) context).close();
        return computerDAO.listAll();
    }

    public List<Company> listAllCompanies() throws PersistenceException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        CompanyDAO companyDAO = (CompanyDAO) context.getBean("companyDAOBean");
        ((ConfigurableApplicationContext) context).close();
        return companyDAO.listAll();
    }

    public Computer getComputer(int computerID)
            throws PersistenceException, IOException, MapperException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        ComputerDAO computerDAO = (ComputerDAO) context.getBean("computerDAOBean");
        ((ConfigurableApplicationContext) context).close();
        return computerDAO.find(computerID);
    }

    public void addComputer(String computerName, Date introducedDate, Date discontinuedDate,
            Integer companyID) throws PersistenceException, IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        ComputerDAO computerDAO = (ComputerDAO) context.getBean("computerDAOBean");
        ((ConfigurableApplicationContext) context).close();
        computerDAO.add(computerName, introducedDate, discontinuedDate, companyID);
    }

    public void updateComputer(Integer computerID, String newComputerName, Date newIntroducedDate,
            Date newDiscontinuedDate, Integer newCompanyID)
            throws PersistenceException, IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        ComputerDAO computerDAO = (ComputerDAO) context.getBean("computerDAOBean");
        ((ConfigurableApplicationContext) context).close();
        computerDAO.update(computerID, newComputerName, newIntroducedDate, newDiscontinuedDate,
                newCompanyID);
    }

    public void deleteComputer(Integer computerID) throws PersistenceException, IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        ComputerDAO computerDAO = (ComputerDAO) context.getBean("computerDAOBean");
        ((ConfigurableApplicationContext) context).close();
        computerDAO.delete(computerID);
    }

    public void deleteCompany(Integer companyID) throws PersistenceException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        CompanyDAO companyDAO = (CompanyDAO) context.getBean("companyDAOBean");
        ((ConfigurableApplicationContext) context).close();
        companyDAO.delete(companyID);
    }

    public boolean doesComputerEntryExist(int id) throws PersistenceException, IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        ComputerDAO computerDAO = (ComputerDAO) context.getBean("computerDAOBean");
        ((ConfigurableApplicationContext) context).close();
        return computerDAO.doesEntryExist(id);
    }

    public boolean doesCompanyEntryExist(int id) throws PersistenceException, IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        CompanyDAO companyDAO = (CompanyDAO) context.getBean("companyDAOBean");
        ((ConfigurableApplicationContext) context).close();
        return companyDAO.doesEntryExist(id);
    }

    public int countComputerEntries() throws PersistenceException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        ComputerDAO computerDAO = (ComputerDAO) context.getBean("computerDAOBean");
        ((ConfigurableApplicationContext) context).close();
        return computerDAO.countEntries();
    }

    public Object countComputerEntriesWhere(String searchTerm) throws PersistenceException {
        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        ComputerDAO computerDAO = (ComputerDAO) context.getBean("computerDAOBean");
        ((ConfigurableApplicationContext) context).close();
        return computerDAO.countEntriesWhere(searchTerm);
    }
}
