package com.excilys.cdb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Victor Gambier
 *
 *         Enables Spring Security's web security support and provide Spring MVC integration
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private HikariDataSource dataSource;

    @Autowired
    public WebSecurityConfig(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Defining queries to perform database authentication.
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery(
                        "select username, role from user_roles where username = ?");
    }

    /**
     * Defines the password hashing algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Defines which URL paths should be secured and which should not.
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
}
