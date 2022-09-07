package com.app.user.microservice.entities;


import com.app.user.microservice.entities.models.VotingGroup;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne
    private City city;
    private List<String> groupList = new ArrayList<>();
}
