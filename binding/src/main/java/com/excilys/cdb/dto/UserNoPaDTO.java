package com.excilys.cdb.dto;

import java.util.List;

public class UserNoPaDTO {

    private String username;
    private String enabled;
    private List<String> authorityList;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public List<String> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<String> authorityList) {
        this.authorityList = authorityList;
    }
}
