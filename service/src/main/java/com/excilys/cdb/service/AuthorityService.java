package com.excilys.cdb.service;

import com.excilys.cdb.dao.AuthorityDAO;
import com.excilys.cdb.dto.AuthorityDTO;
import com.excilys.cdb.mapper.AuthorityDTOMapper;
import com.excilys.cdb.model.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("authorityServiceBean")
public class AuthorityService {

    private final AuthorityDAO authorityDAO;
    private final AuthorityDTOMapper authorityDTOMapper;

    @Autowired
    public AuthorityService (AuthorityDAO authorityDAO, AuthorityDTOMapper authorityDTOMapper){
        this.authorityDAO = authorityDAO;
        this.authorityDTOMapper = authorityDTOMapper;
    }

    public List<AuthorityDTO> listAll() {
        List <Authority> temp = authorityDAO.listAll();
        return temp.stream().map(authorityDTOMapper::authorityToDTO).collect(Collectors.toList());
    }

}
