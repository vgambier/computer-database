package com.excilys.cdb.validator;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

/**
 * @author Victor Gambier
 *
 */
@Component("serviceValidatorBean")
public class ServiceValidator {

    private BindingValidator bindingValidator;
    private ComputerService computerService;
    private CompanyService companyService;

    @Autowired
    public ServiceValidator(BindingValidator bindingValidator, ComputerService computerService,
            CompanyService companyService) {
        this.bindingValidator = bindingValidator;
        this.computerService = computerService;
        this.companyService = companyService;
    }

    /**
     * Validates a ComputerDTO. Does several checks and for each failed test, adds an error message
     * to a list. If at least one date is invalid, or if at least one date is empty, the
     * "isDiscontinuedAfterIntroduced" will pass, and no error messages will be added. If the
     * returned list is empty, it can
     *
     * @param computerDTO
     * @return a collection (List) of error messages, possibly empty.
     */
    public List<String> validateComputerDTO(ComputerDTO computerDTO) {

        // Initializing
        List<String> errorMessages = new ArrayList<>();

        // ID check
        String idString = computerDTO.getId();
        // if the idString is null, the DTO is valid (useful for adding entries)
        if (idString != null && (!bindingValidator.isStringInteger(idString)
                || !computerService.doesComputerEntryExist(Integer.valueOf(idString)))) {
            errorMessages.add("Computer ID is invalid.");
        }

        // Computer name check
        String computerName = computerDTO.getName();
        if (computerName.equals("")) {
            errorMessages.add("Name is mandatory.");
        }

        // Dates check

        // Introduced check
        String introducedString = computerDTO.getIntroduced();
        Date introduced;
        if (introducedString.equals("")) {
            introduced = null;
        } else if (bindingValidator.isDateStringValid(introducedString)) {
            introduced = java.sql.Date.valueOf(introducedString);
        } else { // If invalid
            errorMessages.add("Introduced date field must be empty or match YYYY-MM-DD format.");
            introduced = null;
        }

        // Discontinued check
        String discontinuedString = computerDTO.getDiscontinued();
        Date discontinued;
        if (discontinuedString.equals("")) {
            discontinued = null;
        } else if (bindingValidator.isDateStringValid(discontinuedString)) {
            discontinued = java.sql.Date.valueOf(discontinuedString);
        } else { // If invalid
            errorMessages.add("Discontinued date field must be empty or match YYYY-MM-DD format.");
            discontinued = null;
        }

        // Continuity check
        if (introduced != null && discontinued != null && discontinued.before(introduced)) {
            errorMessages
                    .add("The date of discontinuation must be after the date of introduction.");
        }

        // Company ID check
        String companyIDString = computerDTO.getCompanyDTO().getId();

        // The input string is valid if a) it equals "0" OR if b) it's an integer that corresponds
        // to a database entry. It is invalid if neither condition is met.
        if (!(companyIDString.equals("0") || (bindingValidator.isStringInteger(companyIDString)
                && companyService.doesCompanyEntryExist(Integer.valueOf(companyIDString))))) {
            errorMessages.add("Company ID field must be empty (--) or a valid ID.");
        }

        return errorMessages;
    }

}
