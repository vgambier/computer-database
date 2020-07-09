package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import persistence.CompanyDAO;
import persistence.ComputerDAO;
import service.Service;

@Configuration
@ComponentScan({"persistence"})
public class AppConfiguration {

    @Bean(name = "serviceBean")
    public Service service(ComputerDAO computerDAO, CompanyDAO companyDAO) {
        return new Service(computerDAO, companyDAO);
    }
}