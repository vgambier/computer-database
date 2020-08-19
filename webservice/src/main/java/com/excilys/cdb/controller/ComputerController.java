package com.excilys.cdb.controller;

import com.excilys.cdb.dao.PersistenceException;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.InvalidNewEntryException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.CompanyDTOMapper;
import com.excilys.cdb.model.*;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.BindingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@CrossOrigin
@RequestMapping("/api/computers")
@RestController
public class ComputerController {

    private final ComputerService computerService;
    private final CompanyService companyService;
    private final BindingValidator validator;
    private final CompanyDTOMapper companyDTOMapper;

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
            int id = Integer.parseInt(stringID);
            if (computerService.doesComputerEntryExist(id)) {
                return computerService.getComputer(id);
            }
            throw new ComputerNotFoundException("ID must correspond to an existing entry");
        }
        throw new ComputerNotFoundException("ID must be an integer");
    }


    @GetMapping(value = {"/page/{numPage}","/page/{numPage}/{pageLength}","/page/{numPage}/{pageLength}/{orderBy}","/page/{numPage}/{pageLength}/{orderBy}/{ascendingOrder}","/page/{numPage}/{pageLength}/{orderBy}/{ascendingOrder}/{searchTerm}"})
    public List<ComputerDTO> getComputerPageJSON2(@PathVariable String numPage,
                                                 @PathVariable(required = false) String searchTerm,
                                                 @PathVariable(required = false) String orderBy,
                                                 @PathVariable(required = false) String pageLength,
                                                 @PathVariable(required = false) String ascendingOrder ) {

        Page pageComputer = new Page(computerService.countComputerEntries(),Computer.parseAttribute(orderBy));
        pageComputer.setPageLength(pageLength);
        pageComputer.setSearch(searchTerm);
        pageComputer.goTo(numPage);
        pageComputer.setAscendingOrder(ascendingOrder);

        return computerService.getPageComputers(pageComputer);

    }

    @PostMapping()
    public String addComputerJSON(@RequestBody ComputerDTO computerDto) throws InvalidNewEntryException {

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
        return computerService.addComputer(addedComputer);
    }


    @DeleteMapping("/{stringID}")
    public void deleteComputer(@PathVariable String stringID) throws ComputerNotFoundException {

        String error = "";

        if (validator.isStringInteger(stringID)) {
            int id = Integer.parseInt(stringID);

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
                && computerService.doesComputerEntryExist(Integer.parseInt(computerDto.getId()))) {
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







    private Date validateDate(String intro, String s) throws InvalidNewEntryException {
        Date introduced = null;
        introduced = validateDate(introduced, intro, s);
        return introduced;
    }

    private Date validateDate(Date introduced, String intro, String s) throws InvalidNewEntryException {
        if (intro!=null && !("").equals(intro) && validator.isDateStringValid(intro)) {
            introduced = Date.valueOf(intro);
        } else if ( intro!=null && !("").equals(intro)) {
            throw new InvalidNewEntryException(s);
        }
        return introduced;
    }

    private Company validateCompany(@RequestBody ComputerDTO computerDto) throws InvalidNewEntryException {
        Integer companyID = null;
        if (computerDto.getCompany().getId()==null || computerDto.getCompany().getId().equals("0")) {
            companyID = null; // Needed for the Computer constructor to function as intended
        } else if (!("").equals(computerDto.getCompany().getId()) && validator.isStringInteger(computerDto.getCompany().getId())
                && companyService.doesCompanyEntryExist(Integer.parseInt(computerDto.getCompany().getId()))) {
            companyID = Integer.valueOf(computerDto.getCompany().getId());
        } else {
            throw new InvalidNewEntryException("Company ID field must be 0 or a valid ID.\n");
        }
        // Adding entry if form is valid
        // Fetching corresponding Company object
        return companyID == null ? null : companyDTOMapper.fromDTOtoModel(companyService.getCompany(companyID));
    }


}
