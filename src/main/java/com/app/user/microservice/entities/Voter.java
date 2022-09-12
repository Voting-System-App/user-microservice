package com.app.user.microservice.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

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
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date emissionDate;
    @Field(name = "is_active")
    @Enumerated(EnumType.STRING)
    private Status isActive;
    @Field(name = "finger_print")
    private String fingerPrint;
    private String city;
    private String group;
}
