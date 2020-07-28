package com.excilys.cdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.validator.Validator;

/**
 * @author Victor Gambier
 *
 *         Configures the beans used for the service part of the application.
 */
@Configuration
public class BindingBeansConfig {

    @Bean(name = "validatorBean")
    public Validator validator() {
        return new Validator();
    }
}