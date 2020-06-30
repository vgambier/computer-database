package servlet;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import persistence.PersistenceException;
import service.Service;
import validator.Validator;

@WebServlet(name = "EditServlet", urlPatterns = "/editComputer")
public class EditServlet extends HttpServlet {

    private static final long serialVersionUID = 0xED17L;

    private static Service service = Service.getInstance();
    private static final Logger LOG = Logger.getLogger(EditServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BasicConfigurator.configure(); // configuring the Logger
        LOG.info("Settings attributes for EditServlet.");

        String computerIDString = request.getParameter("id");

        try {
            if (Validator.getInstance().isComputerIDStringValid(computerIDString)) {
                request.setAttribute("id", computerIDString);
            } else {
                // TODO
            }
        } catch (PersistenceException e) {
            throw new ServletException("Couldn't set session attributes", e);
        } catch (IOException e) {
            throw new ServletException("Couldn't set session attributes", e);
        }

        request.getRequestDispatcher("editComputer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO: validation is redundant

        Validator validator = Validator.getInstance();
        StringBuilder str = new StringBuilder();
        boolean isEntryValid = true;

        // Back-end validation

        Integer id = null;
        String idString = request.getParameter("id");
        System.out.println("@@@@@@@@@@" + idString + "@@");
        try {
            if (!validator.isComputerIDStringValid(idString)) {
                str.append("Computer ID is invalid.\n");
                isEntryValid = false;
            } else {
                id = Integer.parseInt(idString);
            }
        } catch (PersistenceException e) {
            throw new ServletException("Couldn't check computer ID validity!", e);
        } catch (IOException e) {
            throw new ServletException("Couldn't check computer ID validity!", e);

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
            } else if (!companyIDString.equals("")
                    && validator.isCompanyIDStringValid(companyIDString)) {
                companyID = Integer.valueOf(companyIDString);
            } else {
                str.append("Company ID field must be empty (--) or a valid ID.\n");
                // Here, "empty" means the "--" choice of id 0
                isEntryValid = false;
            }
        } catch (NumberFormatException e) {
            throw new ServletException("Couldn't check company ID validity!", e);
        } catch (PersistenceException e) {
            throw new ServletException("Couldn't check company ID validity!", e);
        } catch (IOException e) {
            throw new ServletException("Couldn't check company ID validity!", e);
        }

        // Adding entry if form is valid

        if (str.length() == 0) { // If all fields are valid
            try {
                service.updateComputer(id, computerName, introduced, discontinued, companyID);
                request.setAttribute("message", "Entry successfully edited.");
            } catch (PersistenceException e) {
                throw new ServletException("Could not edit the computer!", e);
            } catch (IOException e) {
                throw new ServletException("Could not edit the computer!", e);
            }
        } else {
            request.setAttribute("message", str.toString());
        }

        doGet(request, response);

    }
}