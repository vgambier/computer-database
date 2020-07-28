package com.excilys.cdb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.CompanyDAO;
import com.excilys.cdb.model.Company;

/**
 * @author Victor Gambier
 *
 */
@Service("companyServiceBean")
public class CompanyService {

    private CompanyDAO companyDAO;

    @Autowired
    public CompanyService(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    public Company getCompany(int companyID) {
        return companyDAO.find(companyID);
    }

    public List<Company> listAllCompanies() {
        return companyDAO.listAll();
    }

    public void deleteCompany(Company company) {
        companyDAO.delete(company);
    }

    public boolean doesCompanyEntryExist(int id) {
        return companyDAO.doesEntryExist(id);
    }
}
