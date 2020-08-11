package com.excilys.cdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.excilys.cdb.exception.CustomGlobalExceptionHandler;

/**
 * @author Victor Gambier
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.excilys.cdb.config", "com.excilys.cdb.controller",
        "com.excilys.cdb.service", "com.excilys.cdb.exception"})
public class RESTConfiguration implements WebMvcConfigurer {

    @Bean
    public View jsonTemplate() {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setPrettyPrint(true);
        return view;
    }

    @Bean
    public ViewResolver viewResolver() {
        return new BeanNameViewResolver();
    }

    @Bean
    public CustomGlobalExceptionHandler responseEntityExceptionHandler() {
        return new CustomGlobalExceptionHandler();
    }


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
