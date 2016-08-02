package com.jasonkcwong.pigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.jasonkcwong.pigeon.Adapters.PigeonPagerAdapter;
import com.jasonkcwong.pigeon.Fragment.ContactsFragment;

/**
 * Created by jason on 16-07-31.
 */
public class PigeonActivity extends AppCompatActivity {
    public static final String TAG = PigeonActivity.class.getName();
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private FirebaseAuth mAuth;
    private PigeonPagerAdapter adapter;

    public static final String frag_title_1 = "Contact";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pigeon);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_pigeon);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(){
        adapter = new PigeonPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ContactsFragment(), frag_title_1);
        Log.d(TAG, frag_title_1 + "Added");

        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }



}
