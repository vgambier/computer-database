package com.excilys.cdb.service;

import java.util.List;
import java.util.stream.Collectors;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.CompanyDTOMapper;
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

    public CompanyDTO getCompany(int companyID) {
        return CompanyDTOMapper.fromModeltoDTO(companyDAO.find(companyID));
    }

    public List<CompanyDTO> listAllCompanies() {
        List <Company> temp=companyDAO.listAll();
        List <CompanyDTO> companies = temp.stream().map(company -> CompanyDTOMapper.fromModeltoDTO(company)).collect(Collectors.toList());
        return companies;
    }

    public void deleteCompany(Company company) {
        companyDAO.delete(company);
    }

    public boolean doesCompanyEntryExist(int id) {
        return companyDAO.doesEntryExist(id);
    }

    public int countCompanies() { return companyDAO.countEntries();}
}
