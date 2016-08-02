package com.jasonkcwong.pigeon.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jasonkcwong.pigeon.Models.Contact;
import com.jasonkcwong.pigeon.Models.Message;
import com.jasonkcwong.pigeon.R;
import com.jasonkcwong.pigeon.Utils.DateUtil;

import java.util.List;

/**
 * Created by jason on 16-08-02.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    public static final String TAG = MessageAdapter.class.getName();
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseReference;
    private List<Message> mMessages;
    private FirebaseAuth mAuth;
    private Contact receiverContact;

    private LinearLayout linearLayout;
    private TextView messageText;
    private TextView timeText;

    public MessageAdapter(final Context context, DatabaseReference ref, List<Message> message, final Contact contact){
        super(context, android.R.layout.simple_list_item_1, message);
        mMessages = message;
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = ref;
        receiverContact = contact;
        singleMessageLookup();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message messageAdded = dataSnapshot.getValue(Message.class);
                String currentId = mAuth.getCurrentUser().getUid();
                if (messageAdded.getReceiverId().equals(currentId)||
                        messageAdded.getReceiverId().equals(receiverContact.getUid()) ||
                        messageAdded.getSenderId().equals(currentId) ||
                        messageAdded.getSenderId().equals(receiverContact.getUid())){
                    mMessages.add(messageAdded);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postMessages:onCancelled", databaseError.toException());
                Toast.makeText(context, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        ref.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = mMessages.get(position);
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.message_adapter_item, null);
        }
        linearLayout = (LinearLayout) view.findViewById(R.id.messageLayout);
        messageText = (TextView) view.findViewById(R.id.messageText);
        timeText = (TextView) view.findViewById(R.id.timeText);

        if (message.getSenderId().equals(mAuth.getCurrentUser().getUid())){
            linearLayout.setGravity(Gravity.RIGHT);
        } else {
            linearLayout.setGravity(Gravity.LEFT);
        }
        messageText.setText(message.getContent());
        String messageDate = message.getDate();
        if (messageDate.contains(DateUtil.getTodayDate())){
            timeText.setText(messageDate.substring(11));
        }
        timeText.setText(messageDate);

        return view;
    }

    private void singleMessageLookup(){
        Log.d(TAG, "retrieving from firebase");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Message messageAdded = child.getValue(Message.class);
                    String currentId = mAuth.getCurrentUser().getUid();
                    Log.d(TAG, messageAdded.getSenderId());
                    Log.d(TAG, currentId);
                    if (messageAdded.getReceiverId().equals(currentId) ||
                            messageAdded.getReceiverId().equals(receiverContact.getUid()) ||
                            messageAdded.getSenderId().equals(currentId) ||
                            messageAdded.getSenderId().equals(receiverContact.getUid())){
                        Log.d(TAG, "hi");
                        mMessages.add(messageAdded);

                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Database Error");
            }
        });
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }
}
