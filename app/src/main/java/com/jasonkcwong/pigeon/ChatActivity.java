package com.jasonkcwong.pigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jasonkcwong.pigeon.Adapters.MessageAdapter;
import com.jasonkcwong.pigeon.Models.Contact;
import com.jasonkcwong.pigeon.Models.Message;


/**
 * Created by jason on 16-08-01.
 */
public class ChatActivity extends AppCompatActivity{
    public static final String TAG = ChatActivity.class.getName();
    public static final String EXTRA_CONTACT = "CONTACT";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ActionBar mActionBar;
    private EditText mMessageText;
    private Button mSubmitButton;
    private ListView mChatListView;
    private Contact contact;
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        contact = (Contact) getIntent().getSerializableExtra(EXTRA_CONTACT);
        mActionBar.setTitle(contact.getDisplayName());

        mMessageText = (EditText) findViewById(R.id.edit_message);
        mSubmitButton = (Button) findViewById(R.id.button_submit);
        mChatListView = (ListView) findViewById(R.id.chat_list);

//        mAdapter = new ContactAdapter(ChatActivity.this, );
//        mChatListView.setAdapter(mAdapter);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save to Firebase and notify adapter
                String messageText = mMessageText.getText().toString();
                submitMessageToFirebase(messageText, contact.getUid());
                Log.d(TAG, contact.getUid());
            }
        });
     }

    private void submitMessageToFirebase(String messageString, String receiverUid){
        String userId = mAuth.getCurrentUser().getUid();
        Message message = new Message(userId, receiverUid,messageString);
        mDatabase.child("messages").child(userId).setValue(message);
        mDatabase.child("messages").child(receiverUid).setValue(message);
        Log.d(TAG, messageString + "ADDed");
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
