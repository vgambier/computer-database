package com.excilys.cdb.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.BindingValidator;

import exception.InvalidNewEntryException;

/**
 * @author Victor Gambier
 *
 */
@RestController
public class EditRESTController {

    private ComputerService computerService;
    private CompanyService companyService;
    private BindingValidator validator;

    @Autowired
    public EditRESTController(ComputerService computerService, CompanyService companyService,
            BindingValidator validator) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.validator = validator;
    }

    @GetMapping("/editcomputer/{idString}/{nameString}/{introducedString}/{discontinuedString}/{companyIDString}")
    public void editComputerJSON(@PathVariable String idString, @PathVariable String nameString,
            @PathVariable String introducedString, @PathVariable String discontinuedString,
            @PathVariable String companyIDString) throws InvalidNewEntryException {

        // TODO: use the factorized validator

        // Back-end validation

        Integer id = null;
        if (validator.isStringInteger(idString)
                && computerService.doesComputerEntryExist(Integer.valueOf(idString))) {
            id = Integer.parseInt(idString);
        } else {
            throw new InvalidNewEntryException("Computer ID is invalid.");
        }

        if (nameString.equals("")) {
            throw new InvalidNewEntryException("Name is mandatory.");
        }

        Date introduced = null;
        if (!introducedString.equals("") && validator.isDateStringValid(introducedString)) {
            introduced = java.sql.Date.valueOf(introducedString);
        } else if (!introducedString.equals("")) {
            throw new InvalidNewEntryException(
                    "Introduced date field must be empty or match YYYY-MM-DD format.");
        }

        Date discontinued = null;
        if (!discontinuedString.equals("") && validator.isDateStringValid(discontinuedString)) {
            discontinued = java.sql.Date.valueOf(discontinuedString);
        } else if (!discontinuedString.equals("")) {
            throw new InvalidNewEntryException(
                    "Discontinued date field must be empty or match YYYY-MM-DD format.");
        }

        if (introduced != null && discontinued != null && discontinued.before(introduced)) {
            throw new InvalidNewEntryException(
                    "The date of discontinuation must be after the date of introduction.");
        }

        Integer companyID = null;
        if (companyIDString.equals("0")) {
            companyID = null; // Needed for the Computer constructor to function as intended
        } else if (!companyIDString.equals("") && validator.isStringInteger(companyIDString)
                && companyService.doesCompanyEntryExist(Integer.valueOf(companyIDString))) {
            companyID = Integer.valueOf(companyIDString);
        } else {
            throw new InvalidNewEntryException("Company ID field must be 0 or a valid ID.\n");
        }

        // Adding entry if form is valid

        // Fetching corresponding Company object
        Company company = companyID == null ? null : companyService.getCompany(companyID);

        Computer updatedComputer = new Computer.Builder().withId(id).withName(nameString)
                .withIntroduced(introduced).withDiscontinued(discontinued).withCompany(company)
                .build();
        computerService.updateComputer(updatedComputer);
    }
}