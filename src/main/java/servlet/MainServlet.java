package servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import persistence.ComputerDAO;

@WebServlet(name = "MainServlet", urlPatterns = "/dashboard")
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = 4L;

    ComputerDAO computerDAO;
    private static Logger log = Logger.getLogger(MainServlet.class.getName());

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Getting computers");
        /**
         * try { request.setAttribute("computers", computerDAO.listAll()); } catch
         * (PersistenceException e) { throw new ServletException("Couldn't set session attributes",
         * e); }
         */
        request.setAttribute("helloWorldString", "Hello World!");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}