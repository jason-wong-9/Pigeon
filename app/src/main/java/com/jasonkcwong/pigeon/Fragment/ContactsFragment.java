package com.jasonkcwong.pigeon.Fragment;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jasonkcwong.pigeon.Adapters.ContactAdapter;
import com.jasonkcwong.pigeon.Models.Contact;
import com.jasonkcwong.pigeon.Models.ContactBook;
import com.jasonkcwong.pigeon.Models.User;
import com.jasonkcwong.pigeon.OnItemClickListener;
import com.jasonkcwong.pigeon.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 16-07-31.
 */
public class ContactsFragment extends Fragment implements OnItemClickListener{
    public static final String TAG = ContactsFragment.class.getName();
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private ListView mListView;
    private ContactAdapter mAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private List<Contact> mContacts;
    private ContactBook mContactBook;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        mListView = (ListView) rootView.findViewById(R.id.contact_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mContactBook = new ContactBook();
        mContacts = new ArrayList<>();
        retrieveAllContacts();

        mContactBook.sortContact();
        Log.d(TAG, mContactBook.getContacts().toString());
        mAdapter = new ContactAdapter(getActivity(), mContactBook.getContacts(), this);
        mListView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onCardClick(View view, int position) {
        //Intent to Chat
    }

    @Override
    public boolean onCardLongClick(View view, int position) {
        return false;
    }

    private void retrieveContacts(){
        Log.d(TAG, "retrieving from contacts");
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);

        int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(indexName);
                String phoneNumber = cursor.getString(indexNumber);
                Contact contact = new Contact(name, phoneNumber);
                Log.d(TAG, "Add contact with name = " + name + " and phone number = " + phoneNumber);
                mContacts.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void retrieveAllContacts(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_CONTACTS)) {
            } else {
                requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            retrieveContacts();
            retrieveContactsFromFirebase();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Requested Permission");
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrieveContacts();
                    retrieveContactsFromFirebase();
                } else {

                }
                return;
            }
        }
    }
    private void retrieveContactsFromFirebase(){
        Log.d(TAG, "retrieving from firebase");
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    String phoneNumber = user.phoneNumber;
                    Log.v(TAG, "userid:" + child.getKey());
                    Log.v(TAG, "currentuser:" + mAuth.getCurrentUser().getUid());
                    if (child.getKey().equals(mAuth.getCurrentUser().getUid())){
                        continue;
                    }
                    for (Contact contact: mContacts){
                        if (PhoneNumberUtils.compare(contact.getPhoneNumber(), phoneNumber)){
                            contact.setUid(child.getKey());
                            mContactBook.addContact(contact);
                            Log.v(TAG, phoneNumber);
                            Log.v(TAG, contact.getUid());
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Database Error");
            }
        });
    }
}
