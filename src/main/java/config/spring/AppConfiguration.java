package config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import persistence.CompanyDAO;
import persistence.ComputerDAO;
import service.Service;
import validator.Validator;

/**
 * @author Victor Gambier
 *
 */
@Configuration
@ComponentScan({"persistence, validator"})
public class AppConfiguration {

    @Bean(name = "serviceBean")
    public Service service(ComputerDAO computerDAO, CompanyDAO companyDAO, Validator validator) {
        return new Service(computerDAO, companyDAO, validator);
    }
}