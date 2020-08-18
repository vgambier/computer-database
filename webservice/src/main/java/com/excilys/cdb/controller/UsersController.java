package com.excilys.cdb.controller;

import com.excilys.cdb.dto.*;
import com.excilys.cdb.service.AuthorityService;
import com.excilys.cdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthorityService authorityService;

    @Autowired
    public UsersController (PasswordEncoder passwordEncoder,
                            UserService userService,
                            AuthorityService authorityService){
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authorityService = authorityService;
    }

    @GetMapping
    public List<UserNoPaDTO> getUsers (){
        return userService.listAll();
    }

     @GetMapping("/authorities")
    public List<AuthorityDTO> getAuthorities (){
        return  authorityService.listAll();
    }

    @PostMapping("/addUser")
    public void addUser (@RequestBody AddUserDTO addUserDTO) {
        userService.add(addUserDTO);
    }

//    @PostMapping("/enableUser/{userName}")
//    public void enableUser (@PathVariable String userName){
//        jdbcUserDetailsManager.updateUser(User.withUserDetails(jdbcUserDetailsManager.loadUserByUsername(userName)).disabled(false).build());
//    }

//    @PostMapping("/disableUser/{userName}")
//    public void disableUser (@PathVariable String userName){
//        jdbcUserDetailsManager.updateUser(User.withUserDetails(jdbcUserDetailsManager.loadUserByUsername(userName)).disabled(true).build());
//    }

//    @PutMapping()
//    public void manageRole (@RequestBody UserUpdateRoleDTO userUpdateRoleDTO){
//        jdbcUserDetailsManager.updateUser(User.withUserDetails(jdbcUserDetailsManager.loadUserByUsername(userUpdateRoleDTO.getUserName())).authorities(userUpdateRoleDTO.getRoles()).build());
//    }

}
