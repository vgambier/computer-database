package com.excilys.cdb.controller;

import com.excilys.cdb.dto.AddUserDTO;
import com.excilys.cdb.dto.AuthorityDTO;
import com.excilys.cdb.dto.UserNoPaDTO;
import com.excilys.cdb.dto.UserUpdateRoleDTO;
import com.excilys.cdb.service.AuthorityService;
import com.excilys.cdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @GetMapping("/{username}")
    public UserNoPaDTO getUser (@PathVariable String username){
        return userService.getUser(username);
    }

    @GetMapping("/authorities")
    public List<AuthorityDTO> getAuthorities (){
        return  authorityService.listAll();
    }

    @PostMapping("/add")
    public void addUser (@RequestBody AddUserDTO addUserDTO) {
        userService.add(addUserDTO);
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
        userService.manageRole(userUpdateRoleDTO.getUserName(), userUpdateRoleDTO.getRoles());
    }

    @DeleteMapping("/{username}")
    public void deleteUser (@PathVariable String username){
        userService.deleteUser(username);
    }
}
