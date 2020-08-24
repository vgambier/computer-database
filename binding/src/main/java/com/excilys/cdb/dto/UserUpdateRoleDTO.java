package com.excilys.cdb.dto;

public class UserUpdateRoleDTO {
    String username;
    String[] authorityList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(String[] authorityList) {
        this.authorityList = authorityList;
    }
}
