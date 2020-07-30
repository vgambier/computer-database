package com.excilys.cdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.service.ComputerService;

/**
 * @author Victor Gambier
 *
 */
@RestController
public class EditRESTController {

    private ComputerService computerService;

    @Autowired
    public EditRESTController(ComputerService computerService) {
        this.computerService = computerService;
    }

    // TODO: add create computer feature, with validation

}