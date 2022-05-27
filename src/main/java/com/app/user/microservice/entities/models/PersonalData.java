package com.app.user.microservice.entities.models;

import lombok.Data;

@Data
public class PersonalData {
    private String nombres;
    private String ape_paterno;
    private String ape_materno;
    private String dni;
}
