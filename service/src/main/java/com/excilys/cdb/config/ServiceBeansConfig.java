package com.excilys.cdb.config;

import com.excilys.cdb.mapper.CompanyDTOMapper;
import com.excilys.cdb.mapper.ComputerDTOMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.dao.CompanyDAO;
import com.excilys.cdb.dao.ComputerDAO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.BindingValidator;
import com.excilys.cdb.validator.ServiceValidator;

/**
 * @author Victor Gambier
 *
 *         Configures the beans used for the service part of the application.
 */
@Configuration
@ComponentScan({"com.excilys.cdb.dao","com.excilys.cdb.mapper"})
public class ServiceBeansConfig {


    @Bean(name = "computerServiceBean")
    public ComputerService computerService(ComputerDAO computerDAO, ComputerDTOMapper computerDTOMapper) {
        return new ComputerService(computerDAO, computerDTOMapper);
    }

    @Bean(name = "companyServiceBean")
    public CompanyService companyService(CompanyDAO companyDAO, CompanyDTOMapper companyDTOMapper) {
        return new CompanyService(companyDAO,companyDTOMapper);
    }

    @Bean(name = "serviceValidatorBean")
    public ServiceValidator serviceValidator(BindingValidator bindingValidator,
            ComputerService computerService, CompanyService companyService) {
        return new ServiceValidator(bindingValidator, computerService, companyService);
    }
}
