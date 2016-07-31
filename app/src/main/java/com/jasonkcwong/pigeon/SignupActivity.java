package com.jasonkcwong.pigeon;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.jasonkcwong.pigeon.Models.Contact;
import com.jasonkcwong.pigeon.Models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 16-07-30.
 */
public class SignupActivity extends AppCompatActivity{
    public final String TAG = SignupActivity.class.getName();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mPhoneNumberText;

    private Button mSignupButton;
    private Button mSwitchButton;
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
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
            retrieveAllContacts();
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

    private List<Contact> retrieveContacts(){
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);

        int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(indexName);
                String phoneNumber = cursor.getString(indexNumber);
                Contact contact = new Contact(name, phoneNumber);
                Log.d(TAG, "Add contact with name = " + name + " and phone number = " + phoneNumber);
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contacts;
    }

    private void retrieveAllContacts(){
        if (ContextCompat.checkSelfPermission(SignupActivity.this,
                android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale( SignupActivity.this,
                    android.Manifest.permission.READ_CONTACTS)) {


            } else {

                ActivityCompat.requestPermissions( SignupActivity.this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        REQUEST_CODE_ASK_PERMISSIONS);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    retrieveContacts();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
