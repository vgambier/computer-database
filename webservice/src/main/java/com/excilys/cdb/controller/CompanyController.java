package com.excilys.cdb.controller;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.CompanyDTOMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.validator.BindingValidator;
import com.excilys.cdb.exception.CompanyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("/api/companies")
@RestController
public class CompanyController {

    private CompanyService companyService;
    private BindingValidator validator;
    private CompanyDTOMapper companyDTOMapper;

    @Autowired
    public CompanyController(CompanyService companyService,  BindingValidator validator, CompanyDTOMapper companyDTOMapper
                             ) {
        this.companyService = companyService;
        this.validator = validator;
        this.companyDTOMapper=companyDTOMapper;
    }


    @GetMapping("/{stringID}")
    public CompanyDTO getCompanyJSON(@PathVariable String stringID) throws CompanyNotFoundException {

        if (validator.isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            if (companyService.doesCompanyEntryExist(id)) {
                return companyService.getCompany(id);

            } else {
                throw new CompanyNotFoundException();
            }
        }
        else {
            throw new CompanyNotFoundException();
        }
    }

    @GetMapping()
    public List<CompanyDTO> getCompanies() {

       return companyService.listAllCompanies();
        }

    @GetMapping("/count")
    public int countCompanies() {

        return companyService.countCompanies();  //TODO: ?????????????????
    }


    @DeleteMapping("/{stringID}")
    public void deleteCompany(@PathVariable String stringID) throws CompanyNotFoundException {

        boolean found = false;

        if (validator.isStringInteger(stringID)) {
            int id = Integer.valueOf(stringID);
            if (companyService.doesCompanyEntryExist(id)) {
                Company deletedCompany = companyDTOMapper.fromDTOtoModel(companyService.getCompany(id));
                companyService.deleteCompany(deletedCompany);
                found = true;
            }
        }
        if (!found) {
            throw new CompanyNotFoundException();
        }
    }

}
