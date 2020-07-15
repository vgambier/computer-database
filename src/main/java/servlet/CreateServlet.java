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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.spring.AppConfiguration;
import config.spring.JdbcConfiguration;
import service.Service;
import validator.Validator;

@WebServlet(name = "CreateServlet", urlPatterns = "/addComputer")
public class CreateServlet extends HttpServlet {

    private static final long serialVersionUID = 0xC2EA7EL;

    private static Service service = (Service) new AnnotationConfigApplicationContext(
            AppConfiguration.class, JdbcConfiguration.class).getBean("serviceBean");

    private static final Logger LOG = LoggerFactory.getLogger(CreateServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.info("Settings attributes for CreateServlet.");

        request.setAttribute("companies", service.listAllCompanies());

        request.getRequestDispatcher("WEB-INF/addComputer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Validator validator = service.getValidator();
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
                    && service.doesCompanyEntryExist(Integer.valueOf(companyIDString))) {
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
            service.addComputer(computerName, introduced, discontinued, companyID);
            request.setAttribute("message", "Entry successfully added.");
        } else {
            request.setAttribute("message", str.toString());
        }

        doGet(request, response);

    }
}