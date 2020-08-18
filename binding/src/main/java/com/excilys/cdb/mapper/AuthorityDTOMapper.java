package com.excilys.cdb.mapper;

import com.excilys.cdb.dto.AuthorityDTO;
import com.excilys.cdb.model.Authority;
import org.springframework.stereotype.Component;

@Component("authorityDTOMapper")
public class AuthorityDTOMapper {
    public AuthorityDTO authorityToDTO (Authority authority){
        AuthorityDTO result = new AuthorityDTO();
        result.setAuthority(authority.getAuthority());
        return result;
    }
}
