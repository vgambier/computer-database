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

import com.excilys.cdb.dao.PersistenceException;
import com.excilys.cdb.model.ComputerPage;
import com.excilys.cdb.model.ModelException;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.BindingValidator;

/**
 * @author Victor Gambier
 *
 */
@Controller
@RequestMapping("/")
public class DashboardController {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

    private ComputerService computerService;
    private BindingValidator validator;

    @Autowired
    public DashboardController(ComputerService computerService, BindingValidator validator) {
        this.computerService = computerService;
        this.validator = validator;
    }

    @GetMapping
    protected String getMapping(
            @RequestParam(value = "currentPage", defaultValue = "1") String currentPageString,
            @RequestParam(value = "search", defaultValue = "") String searchTerm,
            @RequestParam(value = "orderBy", defaultValue = "computer.id") String orderBy,
            ModelMap model) throws ServletException {

        LOG.info("Setting attributes for DashboardController.");

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
        model.addAttribute("search", searchTerm);
        model.addAttribute("orderBy", orderBy);

        return "dashboard";
    }

    // Used for changing the number of entries displayed per page
    @PostMapping
    protected String updateNumberEntries(@RequestParam("nbEntries") String newValue)
            throws ServletException {

        if (newValue != null) {
            int newNbEntriesPerPage = Integer.valueOf(newValue);
            try {
                ComputerPage.setMaxItemsPerPage(newNbEntriesPerPage);
            } catch (ModelException e) {
                throw new ServletException(
                        "Cannot set the number of entries to an integer lesser than 1", e);
            }
        }

        return "redirect:/";
    }
}