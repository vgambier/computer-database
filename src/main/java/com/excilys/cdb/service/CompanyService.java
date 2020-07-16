package com.excilys.cdb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDAO;

/**
 * @author Victor Gambier
 *
 */
@Component
public class CompanyService {

    private CompanyDAO companyDAO;

    @Autowired
    public CompanyService(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    public List<Company> listAllCompanies() {
        return companyDAO.listAll();
    }

    public void deleteCompany(Integer companyID) {
        companyDAO.delete(companyID);
    }

    public boolean doesCompanyEntryExist(int id) {
        return companyDAO.doesEntryExist(id);
    }
}
