package com.app.user.microservice.entities;

import jakarta.persistence.OneToMany;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "city")
@Data
public class City {
    @Id
    private String id;
    private String name;
    @OneToMany
    private State state;
}
