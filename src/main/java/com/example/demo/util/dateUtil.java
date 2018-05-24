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

    public static Date getStartMonthofDay(Date date){
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.HOUR_OF_DAY,0);
        day.set(Calendar.DATE,1);
        day.set(Calendar.MINUTE,0);
        day.set(Calendar.SECOND,0);
        day.set(Calendar.MILLISECOND,0);
        return day.getTime();
    }

    public static Date subMonth(Date date,int num){
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        int target = day.get(Calendar.MONTH) - num >= 0 ? day.get(Calendar.MONTH) - num : 12 + day.get(Calendar.MONTH) - num;
        day.set(Calendar.MONTH, target);
        Date endDate = day.getTime();
        return endDate;
    }

    public static Date subDay(Date date,int num){
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.DATE, day.get(Calendar.DATE) - num);
        Date endDate = day.getTime();
        return endDate;
    }
}
