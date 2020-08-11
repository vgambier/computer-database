package com.excilys.cdb.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Computer;

/**
 * @author Victor Gambier
 *
 */
@ComponentScan({"com.excilys.cdb.mapper"})
@Component("computerDTOMapperBean")
public class ComputerDTOMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private CompanyDTOMapper companyDTOMapper;
    private static Logger logger = LoggerFactory.getLogger(ComputerDTOMapper.class);

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

    public ComputerDTO fromModeltoDTO(Computer computer) {
        return new ComputerDTO.Builder().withName(computer.getCompanyName()).withId(String.valueOf(computer.getId()))
                .withIntroduced(localToString(computer.getIntroduced()))
                .withDiscontinued(localToString((computer.getDiscontinued())))
                .withCompany(companyDTOMapper.fromModeltoDTO(computer.getCompany())).build();
    }

    public static String localToString(LocalDate date) {
        String string = null;
        if (date != null) {
            try {
                string = date.format(formatter);
            } catch (DateTimeParseException e) {
                logger.debug("Error formating your LocalDate");
            }
        }
        return string;
    }
}
