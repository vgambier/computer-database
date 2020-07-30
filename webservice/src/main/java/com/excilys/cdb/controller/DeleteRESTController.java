package com.excilys.cdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

import exception.ComputerNotFoundException;

/**
 * @author Victor Gambier
 *
 */
@RestController
public class DeleteRESTController {

    private ComputerService computerService;
    private Validator validator;

    @Autowired
    public DeleteRESTController(ComputerService computerService, Validator validator) {
        this.computerService = computerService;
        this.validator = validator;
    }

    @GetMapping("/deletecomputer/{stringID}")
    public void read(@PathVariable String stringID) throws ComputerNotFoundException {

        boolean found = false;

        if (validator.isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            if (computerService.doesComputerEntryExist(id)) {
                Computer deletedComputer = computerService.getComputer(id);
                computerService.deleteComputer(deletedComputer);
                found = true;
            }
        }
        if (!found) {
            throw new ComputerNotFoundException();
        }
    }

    // TODO: add way to delete one company?

}