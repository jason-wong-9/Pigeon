package com.jasonkcwong.pigeon.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 16-07-31.
 */
public class Chat {
    private User sender;
    private User receiver;
    private List<Message> messageList;

    public Chat(User sender, User receiver){
        this.sender = sender;
        this.receiver = receiver;
        this.messageList = new ArrayList<>();
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void addMessage(Message message){
        this.messageList.add(message);
    }
}
