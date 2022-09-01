package com.app.user.microservice.entities.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class VotingDetail {
    @Id
    private String id;
    private List<String> candidateId= new ArrayList<>();
    private Voting voting;
}
