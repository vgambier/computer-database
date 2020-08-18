package com.excilys.cdb.service;

import com.excilys.cdb.dao.UserDAO;
import com.excilys.cdb.dto.AddUserDTO;
import com.excilys.cdb.dto.UserNoPaDTO;
import com.excilys.cdb.mapper.UserDTOMapper;
import com.excilys.cdb.model.Authority;
import com.excilys.cdb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userServiceBean")
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;
    private final UserDTOMapper userDTOMapper;

    @Autowired
    public UserService(UserDAO userDAO, UserDTOMapper userDTOMapper) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getByUserName(username);
        if (user == null ) {
            throw new UsernameNotFoundException("User \"" + username +"\" not found");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getAuthoritySet().stream().map(Authority::toString).toArray(String[]::new))
                .build();
    }


    public List <UserNoPaDTO> listAll (){
        List<User> userList = userDAO.listAll();
        return userList.stream().map(userDTOMapper::userToUserNoPaDTO).collect(Collectors.toList());
    }

    public void add (AddUserDTO addUserDTO){
        userDAO.add(addUserDTO.getUsername(),addUserDTO.getPassword());
    }



}
