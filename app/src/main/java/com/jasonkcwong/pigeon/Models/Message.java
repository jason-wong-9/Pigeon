package com.jasonkcwong.pigeon.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jason on 16-07-30.
 */
public class Message {
    public String senderId;
    public String receiverId;
    public String content;
    public String date;

    public Message(){

    }
    public Message(String senderId, String receiverId, String content){
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.date = getCurrentTimeStamp();
    }

    private String getCurrentTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return simpleDateFormat.format(now);
    }
}
