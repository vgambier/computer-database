package com.excilys.cdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.dao.CompanyDAO;
import com.excilys.cdb.dao.ComputerDAO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

/**
 * @author Victor Gambier
 *
 *         Configures the beans used for general purposes.
 */
@Configuration
@ComponentScan({"com.excilys.cdb.dao, com.excilys.cdb.validator"})
public class AppConfiguration {

    @Bean(name = "computerServiceBean")
    public ComputerService computerService(ComputerDAO computerDAO) {
        return new ComputerService(computerDAO);
    }

    @Bean(name = "companyServiceBean")
    public CompanyService companyService(CompanyDAO companyDAO) {
        return new CompanyService(companyDAO);
    }

    // TODO: move this bean

    @Bean(name = "validatorBean")
    public Validator validator() {
        return new Validator();
    }
}