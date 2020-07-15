package config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import persistence.CompanyDAO;
import persistence.ComputerDAO;
import service.CompanyService;
import service.ComputerService;
import validator.Validator;

/**
 * @author Victor Gambier
 *
 *         Configures the beans used for general purposes.
 */
@Configuration
@ComponentScan({"persistence, validator"}) // TODO: add servlet package
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