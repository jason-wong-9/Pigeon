package com.jasonkcwong.pigeon.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jason on 16-07-30.
 */
public class Message {
    public User sender;
    public User receiver;
    public String content;
    public String date;

    public Message(){

    }
    public Message(User sender, User receiver, String content){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = getCurrentTimeStamp();
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    private String getCurrentTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return simpleDateFormat.format(now);
    }

}
