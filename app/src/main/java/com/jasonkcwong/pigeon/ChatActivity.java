package com.jasonkcwong.pigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jasonkcwong.pigeon.Fragment.ContactsFragment;
import com.jasonkcwong.pigeon.Models.Contact;


/**
 * Created by jason on 16-08-01.
 */
public class ChatActivity extends AppCompatActivity{
    public static final String TAG = ChatActivity.class.getName();
    private ActionBar mActionBar;
    private EditText mMessageText;
    private Button mSubmitButton;
    private ListView mChatListView;
    private Contact contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        contact = (Contact) getIntent().getSerializableExtra(ContactsFragment.EXTRA_CONTACT);
        mActionBar.setTitle(contact.getDisplayName());

        mMessageText = (EditText) findViewById(R.id.edit_message);
        mSubmitButton = (Button) findViewById(R.id.button_submit);
        mChatListView = (ListView) findViewById(R.id.chat_list);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save to Firebase and notify adapter
            }
        });
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, PigeonActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

}
