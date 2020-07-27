package com.excilys.cdb.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.persistence.CompanyDAO;
import com.excilys.cdb.persistence.ComputerDAO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.Validator;

/**
 * @author Victor Gambier
 *
 *         Configures the beans used for general purposes.
 */
@Configuration
@ComponentScan({"com.excilys.cdb.persistence, com.excilys.cdb.validator", "com.excilys.cdb.mapper"})
public class AppConfiguration {

    @Bean(name = "computerServiceBean")
    public ComputerService computerService(ComputerDAO computerDAO) {
        return new ComputerService(computerDAO);
    }

    @Bean(name = "companyServiceBean")
    public CompanyService companyService(CompanyDAO companyDAO) {
        return new CompanyService(companyDAO);
    }

    @Bean(name = "validatorBean")
    public Validator validator() {
        return new Validator();
    }
}