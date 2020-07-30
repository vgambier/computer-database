package com.excilys.cdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.service.ComputerService;

/**
 * @author Victor Gambier
 *
 */
@RestController
public class CreateRESTController {

    private ComputerService computerService;

    @Autowired
    public CreateRESTController(ComputerService computerService) {
        this.computerService = computerService;
    }

    // TODO: add create computer feature, with displaying of existing data and validation
}