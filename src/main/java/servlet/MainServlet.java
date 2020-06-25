package servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import persistence.ComputerDAO;
import persistence.PersistenceException;

@WebServlet(name = "MainServlet", urlPatterns = "/dashboard")
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = 4L;

    private static ComputerDAO computerDAO = ComputerDAO.getInstance();
    private static final Logger LOG = Logger.getLogger(MainServlet.class.getName());

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BasicConfigurator.configure(); // configuring the Logger
        LOG.info("Settings attributes for MainServlet.");

        try {
            request.setAttribute("computers", computerDAO.listAll());
            request.setAttribute("computerCount", computerDAO.countEntries());
        } catch (PersistenceException e) {
            throw new ServletException("Couldn't set session attributes", e);
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}