package com.jasonkcwong.pigeon.Models;

import java.io.Serializable;

/**
 * Created by jason on 16-07-30.
 */
public class Contact implements Serializable{
    private String displayName;
    private String phoneNumber;
    private String uid;

    public Contact(String displayName, String phoneNumber){
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        uid = "";
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
