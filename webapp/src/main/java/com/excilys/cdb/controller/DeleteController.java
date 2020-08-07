package com.excilys.cdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.BindingValidator;

/**
 * @author Victor Gambier
 *
 */
@Controller
@RequestMapping("/delete")
public class DeleteController {

    private ComputerService computerService;
    private BindingValidator validator;

    @Autowired
    public DeleteController(ComputerService computerService, BindingValidator validator) {
        this.computerService = computerService;
        this.validator = validator;
    }

    @PostMapping
    protected String deleteComputer(@RequestParam("selection") String selection) {

        // Gather the list of entries to delete
        String[] entries = selection.split(",");

        // Delete the entries
        for (String computerIDString : entries) {

            if (validator.isStringInteger(computerIDString)
                    && computerService.doesComputerEntryExist(Integer.valueOf(computerIDString))) {

                int computerID = Integer.valueOf(computerIDString);
                computerService.deleteComputer(computerService.getComputer(computerID));
            }
        }

        // Go back to main page
        return "redirect:/";

    }
}