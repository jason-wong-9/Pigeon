package com.jasonkcwong.pigeon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jasonkcwong.pigeon.Fragment.LoginFragment;
import com.jasonkcwong.pigeon.Fragment.SignupFragment;

/**
 * Created by jason on 16-07-31.
 */
public class AccountActivity extends AppCompatActivity{
    public static final String EXTRA_TYPE = "type";
    public static final int TYPE_LOGIN = 0;
    public static final int TYPE_SIGNUP = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        int type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_LOGIN);
        switch(type) {
            case TYPE_LOGIN:
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_frame, new LoginFragment())
                        .commit();
                break;
            case TYPE_SIGNUP:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_frame, new SignupFragment())
                        .commit();
                break;
        }
    }
}
