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

import service.Service;

@WebServlet(name = "EditServlet", urlPatterns = "/editComputer")
public class EditServlet extends HttpServlet {

    private static final long serialVersionUID = 6L;

    private static Service service = Service.getInstance();
    private static final Logger LOG = Logger.getLogger(EditServlet.class.getName());

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BasicConfigurator.configure(); // configuring the Logger
        LOG.info("Settings attributes for EditServlet.");

        request.getRequestDispatcher("editComputer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);

    }
}