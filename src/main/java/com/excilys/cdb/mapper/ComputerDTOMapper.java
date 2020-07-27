package com.excilys.cdb.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;

/**
 * @author Victor Gambier
 *
 */

@Component("computerDTOMapperBean")
public class ComputerDTOMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private CompanyService companyService;

    @Autowired
    public ComputerDTOMapper(CompanyService companyService) {
        this.companyService = companyService;
    }

    public Computer fromDTOtoModel(ComputerDTO computerDTO) {

        // Fetching corresponding Company object
        String companyIDString = computerDTO.getCompanyId();

        Company company = companyIDString.equals("")
                ? null
                : companyService.getCompany(Integer.valueOf(companyIDString));

        return new Computer(

                Integer.parseInt(computerDTO.getId()),

                computerDTO.getName(),

                computerDTO.getIntroduced() == null
                        ? null
                        : LocalDate.parse(computerDTO.getIntroduced(), formatter),

                computerDTO.getDiscontinued() == null
                        ? null
                        : LocalDate.parse(computerDTO.getDiscontinued(), formatter),

                company);

    }
}
