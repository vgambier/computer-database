package com.excilys.cdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.service.ComputerService;

/**
 * @author Victor Gambier
 *
 */
@RestController
public class DeleteRESTController {

    private ComputerService computerService;

    @Autowired
    public DeleteRESTController(ComputerService computerService) {
        this.computerService = computerService;
    }

    // TODO: add way to delete one computer

}