package com.excilys.cdb.controller;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.model.ComputerPage;
import com.excilys.cdb.model.ModelException;
import com.excilys.cdb.persistence.PersistenceException;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

/**
 * @author Victor Gambier
 *
 */
@Controller
@RequestMapping("/")
public class DashboardController {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

    private ComputerService computerService;
    private Validator validator;

    @Autowired
    public DashboardController(ComputerService computerService, Validator validator) {
        this.computerService = computerService;
        this.validator = validator;
    }

    @GetMapping
    protected String getMapping(
            @RequestParam(value = "currentPage", defaultValue = "1") String currentPageString,
            @RequestParam(value = "search", defaultValue = "") String searchTerm,
            @RequestParam(value = "orderBy", defaultValue = "computer_id") String orderBy,
            ModelMap model) throws ServletException {

        LOG.info("Settings attributes for DashboardServlet.");

        ComputerPage computerPage;

        int nbEntries = computerService.countComputerEntriesWhere(searchTerm);

        int currentPage = validator.isPageIDStringValid(nbEntries, currentPageString)
                ? Integer.valueOf(currentPageString)
                : 1;

        try {
            computerPage = new ComputerPage(nbEntries, currentPage);
        } catch (ModelException e) {
            throw new ServletException(
                    "Couldn't set session attributes because of failed ComputerPage initialization",
                    e);
        }

        try {
            model.addAttribute("computers",
                    computerService.getPageComputers(computerPage, searchTerm, orderBy));
        } catch (PersistenceException e) {
            throw new ServletException("Couldn't set 'computers' attributes", e);

        }

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("computerCount", nbEntries);
        model.addAttribute("nbPages", ComputerPage.getNbPages());

        return "dashboard";
    }

    // Used for changing the number of entries displayed per page
    @PostMapping
    protected String updateNumberEntries(@RequestParam("nbEntries") String newValue) {

        if (newValue != null) {
            int newNbEntriesPerPage = Integer.valueOf(newValue);
            ComputerPage.setMaxItemsPerPage(newNbEntriesPerPage);
        }

        return "redirect:/";
    }
}