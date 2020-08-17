package com.excilys.cdb.model;


import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String user;

    @Column(name = "authority")
    private String authority;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority){
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority = (Authority) o;

        if (user != null ? !user.equals(authority.user) : authority.user != null) return false;
        if (authority != null ? !authority.equals(authority.authority) : authority.authority != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        return result;
    }
}
