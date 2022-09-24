package com.app.user.microservice.utils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateComparison {
    public Date minusDays(Date date){
        TimeZone tz = TimeZone.getTimeZone("America/Lima");
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTime(date);
        calendar.add(Calendar.DATE,-1);
        return calendar.getTime();
    }
    public Date equal(Date date){
        TimeZone tz = TimeZone.getTimeZone("America/Lima");
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTime(date);
        return calendar.getTime();
    }
    public Date plusDays(Date date){
        TimeZone tz = TimeZone.getTimeZone("America/Lima");
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        return calendar.getTime();
    }
}