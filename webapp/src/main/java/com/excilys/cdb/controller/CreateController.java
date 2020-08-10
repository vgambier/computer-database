package com.excilys.cdb.controller;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.excilys.cdb.mapper.CompanyDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.ServiceValidator;

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
    private ServiceValidator serviceValidator;

    @Autowired
    public CreateController(ComputerService computerService, CompanyService companyService,
            ServiceValidator serviceValidator) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.serviceValidator = serviceValidator;
    }

    @GetMapping
    protected String initForm(ModelMap model) {

        LOG.info("Setting attributes for CreateController.");
        model.addAttribute("companies", companyService.listAllCompanies());

        return "addComputer";
    }

    @PostMapping
    protected void addComputerFromForm(@RequestParam(value = "computerName") String computerName,
            @RequestParam(value = "introduced") String introducedString,
            @RequestParam(value = "discontinued") String discontinuedString,
            @RequestParam(value = "companyID") String companyIDString, ModelMap model) {

        CompanyDTO companyDTO = new CompanyDTO.Builder().withId(companyIDString).build();
        ComputerDTO computerDTO = new ComputerDTO.Builder().withName(computerName)
                .withIntroduced(introducedString).withDiscontinued(discontinuedString)
                .withCompany(companyDTO).build();
        List<String> errorMessages = serviceValidator.validateComputerDTO(computerDTO);

        // Back-end validation

        if (errorMessages.isEmpty()) { // No error messages means the DTO is valid, so we can update
                                       // the computer

            Date introduced = "".equals(introducedString) ? null : Date.valueOf(introducedString);
            Date discontinued = "".equals(discontinuedString)
                    ? null
                    : Date.valueOf(discontinuedString);
            Integer companyID = "0".equals(companyIDString)
                    ? null
                    : Integer.valueOf(companyIDString);

            // Fetching corresponding Company object
            Company company = companyID == null ? null : CompanyDTOMapper.fromDTOtoModel(companyService.getCompany(companyID));;

            Computer addedComputer = new Computer.Builder().withName(computerName)
                    .withIntroduced(introduced).withDiscontinued(discontinued).withCompany(company)
                    .build();
            computerService.addComputer(addedComputer);

            model.addAttribute("message", "Entry successfully added.");

        } else {
            String concatenatedErrorMessage = errorMessages.stream()
                    .collect(Collectors.joining("\n"));
            model.addAttribute("message", concatenatedErrorMessage);
        }

        initForm(model);
    }
}
