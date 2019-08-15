package com.skilex.serviceprovider.activity.loginmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;
import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.providerregistration.UnRegOrgDocumentUploadActivity;
import com.skilex.serviceprovider.activity.providerregistration.UnRegisteredOnganizationInfoActivity;
import com.skilex.serviceprovider.languagesupport.LocaleManager;
import com.skilex.serviceprovider.utils.PreferenceStorage;


public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_display);

        String GCMKey = PreferenceStorage.getGCM(getApplicationContext());
        if (GCMKey.equalsIgnoreCase("")) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            PreferenceStorage.saveGCM(getApplicationContext(), refreshedToken);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashScreenActivity.this, UnRegOrgDocumentUploadActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

