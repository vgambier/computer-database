package com.excilys.cdb.controller;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.validator.BindingValidator;
import com.excilys.cdb.exception.CompanyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("/companies")
@RestController
public class CompanyController {

    private CompanyService companyService;
    private BindingValidator validator;

    @Autowired
    public CompanyController(CompanyService companyService,  BindingValidator validator
                             ) {
        this.companyService = companyService;
        this.validator = validator;
    }

    @DeleteMapping("/{stringID}")
    public void deleteCompany(@PathVariable String stringID) throws CompanyNotFoundException {

        boolean found = false;

        if (validator.isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            if (companyService.doesCompanyEntryExist(id)) {
                Company deletedCompany = companyService.getCompany(id);
                companyService.deleteCompany(deletedCompany);
                found = true;
            }
        }
        if (!found) {
            throw new CompanyNotFoundException();
        }
    }

    @GetMapping()
    public List<Company> getAllCompanies() {

       return companyService.listAllCompanies();
        }

    }
