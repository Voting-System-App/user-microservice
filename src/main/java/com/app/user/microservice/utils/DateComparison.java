package com.app.user.microservice.utils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class DateComparison {
    public Date minusDays(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,-1);
        return calendar.getTime();
    }
    public Date plusDays(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        return calendar.getTime();
    }
}