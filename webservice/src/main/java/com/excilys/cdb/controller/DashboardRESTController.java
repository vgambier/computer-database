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

/**
 * @author Victor Gambier
 *
 */
@RestController
public class DashboardRESTController {

    private ComputerService computerService;

    @Autowired
    public DashboardRESTController(ComputerService computerService) {
        this.computerService = computerService;
    }

    @GetMapping("/computer/{id}")
    public Computer read(@PathVariable String id) {
        return computerService.getComputer(Integer.valueOf(id));
    }

    // TODO: validation, error page
    @GetMapping("/page/{id}")
    public List<Computer> getComputerPageJSON(@PathVariable String id)
            throws PersistenceException, ModelException {

        int nbComputers = computerService.countComputerEntries();
        List<Computer> computers = computerService
                .getPageComputers(new ComputerPage(nbComputers, Integer.valueOf(id)));

        return computers;
    }

    // TODO: add all remaining controller functionalities
}