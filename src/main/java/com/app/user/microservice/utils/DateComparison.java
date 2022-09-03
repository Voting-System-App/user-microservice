package com.app.user.microservice.utils;


import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DateComparison {

    public String compareHours(LocalTime time){
        int flag = 0;
        List<String> group = Arrays.asList("A","B","C","D","E","F","G","H","I","J");
        List<LocalTime> rangeStart = Arrays.asList(LocalTime.parse("07:00:00"),LocalTime.parse("08:00:00"),LocalTime.parse("09:00:00")
        ,LocalTime.parse("10:00:00"),LocalTime.parse("11:00:00"),LocalTime.parse("12:00:00"),LocalTime.parse("13:00:00"),
                LocalTime.parse("14:00:00"),LocalTime.parse("15:00:00"),LocalTime.parse("16:00:00"));
        List<LocalTime> rangeEnd = Arrays.asList(LocalTime.parse("08:00:00"),LocalTime.parse("09:00:00")
                ,LocalTime.parse("10:00:00"),LocalTime.parse("11:00:00"),LocalTime.parse("12:00:00"),LocalTime.parse("13:00:00"),
                LocalTime.parse("14:00:00"),LocalTime.parse("15:00:00"),LocalTime.parse("16:00:00"),LocalTime.parse("17:00:00"));
        for (int i = 0; i < 10; i++) {
            if (!time.isBefore(rangeStart.get(i)) && time.isBefore(rangeEnd.get(i))) {
                flag = i;
                break;
            }
        }
        return group.get(flag);
    }
}
