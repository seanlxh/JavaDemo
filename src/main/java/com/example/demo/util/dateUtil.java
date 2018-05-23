package com.example.demo.util;

import java.util.Calendar;
import java.util.Date;

public class dateUtil {
    public static Date getStartTimeofDay(Date date){
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.HOUR_OF_DAY,0);
        day.set(Calendar.MINUTE,0);
        day.set(Calendar.SECOND,0);
        day.set(Calendar.MILLISECOND,0);
        return day.getTime();
    }

    public static Date subDay(Date date,int num){
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.DATE, day.get(Calendar.DATE) - num);
        Date endDate = day.getTime();
        return endDate;
    }
}
