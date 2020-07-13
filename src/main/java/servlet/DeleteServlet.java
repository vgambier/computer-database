package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.AppConfiguration;
import config.JdbcConfiguration;
import service.Service;

@WebServlet(name = "DeleteServlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 0xD31373L;

    private static Service service = (Service) new AnnotationConfigApplicationContext(
            AppConfiguration.class, JdbcConfiguration.class).getBean("serviceBean");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/dashboard"); // TODO + getParameter
                                                                        // ?orderBy
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Gather the list of entries to delete

        String[] selection = request.getParameter("selection").split(",");

        // Delete the entries
        for (String computerIDString : selection) {

            try {
                if (service.getValidator().isStringInteger(computerIDString)
                        && service.doesComputerEntryExist(Integer.valueOf(computerIDString))) {

                    int computerID = Integer.valueOf(computerIDString);
                    service.deleteComputer(computerID);
                }
            } catch (NumberFormatException e) {
                throw new ServletException("Couldn't delete computer", e);
            }
        }

        // Go back to main page
        doGet(request, response);

    }
}