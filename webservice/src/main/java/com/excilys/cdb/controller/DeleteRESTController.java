package com.excilys.cdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

import exception.CompanyNotFoundException;
import exception.ComputerNotFoundException;

/**
 * @author Victor Gambier
 *
 */
@RestController
public class DeleteRESTController {

    private ComputerService computerService;
    private CompanyService companyService;
    private Validator validator;

    @Autowired
    public DeleteRESTController(ComputerService computerService, Validator validator,
            CompanyService companyService) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.validator = validator;
    }

    @GetMapping("/deletecomputer/{stringID}")
    public void deleteComputer(@PathVariable String stringID) throws ComputerNotFoundException {

        boolean found = false;

        if (validator.isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            if (computerService.doesComputerEntryExist(id)) {
                Computer deletedComputer = computerService.getComputer(id);
                computerService.deleteComputer(deletedComputer);
                found = true;
            }
        }
        if (!found) {
            throw new ComputerNotFoundException();
        }
    }

    @GetMapping("/deletecompany/{stringID}")
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
}