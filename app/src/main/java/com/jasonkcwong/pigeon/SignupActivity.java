package com.jasonkcwong.pigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jasonkcwong.pigeon.Models.User;

/**
 * Created by jason on 16-07-30.
 */
public class SignupActivity extends AppCompatActivity{
    private final String TAG = SignupActivity.class.getName();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mPhoneNumberText;

    private Button mSignupButton;
    private Button mSwitchButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEmailText = (EditText) findViewById(R.id.signup_email_editext);
        mPasswordText = (EditText) findViewById(R.id.signup_password_editext);
        mPhoneNumberText = (EditText) findViewById(R.id.signup_phone_edittext);

        mSignupButton = (Button) findViewById(R.id.button_signup);
        mSwitchButton = (Button) findViewById(R.id.signup_button_switch);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) return;
                signup();
            }
        });

        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchLogin();
            }
        });
    }

    private void signup(){
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        final String phoneNumber = mPhoneNumberText.getText().toString();

        Log.d(TAG, "createAccount:" + email);
        //Validate form : requires 6 characters

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser(), phoneNumber);
                        } else {
                            Toast.makeText(SignupActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user, String phoneNumber){
        final String userId = user.getUid();
        if (writeNewUser(userId, user.getEmail(), phoneNumber)){
            //Intent
        }

    }

    private boolean writeNewUser(String userId, String email, String phoneNumber){
        User user = new User(email, phoneNumber);
        mDatabase.child("users").child(userId).setValue(user);
        return true;
    }

    private void switchLogin(){
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailText.setError("Required.");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        String password = mPasswordText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordText.setError("Required.");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        String phoneNumber = mPhoneNumberText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || !validatePhone(phoneNumber)) {
            mPhoneNumberText.setError("Required.");
            valid = false;
        } else {
            mPhoneNumberText.setError(null);
        }

        return valid;
    }

    private boolean validatePhone(String phoneNumber){
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }


}
