package com.app.user.microservice.entities;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Document(collection = "voter")
@Data
public class Voter {
    @Id
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Field(name = "last_name")
    private String lastName;
    @Email
    private String email;
    @Size(min = 8,max =8)
    private String dni;
    private String gender;
    private String birthDate;
    @Field(name = "is_active")
    @Enumerated(EnumType.STRING)
    private Status isActive;
    @Field(name = "finger_print")
    private String fingerPrint;
}
