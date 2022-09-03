package com.app.user.microservice.entities.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class Voting {
    private String id;
    private String description;
    @Enumerated(EnumType.STRING)
    private VotingStatus votingStatus;
    private VotingDate votingDate;
    private Boolean isActive;
}
