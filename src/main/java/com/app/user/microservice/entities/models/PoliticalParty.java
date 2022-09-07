package com.app.user.microservice.entities.models;

import lombok.Data;

@Data
public class PoliticalParty {
    private String id;
    private String description;
    private String date;
    private Boolean status;
}
