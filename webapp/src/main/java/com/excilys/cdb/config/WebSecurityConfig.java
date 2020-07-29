package com.excilys.cdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Victor Gambier
 *
 *         Enables Spring Security's web security support and provide Spring MVC integration
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Defines which URL paths should be secured and which should not. Automatically creates a
     * controller for the login view.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/dashboard").hasAnyRole("ADMIN", "USER")

                .and().authorizeRequests().antMatchers("/addComputer").hasAnyRole("ADMIN")

                .and().authorizeRequests().antMatchers("/editComputer").hasAnyRole("ADMIN")

                .and().formLogin().permitAll()
                // Note: if .loginPage() isn't called after .formLogin(), a login page is generated
                // by default

                .and().logout().permitAll()

                .and().csrf().disable(); // Needed so that POST forms don't throw a 403 error
    }

    /**
     * Sets up an in-memory user store with a single user.
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password")
                .roles("USER").build();
        UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("password")
                .roles("ADMIN").build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
