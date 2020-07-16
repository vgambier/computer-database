package com.excilys.cdb.controller;

import java.sql.Date;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

/**
 * @author Victor Gambier
 *
 */
@Controller
@RequestMapping("/editComputer")
public class EditController {

    private static final Logger LOG = LoggerFactory.getLogger(EditController.class);

    private ComputerService computerService;
    private CompanyService companyService;
    private Validator validator;

    @Autowired
    public EditController(ComputerService computerService, CompanyService companyService,
            Validator validator) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.validator = validator;
    }

    @GetMapping
    protected String doGet(@RequestParam(value = "id") String computerIDString, ModelMap model) {

        LOG.info("Settings attributes for EditServlet.");

        int computerID;

        if (validator.isStringInteger(computerIDString)
                && computerService.doesComputerEntryExist(Integer.valueOf(computerIDString))) {
            computerID = Integer.valueOf(computerIDString);
            model.addAttribute("id", computerIDString);
        } else { // if no id was given, go back to the main page
            return "dashboard";
        }

        // Fill the form with existing data

        Computer computer = computerService.getComputer(computerID);

        model.addAttribute("computer", computer);
        model.addAttribute("companies", companyService.listAllCompanies());

        return "editComputer";
    }

    @PostMapping
    protected String doPost(@ModelAttribute(value = "id") String idString,
            @RequestParam(value = "computerName") String computerName,
            @RequestParam(value = "introduced") String introducedString,
            @RequestParam(value = "discontinued") String discontinuedString,
            @RequestParam(value = "companyID") String companyIDString, ModelMap model)

            throws ServletException {

        // TODO: validation is redundant with CreateServlet - back-end validation doesn't need to
        // have explicit error
        // messages; therefore, extracting part of this method is possible
        // the extracted method will return a boolean, but the specific error will not be regarded
        // The extracted method should be a Validator method that takes a DTO as input

        StringBuilder str = new StringBuilder();
        boolean isEntryValid = true;

        // Back-end validation

        Integer id = null;

        if (!validator.isStringInteger(idString)
                || !computerService.doesComputerEntryExist(Integer.valueOf(idString))) {
            str.append("Computer ID is invalid.\n");
            isEntryValid = false;
        } else {
            id = Integer.parseInt(idString);
        }

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
            str.append("The date of discontinuation must be after the date of introduction.");
        }

        Integer companyID = null;
        try {
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
        } catch (NumberFormatException e) {
            throw new ServletException("Couldn't check company ID validity!", e);
        }

        // Adding entry if form is valid

        if (str.length() == 0) { // If all fields are valid
            computerService.updateComputer(id, computerName, introduced, discontinued, companyID);
            model.addAttribute("message", "Entry successfully edited.");
        } else {
            model.addAttribute("message", str.toString());
        }

        return "redirect:/editComputer";

    }
}