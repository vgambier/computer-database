package com.excilys.cdb.servlet;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.model.ComputerPage;
import com.excilys.cdb.model.ModelException;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

/**
 * @author Victor Gambier
 *
 */
@Controller
public class DashboardController {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

    private ComputerService computerService;
    private Validator validator;

    @Autowired
    public DashboardController(ComputerService computerService, Validator validator) {
        this.computerService = computerService;
        this.validator = validator;
    }

    // Allows automatic redirection to the main dashboard page when URL is "empty"
    @RequestMapping("/")
    public String redirectPage() {
        return "dashboard";
    }

    @GetMapping(path = "/dashboard")
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

        // TODO: uncomment
        // request.setAttribute("currentPage", currentPage);

        try {
            computerPage = new ComputerPage(nbEntries, currentPage);
        } catch (ModelException e) {
            throw new ServletException("Couldn't set session attributes", e);
        }
        // request.setAttribute("computers",
        // computerService.getPageComputers(computerPage, searchTerm, orderBy));
        // request.setAttribute("computerCount", nbEntries);

        // request.setAttribute("nbPages", ComputerPage.getNbPages());

        return "dashboard";
    }

    // Used for changing the number of entries per page
    @PostMapping("/dashboard")
    protected void updateNumberEntries(@ModelAttribute("action") String newValue) {

        // TODO: rename action

        if (newValue != null) {
            int newNbEntriesPerPage = Integer.valueOf(newValue);
            ComputerPage.setMaxItemsPerPage(newNbEntriesPerPage);
        }

        // TODO getMapping(request, response);
    }
}