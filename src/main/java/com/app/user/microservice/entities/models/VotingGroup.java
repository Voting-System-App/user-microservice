package com.app.user.microservice.entities.models;

import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class VotingGroup {
    private String id;
    private String name;
    private String time;
    private Boolean isActive;
    private VotingDate votingDate;
}
