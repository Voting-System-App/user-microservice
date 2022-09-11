package com.app.user.microservice.utils;

import org.springframework.stereotype.Component;

@Component
public class Groups {
    public String assignGroup(String dni){
        String group = switch (dni) {
            case "1" -> "A";
            case "2" -> "B";
            case "3" -> "C";
            case "4" -> "D";
            case "5" -> "E";
            case "6" -> "F";
            case "7" -> "G";
            case "8" -> "H";
            case "9" -> "I";
            default -> "J";
        };
        return group;
    }
}
