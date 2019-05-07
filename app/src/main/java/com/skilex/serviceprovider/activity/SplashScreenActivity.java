package com.skilex.serviceprovider.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.utils.PreferenceStorage;


public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                if (PreferenceStorage.getUserName(getApplicationContext()) != null && AppValidator.checkNullString(PreferenceStorage.getUserName(getApplicationContext()))) {
//                    if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
//                        Intent i = new Intent(SplashScreenActivity.this, TnsrlmDashboard.class);
//                        startActivity(i);
//                        finish();
//                    } else if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("3")) {
//                        Intent i = new Intent(SplashScreenActivity.this, PiaDashboard.class);
//                        startActivity(i);
//                        finish();
//                    }
//
//                }
//                else {
//                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
//                    startActivity(i);
//                    finish();
//                }
            }
        }, SPLASH_TIME_OUT);

    }
}

