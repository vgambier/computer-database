package com.excilys.cdb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.mapper.CompanyDTOMapper;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.validator.BindingValidator;

@CrossOrigin
@RequestMapping("/api/companies")
@RestController
public class CompanyController {

    private final CompanyService companyService;
    private final BindingValidator validator;
    private final CompanyDTOMapper companyDTOMapper;

    @Autowired
    public CompanyController(CompanyService companyService, BindingValidator validator,
            CompanyDTOMapper companyDTOMapper) {
        this.companyService = companyService;
        this.validator = validator;
        this.companyDTOMapper = companyDTOMapper;
    }

    @GetMapping("/{stringID}")
    public CompanyDTO getCompanyJSON(@PathVariable String stringID)
            throws CompanyNotFoundException {

        if (validator.isStringInteger(stringID)) {
            int id = Integer.parseInt(stringID);
            if (companyService.doesCompanyEntryExist(id)) {
                return companyService.getCompany(id);

            } else {
                throw new CompanyNotFoundException();
            }
        } else {
            throw new CompanyNotFoundException();
        }
    }

    @GetMapping()
    public List<CompanyDTO> getCompanies() {

        return companyService.listAllCompanies();
    }

    @GetMapping("/count")
    public int countCompanies() {

        return companyService.countCompanies();
    }

    @DeleteMapping("/{stringID}")
    public void deleteCompany(@PathVariable String stringID) throws CompanyNotFoundException {

        boolean found = false;

        if (validator.isStringInteger(stringID)) {
            int id = Integer.parseInt(stringID);
            if (companyService.doesCompanyEntryExist(id)) {
                companyService.deleteCompany(id);
                found = true;
            }
        }
        if (!found) {
            throw new CompanyNotFoundException();
        }
    }

}
