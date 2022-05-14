package com.devpro.airj18bookingapp.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeAgo {



    public String covertTimeToText(String dataDate) {

        String convertTime = null;
        String suffix = "trước";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                if (second == 1) {
                    convertTime = second + " giây " + suffix;
                } else {
                    convertTime = second + " giây " + suffix;
                }
            } else if (minute < 60) {
                if (minute == 1) {
                    convertTime = minute + " phút " + suffix;
                } else {
                    convertTime = minute + " phút " + suffix;
                }
            } else if (hour < 24) {
                if (hour == 1) {
                    convertTime = " giờ " + suffix;
                } else {
                    convertTime = hour + " giờ " + suffix;
                }
            } else if (day >= 7) {
                if (day >= 365) {
                    long tempYear = day / 365;
                    if (tempYear == 1) {
                        convertTime = " năm " + suffix;
                    } else {
                        convertTime = tempYear + " năm " + suffix;
                    }
                } else if (day >= 30) {
                    long tempMonth = day / 30;
                    if (tempMonth == 1) {
                        convertTime = (day / 30) + " tháng " + suffix;
                    } else {
                        convertTime = (day / 30) + " tháng " + suffix;
                    }
                } else {
                    long tempWeek = day / 7;
                    if (tempWeek == 1) {
                        convertTime = (day / 7) + " tuần " + suffix;
                    } else {
                        convertTime = (day / 7) + " tuần " + suffix;
                    }
                }
            } else {
                if (day == 1) {
                    convertTime = day + " ngày " + suffix;
                } else {
                    convertTime = day + " ngày " + suffix;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("TimeAgo", e.getMessage() + "");
        }
        return convertTime;
    }

}
