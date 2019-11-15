package com.skilex.serviceprovider.activity.loginmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.LandingPageActivity;
import com.skilex.serviceprovider.activity.providerregistration.InitialDepositActivity;
import com.skilex.serviceprovider.activity.providerregistration.RegOrgDocumentUploadActivity;
import com.skilex.serviceprovider.activity.providerregistration.UnRegOrgDocumentUploadActivity;
import com.skilex.serviceprovider.utils.PreferenceStorage;
import com.skilex.serviceprovider.utils.SkilExValidator;
import com.skilex.serviceprovider.utils.SmsVerification;

import java.util.ArrayList;


public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    //    AppSignatureHelper appSignatureHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_display);

        //        appSignatureHelper = new AppSignatureHelper(this);
//        appSignatureHelper.getAppSignatures();

        ArrayList<String> appCodes = new ArrayList<>();
        SmsVerification hash = new SmsVerification(getBaseContext());
        appCodes = hash.getAppSignatures();
        String yourhash = appCodes.get(0);
        Log.d("Hash Key: ", yourhash);
        System.out.println("Hash Key: " + yourhash);

        Toast.makeText(SplashScreenActivity.this, "Hash key...  " + yourhash, Toast.LENGTH_SHORT).show();

//        String GCMKey = PreferenceStorage.getGCM(getApplicationContext());
//        if (GCMKey.equalsIgnoreCase("")) {
//            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//            PreferenceStorage.saveGCM(getApplicationContext(), refreshedToken);
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (PreferenceStorage.getUserMasterId(getApplicationContext()) != null && SkilExValidator.checkNullString(PreferenceStorage.getUserMasterId(getApplicationContext()))) {
                    if (PreferenceStorage.getActiveStatus(getApplicationContext()).equalsIgnoreCase("Live")) {
                        Intent i = new Intent(SplashScreenActivity.this, LandingPageActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreenActivity.this, new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String newToken = instanceIdResult.getToken();
                                Log.e("newToken", newToken);
                                PreferenceStorage.saveGCM(getApplicationContext(), newToken);

                            }
                        });
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                } else {

                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreenActivity.this, new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String newToken = instanceIdResult.getToken();
                            Log.e("newToken", newToken);
                            PreferenceStorage.saveGCM(getApplicationContext(), newToken);

                        }
                    });
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}

