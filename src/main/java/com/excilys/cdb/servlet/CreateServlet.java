package com.excilys.cdb.servlet;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

/**
 * @author Victor Gambier
 *
 */
@Controller
@WebServlet(name = "CreateServlet", urlPatterns = "/addComputer")
public class CreateServlet extends HttpServlet {

    private static final long serialVersionUID = 0xC2EA7EL;
    private static final Logger LOG = LoggerFactory.getLogger(CreateServlet.class);

    private ComputerService computerService;
    private CompanyService companyService;
    private Validator validator;

    @Autowired
    public CreateServlet(ComputerService computerService, CompanyService companyService,
            Validator validator) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.validator = validator;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.info("Settings attributes for CreateServlet.");

        request.setAttribute("companies", companyService.listAllCompanies());

        request.getRequestDispatcher("addComputer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder str = new StringBuilder();
        boolean isEntryValid = true;

        // Back-end validation

        String computerName = request.getParameter("computerName");
        if (computerName.equals("")) {
            str.append("Name is mandatory.\n");
            isEntryValid = false;
        }

        Date introduced = null;
        String introducedString = request.getParameter("introduced");
        if (!introducedString.equals("") && validator.isDateStringValid(introducedString)) {
            introduced = java.sql.Date.valueOf(introducedString);
        } else if (!introducedString.equals("")) {
            str.append("Introduced date field must be empty or match YYYY-MM-DD format.\n");
            isEntryValid = false;
        }

        Date discontinued = null;
        String discontinuedString = request.getParameter("discontinued");
        if (!discontinuedString.equals("") && validator.isDateStringValid(discontinuedString)) {
            discontinued = java.sql.Date.valueOf(discontinuedString);
        } else if (!discontinuedString.equals("")) {
            str.append("Discontinued date field must be empty or match YYYY-MM-DD format.\n");
            isEntryValid = false;
        }

        if (isEntryValid && introduced != null && discontinued != null
                && discontinued.before(introduced)) {
            str.append("The date of discontinuation must be after the date of introduction.");
        }

        Integer companyID = null;
        String companyIDString = request.getParameter("companyID");
        try {
            if (companyIDString.equals("0")) { // If the user chose the "--" default option
                companyID = null; // Needed for the Computer constructor to function as intended
            } else if (!companyIDString.equals("") && validator.isStringInteger(companyIDString)
                    && companyService.doesCompanyEntryExist(Integer.valueOf(companyIDString))) {
                companyID = Integer.valueOf(companyIDString);
            } else {
                str.append("Company ID field must be empty (--) or a valid ID.\n");
                // Here, "empty" means the "--" choice of id 0
                isEntryValid = false;
            }
        } catch (NumberFormatException e) {
            throw new ServletException("Couldn't check company ID validity!", e);
        }

        // Adding entry if form is valid

        if (str.length() == 0) { // If all fields are valid
            computerService.addComputer(computerName, introduced, discontinued, companyID);
            request.setAttribute("message", "Entry successfully added.");
        } else {
            request.setAttribute("message", str.toString());
        }

        doGet(request, response);

    }
}