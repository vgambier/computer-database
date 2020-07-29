package com.excilys.cdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.dao.CompanyDAO;
import com.excilys.cdb.dao.ComputerDAO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

/**
 * @author Victor Gambier
 *
 *         Configures the beans used for the service part of the application.
 */
@Configuration
@ComponentScan({"com.excilys.cdb.dao", "com.excilys.cdb.controller"})
public class ServiceBeansConfig {

    @Bean(name = "computerServiceBean")
    public ComputerService computerService(ComputerDAO computerDAO) {
        return new ComputerService(computerDAO);
    }

    @Bean(name = "companyServiceBean")
    public CompanyService companyService(CompanyDAO companyDAO) {
        return new CompanyService(companyDAO);
    }
}