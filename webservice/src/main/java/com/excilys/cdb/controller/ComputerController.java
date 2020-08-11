package com.excilys.cdb.controller;

import com.excilys.cdb.dao.PersistenceException;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.RestDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerPage;
import com.excilys.cdb.model.ModelException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.BindingValidator;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.InvalidNewEntryException;
import com.excilys.cdb.exception.PageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@CrossOrigin
@RequestMapping("/api/computers")
@RestController
public class ComputerController {

    private ComputerService computerService;
    private CompanyService companyService;
    private BindingValidator validator;

    @Autowired
    public ComputerController(ComputerService computerService, CompanyService companyService, BindingValidator validator) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.validator = validator;
    }

    @GetMapping("/{stringID}")
    public Computer getComputerJSON(@PathVariable String stringID)
            throws ComputerNotFoundException {

        if (validator.isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            if (computerService.doesComputerEntryExist(id)) {
                return computerService.getComputer(id);
            }
            throw new ComputerNotFoundException("ID must correspond to an existing entry");
        }
        throw new ComputerNotFoundException("ID must be an integer");
    }

    @GetMapping()
    public List<Computer> getComputerPageJSON(@RequestBody RestDTO restDto)
            throws PersistenceException, ModelException, PageNotFoundException {

        // Default values for optional parameters

        if (restDto.getNbEntries() == null) {
            restDto.setNbEntries("25");
        }
        if (restDto.getSearchTerm() == null) {
            restDto.setSearchTerm("");
        }
        if (restDto.getOrderBy() == null) {
            restDto.setOrderBy("computer.id");
        }

        // Updating number of entries per page

        if (validator.isStringInteger(restDto.getNbEntries())) {
            int newNbEntriesPerPage = Integer.valueOf(restDto.getNbEntries());
            ComputerPage.setMaxItemsPerPage(newNbEntriesPerPage);
        }

        // Gathering list of computers in the page

        int nbComputers = computerService.countComputerEntries();
        if (validator.isPageIDStringValid(nbComputers, restDto.getId())) {
            ComputerPage computerPage = new ComputerPage(nbComputers, Integer.valueOf(restDto.getId()));
            List<Computer> computers = computerService.getPageComputers(computerPage, restDto.getSearchTerm(),
                    restDto.getOrderBy());
            return computers;
        }
        throw new PageNotFoundException();

    }

    @PostMapping()
    public void addComputerJSON(@RequestBody ComputerDTO computerDto) throws InvalidNewEntryException {

        // TODO: use the factorized validator

        // Back-end validation

        if (computerDto.getName().equals("")) {
            throw new InvalidNewEntryException("Name is mandatory.");
        }

        Date introduced = null;
        if (!computerDto.getIntroduced().equals("") && validator.isDateStringValid(computerDto.getIntroduced())) {
            introduced = java.sql.Date.valueOf(computerDto.getIntroduced());
        } else if (!computerDto.getIntroduced().equals("")) {
            throw new InvalidNewEntryException(
                    "Introduced date field must be empty or match YYYY-MM-DD format.");
        }

        Date discontinued = null;
        if (!computerDto.getDiscontinued().equals("") && validator.isDateStringValid(computerDto.getDiscontinued())) {
            discontinued = java.sql.Date.valueOf(computerDto.getDiscontinued());
        } else if (!computerDto.getDiscontinued().equals("")) {
            throw new InvalidNewEntryException(
                    "Discontinued date field must be empty or match YYYY-MM-DD format.");
        }

        if (introduced != null && discontinued != null && discontinued.before(introduced)) {
            throw new InvalidNewEntryException(
                    "The date of discontinuation must be after the date of introduction.");
        }

        Integer companyID = null;
        if (computerDto.getCompany().getId().equals("0")) {
            companyID = null; // Needed for the Computer constructor to function as intended
        } else if (!computerDto.getCompany().getId().equals("") && validator.isStringInteger(computerDto.getCompany().getId())
                && companyService.doesCompanyEntryExist(Integer.valueOf(computerDto.getCompany().getId()))) {
            companyID = Integer.valueOf(computerDto.getCompany().getId());
        } else {
            throw new InvalidNewEntryException("Company ID field must be 0 or a valid ID.\n");
        }

        // Adding entry if form is valid

        // Fetching corresponding Company object
        Company company = companyID == null ? null : companyService.getCompany(companyID);

        Computer addedComputer = new Computer.Builder().withName(computerDto.getName())
                .withIntroduced(introduced).withDiscontinued(discontinued).withCompany(company)
                .build();
        computerService.addComputer(addedComputer);
    }

    @DeleteMapping("/{stringID}")
    public void deleteComputer(@PathVariable String stringID) throws ComputerNotFoundException {

        String error = "";

        if (validator.isStringInteger(stringID)) {

            int id = Integer.valueOf(stringID);

            if (computerService.doesComputerEntryExist(id)) {
                Computer deletedComputer = computerService.getComputer(id);
                computerService.deleteComputer(deletedComputer);

            } else {
                error = "ID must correspond to an existing entry";
            }

        } else {
            error = "ID must be an integer";
        }

        if (!"".equals(error)) { // If the ID is invalid
            throw new ComputerNotFoundException(error);
        }
    }

    @PutMapping()
    public void editComputerJSON(@RequestBody ComputerDTO computerDto) throws InvalidNewEntryException {

        // TODO: use the factorized validator

        // Back-end validation

        Integer id = null;
        if (validator.isStringInteger(computerDto.getId())
                && computerService.doesComputerEntryExist(Integer.valueOf(computerDto.getId()))) {
            id = Integer.parseInt(computerDto.getId());
        } else {
            throw new InvalidNewEntryException("Computer ID is invalid.");
        }

        if (computerDto.getName().equals("")) {
            throw new InvalidNewEntryException("Name is mandatory.");
        }

        Date introduced = null;
        if (!computerDto.getIntroduced().equals("") && validator.isDateStringValid(computerDto.getIntroduced())) {
            introduced = java.sql.Date.valueOf(computerDto.getIntroduced());
        } else if (!computerDto.getIntroduced().equals("")) {
            throw new InvalidNewEntryException(
                    "Introduced date field must be empty or match YYYY-MM-DD format.");
        }

        Date discontinued = null;
        if (!computerDto.getDiscontinued().equals("") && validator.isDateStringValid(computerDto.getDiscontinued())) {
            discontinued = java.sql.Date.valueOf(computerDto.getDiscontinued());
        } else if (!computerDto.getDiscontinued().equals("")) {
            throw new InvalidNewEntryException(
                    "Discontinued date field must be empty or match YYYY-MM-DD format.");
        }

        if (introduced != null && discontinued != null && discontinued.before(introduced)) {
            throw new InvalidNewEntryException(
                    "The date of discontinuation must be after the date of introduction.");
        }

        Integer companyID = null;
            if (computerDto.getCompany().getId().equals("0")) {
                companyID = null; // Needed for the Computer constructor to function as intended
            } else if (!computerDto.getCompany().getId().equals("") && validator.isStringInteger(computerDto.getCompany().getId())
                    && companyService.doesCompanyEntryExist(Integer.valueOf(computerDto.getCompany().getId()))) {
                companyID = Integer.valueOf(computerDto.getCompany().getId());
            } else {
                throw new InvalidNewEntryException("Company ID field must be 0 or a valid ID.\n");
            }


        // Adding entry if form is valid

        // Fetching corresponding Company object
        Company company = companyID == null ? null : companyService.getCompany(companyID);

        Computer updatedComputer = new Computer.Builder().withId(id).withName(computerDto.getName())
                .withIntroduced(introduced).withDiscontinued(discontinued).withCompany(company)
                .build();
        computerService.updateComputer(updatedComputer);
    }
}
