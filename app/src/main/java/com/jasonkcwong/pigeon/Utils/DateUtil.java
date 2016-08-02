package com.jasonkcwong.pigeon.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jason on 16-08-02.
 */
public class DateUtil {
    public static String getCurrentTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return simpleDateFormat.format(now);
    }

    public static String getTodayDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return simpleDateFormat.format(now);
    }
}
