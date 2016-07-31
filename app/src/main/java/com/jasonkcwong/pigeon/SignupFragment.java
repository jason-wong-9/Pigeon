package com.jasonkcwong.pigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SignupFragment extends Fragment {
    public final String TAG = SignupFragment.class.getName();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mPhoneNumberText;

    private Button mSignupButton;
    private Button mSwitchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEmailText = (EditText) rootView.findViewById(R.id.signup_email_editext);
        mPasswordText = (EditText) rootView.findViewById(R.id.signup_password_editext);
        mPhoneNumberText = (EditText) rootView.findViewById(R.id.signup_phone_edittext);

        mSignupButton = (Button) rootView.findViewById(R.id.button_signup);
        mSwitchButton = (Button) rootView.findViewById(R.id.signup_button_switch);

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


        return rootView;
    }

    private void signup(){
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        final String phoneNumber = mPhoneNumberText.getText().toString();

        Log.d(TAG, "createAccount:" + email);
        //Validate form : requires 6 characters

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser(), phoneNumber);
                        } else {
                            Toast.makeText(getActivity(), "Failed to create account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user, String phoneNumber){
        final String userId = user.getUid();
        if (writeNewUser(userId, user.getEmail(), phoneNumber)){
            //Intent

            Intent intent = new Intent(getActivity(), PigeonActivity.class);
            intent.putExtra(PigeonActivity.EXTRA_CONTACT, PigeonActivity.CONTACT_TRUE);
            startActivity(intent);
            getActivity().finish();
        }

    }

    private boolean writeNewUser(String userId, String email, String phoneNumber){
        User user = new User(email, phoneNumber);
        mDatabase.child("users").child(userId).setValue(user);
        return true;
    }

    private void switchLogin(){
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        intent.putExtra(AccountActivity.EXTRA_TYPE, AccountActivity.TYPE_LOGIN);
        startActivity(intent);
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
