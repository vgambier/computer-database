package com.excilys.cdb.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private String enabled;


    @JoinTable(name = "authority",
            joinColumns = {
                @JoinColumn(name = "users_id")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "authorities_id")
            })
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Authority> authorityList = new HashSet<Authority>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Set<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(Set<Authority> authorityList) {
        this.authorityList = authorityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (enabled != null ? !enabled.equals(user.enabled) : user.enabled != null) return false;
        return authorityList != null ? authorityList.equals(user.authorityList) : user.authorityList == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        result = 31 * result + (authorityList != null ? authorityList.hashCode() : 0);
        return result;
    }
}
