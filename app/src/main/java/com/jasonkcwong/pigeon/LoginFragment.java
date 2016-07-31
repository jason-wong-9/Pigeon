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

/**
 * Created by jason on 16-07-30.
 */
public class LoginFragment extends Fragment {
    public final String TAG = LoginFragment.class.getName();
    private FirebaseAuth mAuth;

    private EditText mEmailText;
    private EditText mPasswordText;

    private Button mLoginButton;
    private Button mSwitchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        mEmailText = (EditText) rootView.findViewById(R.id.login_email_editText);
        mPasswordText = (EditText) rootView.findViewById(R.id.login_password_editText);

        mLoginButton = (Button) rootView.findViewById(R.id.button_login);
        mSwitchButton = (Button) rootView.findViewById(R.id.login_button_switch);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) return;
                login();
            }
        });
        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchSignup();
            }
        });
        return rootView;
    }

    private void login(){
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        Log.d(TAG, "signIn:" + email);
        //ValidateForm

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()){
                            Log.d(TAG, "Logged on sucessfully");
                            onAuthSuccess();
                        } else {
                            Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(){

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_CONTACT, MainActivity.CONTACT_FALSE);
        startActivity(intent);

    }

    private void switchSignup(){
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        intent.putExtra(AccountActivity.EXTRA_TYPE, AccountActivity.TYPE_SIGNUP);
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

        return valid;
    }



}
