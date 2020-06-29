package servlet;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import persistence.PersistenceException;
import service.Service;

@WebServlet(name = "CreateServlet", urlPatterns = "/addComputer")
public class CreateServlet extends HttpServlet {

    private static final long serialVersionUID = 5L;

    private static Service service = Service.getInstance();
    private static final Logger LOG = Logger.getLogger(CreateServlet.class.getName());

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BasicConfigurator.configure(); // configuring the Logger
        LOG.info("Settings attributes for CreateServlet.");

        try {
            request.setAttribute("companies", service.listAllCompanies());
        } catch (PersistenceException e) {
            throw new ServletException("Couldn't set session attributes", e);
        }

        request.getRequestDispatcher("addComputer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO: validation
        String computerName = request.getParameter("computerName");
        Date introduced = java.sql.Date.valueOf(request.getParameter("introduced"));
        Date discontinued = java.sql.Date.valueOf(request.getParameter("discontinued"));
        Integer companyID = Integer.valueOf(request.getParameter("companyID"));

        try {
            service.addComputer(computerName, introduced, discontinued, companyID);
        } catch (PersistenceException e) {
            throw new ServletException("Could not add the new computer!", e);
        } catch (IOException e) {
            throw new ServletException("Could not add the new computer!", e);
        }

        doGet(request, response);

    }
}