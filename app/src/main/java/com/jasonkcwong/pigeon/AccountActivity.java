package com.jasonkcwong.pigeon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by jason on 16-07-30.
 */
public class AccountActivity extends AppCompatActivity {
    private final String TAG = AccountActivity.class.getName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "I am at account activity");
    }
}
