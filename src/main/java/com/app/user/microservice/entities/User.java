package com.app.user.microservice.entities;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Document(collection = "user")
@Data
public class User implements Serializable {

    @Id
    private String id;
    @NotEmpty
    @Indexed(unique=true, sparse=true)
    private String username;
    @NotEmpty
    private String password;
    private String enabled;
    @Email
    private String email;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> roles;
    @Serial
    private static final long serialVersionUID = 4110570539416850727L;
}
