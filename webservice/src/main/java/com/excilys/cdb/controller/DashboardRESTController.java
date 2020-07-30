package com.excilys.cdb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dao.PersistenceException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerPage;
import com.excilys.cdb.model.ModelException;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

import exception.ComputerNotFoundException;
import exception.PageNotFoundException;

/**
 * @author Victor Gambier
 *
 */
@RestController
public class DashboardRESTController {

    private ComputerService computerService;
    private Validator validator;

    @Autowired
    public DashboardRESTController(ComputerService computerService, Validator validator) {
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
        }
        throw new ComputerNotFoundException();
    }

    @GetMapping("/page/{id}")
    public List<Computer> getComputerPageJSON(@PathVariable String id)
            throws PersistenceException, ModelException, PageNotFoundException {

        int nbComputers = computerService.countComputerEntries();

        if (validator.isPageIDStringValid(nbComputers, id)) {
            List<Computer> computers = computerService
                    .getPageComputers(new ComputerPage(nbComputers, Integer.valueOf(id)));

            return computers;
        }
        throw new PageNotFoundException();

    }

    // TODO: add search feature

    // TODO: add orderBy feature + validation

    // TODO: add number of entries per page feature

}