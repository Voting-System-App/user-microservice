package com.app.user.microservice.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "state")
@Data
public class State {
    @Id
    private String id;
    private String name;
}
