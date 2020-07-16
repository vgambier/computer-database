package com.excilys.cdb.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

/**
 * @author Victor Gambier
 *
 */
@Controller
@WebServlet(name = "DeleteServlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 0xD31373L;

    private ComputerService computerService;
    private Validator validator;

    @Autowired
    public DeleteServlet(ComputerService computerService, Validator validator) {
        this.computerService = computerService;
        this.validator = validator;
    }

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
                if (validator.isStringInteger(computerIDString) && computerService
                        .doesComputerEntryExist(Integer.valueOf(computerIDString))) {

                    int computerID = Integer.valueOf(computerIDString);
                    computerService.deleteComputer(computerID);
                }
            } catch (NumberFormatException e) {
                throw new ServletException("Couldn't delete computer", e);
            }
        }

        // Go back to main page
        doGet(request, response);

    }
}