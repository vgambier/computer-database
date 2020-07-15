package servlet;

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
import org.springframework.stereotype.Component;

import model.Computer;
import service.CompanyService;
import service.ComputerService;
import validator.Validator;

/**
 * @author Victor Gambier
 *
 */
@Component
@WebServlet(name = "EditServlet", urlPatterns = "/editComputer")
public class EditServlet extends HttpServlet {

    private static final long serialVersionUID = 0xED17L;

    private static final Logger LOG = LoggerFactory.getLogger(EditServlet.class);

    private ComputerService computerService;
    private CompanyService companyService;
    private Validator validator;

    @Autowired
    public EditServlet(ComputerService computerService, CompanyService companyService,
            Validator validator) {
        this.computerService = computerService;
        this.companyService = companyService;
        this.validator = validator;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.info("Settings attributes for EditServlet.");

        String computerIDString = request.getParameter("id");
        int computerID;

        try {
            if (validator.isStringInteger(computerIDString)
                    && computerService.doesComputerEntryExist(Integer.valueOf(computerIDString))) {
                computerID = Integer.valueOf(computerIDString);
                request.setAttribute("id", computerIDString);
            } else { // if no id was given, go back to the main page
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                return;
            }
        } catch (IOException e) {
            throw new ServletException("Couldn't set session attributes", e);
        }

        // Fill the form with existing data

        Computer computer = computerService.getComputer(computerID);

        request.setAttribute("computer", computer);
        request.setAttribute("companies", companyService.listAllCompanies());

        request.getRequestDispatcher("editComputer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO: validation is redundant with CreateServlet - back-end validation doesn't need to
        // have explicit error
        // messages; therefore, extracting part of this method is possible
        // the extracted method will return a boolean, but the specific error will not be regarded
        // The extracted method should be a Validator method that takes a DTO as input

        StringBuilder str = new StringBuilder();
        boolean isEntryValid = true;

        // Back-end validation

        Integer id = null;
        String idString = request.getParameter("id");

        if (!validator.isStringInteger(idString)
                || !computerService.doesComputerEntryExist(Integer.valueOf(idString))) {
            str.append("Computer ID is invalid.\n");
            isEntryValid = false;
        } else {
            id = Integer.parseInt(idString);
        }

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
            computerService.updateComputer(id, computerName, introduced, discontinued, companyID);
            request.setAttribute("message", "Entry successfully edited.");
        } else {
            request.setAttribute("message", str.toString());
        }

        doGet(request, response);

    }
}