package com.app.user.microservice.entities.models;

import com.app.user.microservice.entities.Voter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VotingDetail {
    private String id;
    private Voter voter;
    private List<Candidate> candidateList= new ArrayList<>();
    private Voting voting;
}
