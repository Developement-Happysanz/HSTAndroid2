package com.skilex.serviceprovider.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.fragment.HomeFragment;
import com.skilex.serviceprovider.fragment.ProfileFragment;
import com.skilex.serviceprovider.fragment.ServiceExpertFragment;
import com.skilex.serviceprovider.interfaces.DialogClickListener;
import com.skilex.serviceprovider.languagesupport.BaseActivity;

public class LandingPageActivity extends BaseActivity implements DialogClickListener {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    int checkPointSearch = 0;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_home:
                                changeFragment(0);
//                                fabView.setVisibility(View.VISIBLE);
                                break;

                            case R.id.action_services:
                                changeFragment(1);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_profile:
                                changeFragment(2);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                        }
                        return true;
                    }
                });

        changeFragment(0);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

    private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 1) {
            checkPointSearch = 1;
            newFragment = new ServiceExpertFragment();
        } else if (position == 0) {
            checkPointSearch = 0;
            newFragment = new HomeFragment();
        } else if (position == 2) {
            checkPointSearch = 2;
            newFragment = new ProfileFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}
