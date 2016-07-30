package com.jasonkcwong.pigeon.Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jason on 16-07-29.
 */
@IgnoreExtraProperties
public class User {
    public String email;
    public String phoneNumber;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String phoneNumber){
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


}
