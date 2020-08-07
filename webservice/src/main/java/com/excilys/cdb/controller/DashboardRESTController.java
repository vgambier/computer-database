package com.excilys.cdb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dao.PersistenceException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerPage;
import com.excilys.cdb.model.ModelException;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.BindingValidator;

import exception.ComputerNotFoundException;
import exception.PageNotFoundException;

/**
 * @author Victor Gambier
 *
 */
@CrossOrigin
@RestController
public class DashboardRESTController {

    private ComputerService computerService;
    private BindingValidator validator;

    @Autowired
    public DashboardRESTController(ComputerService computerService, BindingValidator validator) {
        this.computerService = computerService;
        this.validator = validator;
    }

    @GetMapping("/computer/{stringID}")
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

    @GetMapping(value = {"/page/{id}", "/page/{id}/{searchTerm}/{orderBy}/{nbEntries}"})
    public List<Computer> getComputerPageJSON(@PathVariable String id,
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
            List<Computer> computers = computerService.getPageComputers(computerPage, searchTerm,
                    orderBy);
            return computers;
        }
        throw new PageNotFoundException();

    }
}
