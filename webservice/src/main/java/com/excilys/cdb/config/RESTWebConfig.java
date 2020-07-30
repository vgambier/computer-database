package com.excilys.cdb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Victor Gambier
 *
 *         Initializes Spring MVC
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.excilys.cdb.config", "com.excilys.cdb.controller",
        "com.excilys.cdb.service", "com.excilys.cdb.exception"})
public class RESTWebConfig implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}