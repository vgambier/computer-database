package com.excilys.cdb.controller;

import com.excilys.cdb.config.jwt.JwtTokenUtil;
import com.excilys.cdb.config.jwt.dto.JwtResponse;
import com.excilys.cdb.dto.*;
import com.excilys.cdb.service.AuthorityService;
import com.excilys.cdb.service.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthorityService authorityService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UsersController(PasswordEncoder passwordEncoder,
                           UserService userService,
                           AuthorityService authorityService,
                           JwtTokenUtil jwtTokenUtil){
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authorityService = authorityService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping
    public List<UserNoPaDTO> getUsers (){
        return userService.listAll();
    }

    @GetMapping("/{username}")
    public UserNoPaDTO getUser (@PathVariable String username){
        return userService.getUser(username);
    }

    @GetMapping("/authorities")
    public List<AuthorityDTO> getAuthorities (){
        return  authorityService.listAll();
    }

    @PostMapping("/userFromToken")
    public UserNoPaDTO getUserFomToken (@RequestBody TokenDTO jwtToken){
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken.getJwtToken());
        return getUser(username);
    }

    @PostMapping("/add")
    public void addUser (@RequestBody AddUserDTO addUserDTO) {
        try{
            userService.add(addUserDTO);
        }catch (ConstraintViolationException exception){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"username already exist",exception);
        }

    }

    @PostMapping("/enable/{userName}")
    public void enableUser (@PathVariable String userName){
        userService.enable(userName);
    }

    @PostMapping("/disable/{userName}")
    public void disableUser (@PathVariable String userName){
        userService.disable((userName));
    }

    @PutMapping()
    public void manageRole (@RequestBody UserUpdateRoleDTO userUpdateRoleDTO){
        userService.manageRole(userUpdateRoleDTO.getUsername(), userUpdateRoleDTO.getAuthorities());
    }

    @DeleteMapping("/{username}")
    public void deleteUser (@PathVariable String username){
        userService.deleteUser(username);
    }

}
