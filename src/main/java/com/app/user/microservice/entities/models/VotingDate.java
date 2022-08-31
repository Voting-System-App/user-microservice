package com.app.user.microservice.entities.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class VotingDate {
    private String id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;
}
