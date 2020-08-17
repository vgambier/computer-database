package com.excilys.cdb.mapper;

import com.excilys.cdb.dto.UserDTO;
import com.excilys.cdb.dto.UserNoPaDTO;
import com.excilys.cdb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component("userDTOMapperBean")
public class UserDTOMapper {

    private final AuthorityDTOMapper authorityDTOMapper;

    @Autowired
    public UserDTOMapper (AuthorityDTOMapper authorityDTOMapper){
        this.authorityDTOMapper = authorityDTOMapper;
    }

    public UserDTO userToDto (User user){
        UserDTO result = new UserDTO();
        result.setUsername(user.getUsername());
        result.setPassword(user.getPassword());
        result.setEnabled(user.getEnabled());
        return result;
    }

    public UserNoPaDTO userToUserNoPaDTO (User user){
        UserNoPaDTO result = new UserNoPaDTO();
        result.setUsername(user.getUsername());
        result.setEnabled(user.getEnabled());
        result.setAuthorityList(user.getAuthoritySet().stream().map(authority -> authorityDTOMapper.authorityToDTO(authority).getAuthority()).collect(Collectors.toList()));
        return result;
    }
}
