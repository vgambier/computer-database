package com.excilys.cdb.controller;

import com.excilys.cdb.dao.PersistenceException;
import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.RestDTO;
import com.excilys.cdb.mapper.CompanyDTOMapper;
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
    private CompanyDTOMapper companyDTOMapper;

    @Autowired
    public ComputerController(ComputerService computerService, CompanyService companyService, BindingValidator validator,CompanyDTOMapper companyDTOMapper) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.validator = validator;
        this.companyDTOMapper = companyDTOMapper;
    }


    @GetMapping("/count")
    public int countComputers()
    {
        return computerService.countComputerEntries();
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
/*
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
*/
@GetMapping(value = {"/page/{id}/{nbEntries}","/page/{id}", "/page/{id}/{nbEntries}/{searchTerm}/{orderBy}"})
public List<ComputerDTO> getComputerPageJSON(@PathVariable String id,
                                          @PathVariable(required = false) String searchTerm,
                                          @PathVariable(required = false) String orderBy,
                                          @PathVariable(required = false) String nbEntries)
        throws PersistenceException, ModelException, PageNotFoundException {

    // Default values for optional parameters

    if (nbEntries == null) {
        nbEntries = "25";
    }
    if (searchTerm == null) {
        searchTerm = "";
    }
    if (orderBy == null) {
        orderBy = "computer.id";
    }

    // Updating number of entries per page

    if (validator.isStringInteger(nbEntries)) {
        int newNbEntriesPerPage = Integer.valueOf(nbEntries);
        ComputerPage.setMaxItemsPerPage(newNbEntriesPerPage);
    }

    // Gathering list of computers in the page

    int nbComputers = computerService.countComputerEntries();
    if (validator.isPageIDStringValid(nbComputers, id)) {
        ComputerPage computerPage = new ComputerPage(nbComputers, Integer.valueOf(id));
        List<ComputerDTO> computers = computerService.getPageComputers(computerPage, searchTerm,
                orderBy);
        return computers;
    }
    throw new PageNotFoundException();

}

    @PostMapping()
    public void addComputerJSON(@RequestBody ComputerDTO computerDto) throws InvalidNewEntryException {

        // Back-end validation
        if (computerDto.getName()==null || computerDto.getName().equals("")) {
            throw new InvalidNewEntryException("Name is mandatory.");
        }

        Date introduced = validateDate(computerDto.getIntroduced(), "Introduced date field must be empty or match YYYY-MM-DD format.");
        Date discontinued = validateDate(computerDto.getDiscontinued(), "Discontinued date field must be empty or match YYYY-MM-DD format.");
        if (introduced != null && discontinued != null && discontinued.before(introduced)) {
            throw new InvalidNewEntryException(
                    "The date of discontinuation must be after the date of introduction.");
        }

        Company company = validateCompany(computerDto);
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

        Date introduced = validateDate(computerDto.getIntroduced(), "Introduced date field must be empty or match YYYY-MM-DD format.");
        Date discontinued = validateDate(computerDto.getDiscontinued(), "Discontinued date field must be empty or match YYYY-MM-DD format.");
        if (introduced != null && discontinued != null && discontinued.before(introduced)) {
            throw new InvalidNewEntryException(
                    "The date of discontinuation must be after the date of introduction.");
        }

        Company company = validateCompany(computerDto);
        Computer updatedComputer = new Computer.Builder().withId(id).withName(computerDto.getName())
                .withIntroduced(introduced).withDiscontinued(discontinued).withCompany(company)
                .build();
        computerService.updateComputer(updatedComputer);
    }







    private Date validateDate(String introduced2, String s) throws InvalidNewEntryException {
        Date introduced = null;
        introduced = validateDate(introduced, introduced2, s);
        return introduced;
    }

    private Date validateDate(Date introduced, String introduced2, String s) throws InvalidNewEntryException {
        if (introduced2!=null && !("").equals(introduced2) && validator.isDateStringValid(introduced2)) {
            introduced = Date.valueOf(introduced2);
        } else if ( introduced2!=null && !("").equals(introduced2)) {
            throw new InvalidNewEntryException(
                    s);
        }
        return introduced;
    }

    private Company validateCompany(@RequestBody ComputerDTO computerDto) throws InvalidNewEntryException {
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
        return companyID == null ? null : companyDTOMapper.fromDTOtoModel(companyService.getCompany(companyID));
    }


}
