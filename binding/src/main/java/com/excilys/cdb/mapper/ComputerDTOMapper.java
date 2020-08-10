package com.excilys.cdb.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Computer;

/**
 * @author Victor Gambier
 *
 */

@Component("computerDTOMapperBean")
public class ComputerDTOMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private CompanyDTOMapper companyDTOMapper;

    @Autowired
    public ComputerDTOMapper(CompanyDTOMapper companyDTOMapper) {
        this.companyDTOMapper = companyDTOMapper;
    }

    public Computer fromDTOtoModel(ComputerDTO computerDTO) {

        return new Computer(

                computerDTO.getId() == null ? 0 : Integer.parseInt(computerDTO.getId()),

                computerDTO.getName(),

                computerDTO.getIntroduced() == null
                        ? null
                        : LocalDate.parse(computerDTO.getIntroduced(), formatter),

                computerDTO.getDiscontinued() == null
                        ? null
                        : LocalDate.parse(computerDTO.getDiscontinued(), formatter),

                companyDTOMapper.fromDTOtoModel(computerDTO.getCompany()));
    }
}
