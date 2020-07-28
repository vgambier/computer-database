package com.excilys.cdb.mapper;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.model.Company;

/* This class uses the Singleton pattern */

/**
 * @author Victor Gambier
 *
 */
public class CompanyDTOMapper {

    private static CompanyDTOMapper instance = null;

    private CompanyDTOMapper() {
    }

    public static CompanyDTOMapper getInstance() {
        if (instance == null) {
            instance = new CompanyDTOMapper();
        }
        return instance;
    }

    public Company fromDTOtoModel(CompanyDTO companyDTO) {
        return new Company(Integer.parseInt(companyDTO.getId()), companyDTO.getName());
    }
}
