package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.ComputerPage;
import model.ModelException;
import persistence.PersistenceException;
import service.ComputerService;
import validator.Validator;

/**
 * @author Victor Gambier
 *
 */
@Component
@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 0xDA57B0A2DL;
    private static final Logger LOG = LoggerFactory.getLogger(DashboardServlet.class);

    private ComputerService computerService;
    private Validator validator;

    @Autowired
    public DashboardServlet(ComputerService computerService, Validator validator) {
        this.computerService = computerService;
        this.validator = validator;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.info("Settings attributes for DashboardServlet.");

        String currentPageString = request.getParameter("currentPage");
        if (currentPageString == null) {
            currentPageString = "1";
        }
        String searchTerm = request.getParameter("search");
        String orderBy = request.getParameter("orderBy");

        if (searchTerm == null) {
            searchTerm = "";
        }
        request.setAttribute("search", searchTerm);

        if (orderBy == null) {
            orderBy = "computer_id";
        }
        request.setAttribute("orderBy", orderBy);

        ComputerPage computerPage;

        try {

            int nbEntries = computerService.countComputerEntriesWhere(searchTerm);

            int currentPage = validator.isPageIDStringValid(nbEntries, currentPageString)
                    ? Integer.valueOf(currentPageString)
                    : 1;
            request.setAttribute("currentPage", currentPage);

            computerPage = new ComputerPage(nbEntries, currentPage);
            request.setAttribute("computers",
                    computerService.getPageComputers(computerPage, searchTerm, orderBy));
            request.setAttribute("computerCount", nbEntries);

        } catch (ModelException e) {
            throw new ServletException("Couldn't set session attributes", e);
        } catch (PersistenceException e) {
            throw new ServletException("Couldn't set session attributes", e);
        }

        request.setAttribute("nbPages", ComputerPage.getNbPages());

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Used for changing the number of entries per page
        String newValue = request.getParameter("action");

        if (newValue != null) {
            int newNbEntriesPerPage = Integer.valueOf(request.getParameter("action"));
            ComputerPage.setMaxItemsPerPage(newNbEntriesPerPage);
        }

        doGet(request, response);
    }
}