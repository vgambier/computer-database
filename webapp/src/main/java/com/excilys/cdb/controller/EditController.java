package com.excilys.cdb.controller;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.BindingValidator;
import com.excilys.cdb.validator.ServiceValidator;

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
    private BindingValidator bindingValidator;
    private ServiceValidator serviceValidator;

    @Autowired
    public EditController(ComputerService computerService, CompanyService companyService,
            BindingValidator validator, ServiceValidator serviceValidator) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.bindingValidator = validator;
        this.serviceValidator = serviceValidator;
    }

    @GetMapping
    protected String initForm(@RequestParam(value = "id") String computerIDString, ModelMap model) {

        LOG.info("Settings attributes for EditServlet.");

        int computerID;

        if (bindingValidator.isStringInteger(computerIDString)
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
    protected void editComputerFromForm(@ModelAttribute(value = "id") String idString,
            @RequestParam(value = "computerName") String computerName,
            @RequestParam(value = "introduced") String introducedString,
            @RequestParam(value = "discontinued") String discontinuedString,
            @RequestParam(value = "companyID") String companyIDString, ModelMap model) {

        CompanyDTO companyDTO = new CompanyDTO.Builder().withId(companyIDString).build();
        ComputerDTO computerDTO = new ComputerDTO.Builder().withId(idString).withName(computerName)
                .withIntroduced(introducedString).withDiscontinued(discontinuedString)
                .withCompany(companyDTO).build();
        List<String> errorMessages = serviceValidator.validateComputerDTO(computerDTO);

        // Back-end validation

        if (errorMessages.isEmpty()) { // No error messages means the DTO is valid, so we can update
                                       // the computer

            Integer id = Integer.parseInt(idString);
            Date introduced = "".equals(introducedString) ? null : Date.valueOf(introducedString);
            Date discontinued = "".equals(discontinuedString)
                    ? null
                    : Date.valueOf(discontinuedString);
            Integer companyID = "0".equals(companyIDString)
                    ? null
                    : Integer.valueOf(companyIDString);

            // Fetching corresponding Company object
            Company company = companyID == null ? null : companyService.getCompany(companyID);

            Computer updatedComputer = new Computer.Builder().withId(id).withName(computerName)
                    .withIntroduced(introduced).withDiscontinued(discontinued).withCompany(company)
                    .build();

            computerService.updateComputer(updatedComputer);
            model.addAttribute("message", "Entry successfully edited.");

        } else {
            String concatenatedErrorMessage = errorMessages.stream()
                    .collect(Collectors.joining("\n"));
            model.addAttribute("message", concatenatedErrorMessage);
        }

        initForm(idString, model);

    }
}