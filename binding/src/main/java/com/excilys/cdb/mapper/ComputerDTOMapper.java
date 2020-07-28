package com.excilys.cdb.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Computer;

/**
 * @author Victor Gambier
 *
 */

@Component("computerDTOMapperBean")
@Deprecated // TODO: this doesn't work because of the Company type mismatch
public class ComputerDTOMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Computer fromDTOtoModel(ComputerDTO computerDTO) {

        // Fetching ID

        int computerID = computerDTO.getId() == null ? -1 : Integer.parseInt(computerDTO.getId());

        // Fetching corresponding Company object
        String companyIDString = computerDTO.getCompanyId();

        return new Computer(

                computerID,

                computerDTO.getName(),

                computerDTO.getIntroduced() == null
                        ? null
                        : LocalDate.parse(computerDTO.getIntroduced(), formatter),

                computerDTO.getDiscontinued() == null
                        ? null
                        : LocalDate.parse(computerDTO.getDiscontinued(), formatter),

                null);
    }
}
