package com.app.user.microservice.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Document(collection = "role")
@Data
public class Role implements Serializable {

    @Id
    private String id;
    @Indexed(unique=true, sparse=true)
    private String name;
    @Serial
    private static final long serialVersionUID = -5487746865554917595L;
}
