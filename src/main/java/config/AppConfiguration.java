package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import persistence.CompanyDAO;
import persistence.ComputerDAO;
import service.Service;

@Configuration
public class AppConfiguration {

    @Bean(name = "computerDAOBean")
    public ComputerDAO computerDAO() {
        return new ComputerDAO();
    }

    @Bean(name = "companyDAOBean")
    public CompanyDAO companyDAO() {
        return new CompanyDAO();
    }

    @Bean(name = "serviceBean")
    public Service service() {
        return new Service(computerDAO(), companyDAO());
    }
}