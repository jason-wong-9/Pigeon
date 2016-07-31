package com.jasonkcwong.pigeon;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.jasonkcwong.pigeon.Models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 16-07-31.
 */
public class PigeonActivity extends AppCompatActivity {
    public static final String TAG = PigeonActivity.class.getName();
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;

    public static final String EXTRA_CONTACT = "contact";
    public static final int CONTACT_FALSE = 0;
    public static final int CONTACT_TRUE = 1;

    final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static final String frag_title_1 = "Contact";
    public static final String frag_title_2 = "Chat";
    public static final String frag_title_3 = "Setting";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        int type = getIntent().getIntExtra(EXTRA_CONTACT, CONTACT_FALSE);

        if (type == CONTACT_TRUE){
            retrieveAllContacts();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    private void setupViewPager(ViewPager viewPager){
        PigeonPagerAdapter adapter = new PigeonPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new TabFragment(), frag_title_1);
        Log.d(TAG, frag_title_1 + "Added");

        adapter.addFragment(new TabFragment(), frag_title_2);
        Log.d(TAG, frag_title_2 + "Added");

        adapter.addFragment(new TabFragment(), frag_title_3);
        Log.d(TAG, frag_title_3 + "Added");

        viewPager.setAdapter(adapter);
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

    public void retrieveAllContacts(){
        if (ContextCompat.checkSelfPermission(PigeonActivity.this,
                android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale( PigeonActivity.this,
                    android.Manifest.permission.READ_CONTACTS)) {


            } else {

                ActivityCompat.requestPermissions(PigeonActivity.this,
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    retrieveContacts();

                } else {

                }
                return;
            }
        }
    }
}
