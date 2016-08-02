package com.jasonkcwong.pigeon.Models;

import com.jasonkcwong.pigeon.Utils.DateUtil;

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
        this.date = DateUtil.getCurrentTimeStamp();
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getContent(){
        return content;
    }

    public String getDate(){
        return date;
    }


}
