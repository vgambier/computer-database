package com.excilys.cdb.controller;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.UserDTO;
import com.excilys.cdb.exception.CompanyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/addUser")
    public void createUser (  @RequestBody UserDTO userDTO ) {
        jdbcUserDetailsManager.createUser(User.builder().username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword())).disabled(true).authorities("ROLE_USER").build());

    }

    @PostMapping("/enableUser/{userName}")
    public void enableUser (@PathVariable String userName){
        jdbcUserDetailsManager.updateUser(User.withUserDetails(jdbcUserDetailsManager.loadUserByUsername(userName)).disabled(false).build());
    }

    @PostMapping("/disableUser/{userName}")
    public void disableUser (@PathVariable String userName){
        jdbcUserDetailsManager.updateUser(User.withUserDetails(jdbcUserDetailsManager.loadUserByUsername(userName)).disabled(true).build());
    }

}
