package com.excilys.cdb.mapper;

import com.excilys.cdb.dto.UserDTO;
import com.excilys.cdb.model.User;
import org.springframework.stereotype.Component;


@Component("userDTOMapperBean")
public class UserDTOMapper {
    public UserDTO userToDto (User user){
        UserDTO result = new UserDTO();
        result.setUsername(user.getUsername());
        result.setPassword(user.getPassword());
        result.setEnabled(user.getEnabled());
        return result;
    }
}
