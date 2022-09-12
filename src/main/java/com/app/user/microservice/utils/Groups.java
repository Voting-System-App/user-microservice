package com.app.user.microservice.utils;

import org.springframework.stereotype.Component;

@Component
public class Groups {
    public String assignGroup(String dni){
        if(dni.equals("1")){
            return "A";
        }
        if(dni.equals("2")){
            return "B";
        }
        if(dni.equals("3")){
            return "C";
        }
        if(dni.equals("4")){
            return "D";
        }
        if(dni.equals("5")){
            return "E";
        }
        if(dni.equals("6")){
            return "F";
        }
        if(dni.equals("7")){
            return "G";
        }
        if(dni.equals("8")){
            return "H";
        }
        if(dni.equals("9")){
            return "I";
        }
        else {
            return "J";
        }
    }
}
