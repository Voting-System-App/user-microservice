package com.app.user.microservice.entities;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "user")
@Data
public class User implements UserDetails{

    @Id
    private String id;
    @NotEmpty
    @Indexed(unique=true, sparse=true)
    private String username;
    @NotEmpty
    private String password;
    private Boolean enabled;
    @Email
    private String email;
    private List<Role> roles;
    @Serial
    private static final long serialVersionUID = 4110570539416850727L;
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
    }
}
