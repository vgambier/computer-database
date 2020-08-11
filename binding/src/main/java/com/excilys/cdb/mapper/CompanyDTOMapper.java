package com.excilys.cdb.mapper;

import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.model.Company;

/**
 * @author Victor Gambier
 *
 */
@Component("companyDTOMapperBean")
public class CompanyDTOMapper {

    public CompanyDTO fromModeltoDTO(Company company) {
        return new CompanyDTO.Builder().withId(String.valueOf(company.getId())).withName(company.getName()).build();
    }

    public Company fromDTOtoModel(CompanyDTO companyDTO) {
        return new Company(Integer.parseInt(companyDTO.getId()), companyDTO.getName());
    }
}
