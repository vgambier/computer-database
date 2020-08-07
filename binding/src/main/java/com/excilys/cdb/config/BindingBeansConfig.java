package com.excilys.cdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.validator.BindingValidator;

/**
 * @author Victor Gambier
 *
 *         Configures the beans used for the service part of the application.
 */
@Configuration
public class BindingBeansConfig {

    @Bean(name = "validatorBean")
    public BindingValidator validator() {
        return new BindingValidator();
    }
}