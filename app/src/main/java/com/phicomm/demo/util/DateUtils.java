package com.phicomm.demo.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hzn on 17-6-6.
 */

public class DateUtils {
    public static long date2seconds(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis() / 1000;
    }

    /*
    public static void main(String[] args) {
        System.out.println(date2seconds(new Date()));
    }
    */
}
