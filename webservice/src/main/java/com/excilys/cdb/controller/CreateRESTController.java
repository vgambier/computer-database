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
import com.excilys.cdb.validator.Validator;

import exception.InvalidNewEntryException;

/**
 * @author Victor Gambier
 *
 */
@RestController
public class CreateRESTController {

    private ComputerService computerService;
    private CompanyService companyService;
    private Validator validator;

    @Autowired
    public CreateRESTController(ComputerService computerService, Validator validator,
            CompanyService companyService) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.validator = validator;
    }

    // TODO: Empty fields are currently not supported

    @GetMapping("/addcomputer/{nameString}/{introducedString}/{discontinuedString}/{companyIDString}")
    public void addComputerJSON(@PathVariable String nameString,
            @PathVariable String introducedString, @PathVariable String discontinuedString,
            @PathVariable String companyIDString) throws InvalidNewEntryException {

        // Back-end validation

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

        Computer addedComputer = new Computer.Builder().withName(nameString)
                .withIntroduced(introduced).withDiscontinued(discontinued).withCompany(company)
                .build();
        computerService.addComputer(addedComputer);
    }
}