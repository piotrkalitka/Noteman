package com.piotrkalitka.noteman.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piotrkalitka.noteman.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private Long id;
    private String name;
    private String surname;
    private String username;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(user.getId(), user.getName(), user.getSurname(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    public UserPrincipal(Long id, String name, String surname, String username, String email, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User getUser() {
        return new User(email, name, surname, username, password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}