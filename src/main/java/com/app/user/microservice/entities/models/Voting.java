package com.app.user.microservice.entities.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;

@Data
public class Voting {
    private String id;
    private String description;
    @Enumerated(EnumType.STRING)
    private VotingStatus votingStatus;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;
    private Boolean isActive;
}
