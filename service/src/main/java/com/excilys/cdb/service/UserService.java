package com.excilys.cdb.service;

import com.excilys.cdb.dao.UserDAO;
import com.excilys.cdb.dto.UserDTO;
import com.excilys.cdb.dto.UserNoPaDTO;
import com.excilys.cdb.mapper.UserDTOMapper;
import com.excilys.cdb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userServiceBean")
public class UserService {

    private UserDAO userDAO;
    private UserDTOMapper userDTOMapper;

    @Autowired
    public UserService(UserDAO userDAO, UserDTOMapper userDTOMapper) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;
    }

    public List<UserDTO> listAll() {
        List <User> temp = userDAO.listAll();
        List <UserDTO> userDTOList = temp.stream().map(user -> userDTOMapper.userToDto(user)).collect(Collectors.toList());
        return userDTOList;
    }

    public List <UserNoPaDTO> listAll2 (){
        List<User> userList = userDAO.listAll();
        User u1 = userList.get(0);
        User u2 = userList.get(1);
        List<UserNoPaDTO> userNoPaDTOList = userList.stream().map(user -> userDTOMapper.userToUserNoPaDTO(user)).collect(Collectors.toList());
        return userNoPaDTOList;
    }


}
