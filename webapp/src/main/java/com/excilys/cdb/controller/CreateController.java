package com.excilys.cdb.controller;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

/**
 * @author Victor Gambier
 *
 */
@Controller
@RequestMapping("/addComputer")
public class CreateController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateController.class);

    private ComputerService computerService;
    private CompanyService companyService;
    private Validator validator;

    @Autowired
    public CreateController(ComputerService computerService, CompanyService companyService,
            Validator validator) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.validator = validator;
    }

    @GetMapping
    protected String initForm(ModelMap model) {

        LOG.info("Settings attributes for CreateServlet.");
        model.addAttribute("companies", companyService.listAllCompanies());

        return "addComputer";
    }

    @PostMapping
    protected String addComputerFromForm(@RequestParam(value = "computerName") String computerName,
            @RequestParam(value = "introduced") String introducedString,
            @RequestParam(value = "discontinued") String discontinuedString,
            @RequestParam(value = "companyID") String companyIDString, ModelMap model) {

        StringBuilder str = new StringBuilder();
        boolean isEntryValid = true;

        // Back-end validation

        if (computerName.equals("")) {
            str.append("Name is mandatory.\n");
            isEntryValid = false;
        }

        Date introduced = null;
        if (!introducedString.equals("") && validator.isDateStringValid(introducedString)) {
            introduced = java.sql.Date.valueOf(introducedString);
        } else if (!introducedString.equals("")) {
            str.append("Introduced date field must be empty or match YYYY-MM-DD format.\n");
            isEntryValid = false;
        }

        Date discontinued = null;
        if (!discontinuedString.equals("") && validator.isDateStringValid(discontinuedString)) {
            discontinued = java.sql.Date.valueOf(discontinuedString);
        } else if (!discontinuedString.equals("")) {
            str.append("Discontinued date field must be empty or match YYYY-MM-DD format.\n");
            isEntryValid = false;
        }

        if (isEntryValid && introduced != null && discontinued != null
                && discontinued.before(introduced)) {
            str.append("The date of discontinuation must be after the date of introduction.\n");
        }

        Integer companyID = null;
        if (companyIDString.equals("0")) { // If the user chose the "--" default option
            companyID = null; // Needed for the Computer constructor to function as intended
        } else if (!companyIDString.equals("") && validator.isStringInteger(companyIDString)
                && companyService.doesCompanyEntryExist(Integer.valueOf(companyIDString))) {
            companyID = Integer.valueOf(companyIDString);
        } else {
            str.append("Company ID field must be empty (--) or a valid ID.\n");
            // Here, "empty" means the "--" choice of id 0
            isEntryValid = false;
        }

        // Adding entry if form is valid

        if (str.length() == 0) { // If all fields are valid

            // Fetching corresponding Company object
            Company company = companyID == null ? null : companyService.getCompany(companyID);

            Computer addedComputer = new Computer.Builder().withName(computerName)
                    .withIntroduced(introduced).withDiscontinued(discontinued).withCompany(company)
                    .build();
            computerService.addComputer(addedComputer);

            model.addAttribute("message", "Entry successfully added.");
        } else {
            model.addAttribute("message", str.toString());
        }

        return "addComputer";

    }
}