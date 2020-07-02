package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import mapper.MapperException;
import model.ComputerPage;
import model.ModelException;
import persistence.PersistenceException;
import service.Service;
import validator.Validator;

@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 0xDA57B0A2DL;
    private static Service service = Service.getInstance();
    private static final Logger LOG = Logger.getLogger(DashboardServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BasicConfigurator.configure(); // configuring the Logger
        LOG.info("Settings attributes for MainServlet.");

        String currentPageString = request.getParameter("currentPage");
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

            int currentPage = Validator.getInstance().isPageIDStringValid(currentPageString)
                    ? Integer.valueOf(currentPageString)
                    : 1;
            request.setAttribute("currentPage", currentPage);

            // TODO remove
            for (int i = 0; i < 1000; i++) {
                System.out.println("HELLO" + orderBy);
            }

            computerPage = new ComputerPage(currentPage, searchTerm, orderBy);
            request.setAttribute("computerPage", computerPage);
            request.setAttribute("computerCount", service.countComputerEntriesWhere(searchTerm));

        } catch (IOException e) {
            throw new ServletException("Couldn't set session attributes", e);
        } catch (ModelException e) {
            throw new ServletException("Couldn't set session attributes", e);
        } catch (MapperException e) {
            throw new ServletException("Couldn't set session attributes", e);
        } catch (PersistenceException e) {
            throw new ServletException("Couldn't set session attributes", e);
        }

        request.setAttribute("nbPages", computerPage.getNbPages());

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Used for changing the number of entries per page
        String newValue = request.getParameter("action");

        if (newValue != null) {
            int newNbEntriesPerPage = Integer.valueOf(request.getParameter("action"));
            System.out.println(newNbEntriesPerPage);
            ComputerPage.setMaxItemsPerPage(newNbEntriesPerPage);
        }

        doGet(request, response);
    }
}