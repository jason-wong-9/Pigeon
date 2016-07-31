package com.jasonkcwong.pigeon.Models;

/**
 * Created by jason on 16-07-30.
 */
public class Contact {
    private String displayName;
    private String phoneNumber;

    public Contact(String displayName, String phoneNumber){
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
