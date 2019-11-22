package com.skilex.serviceprovider.activity.providerregistration;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.LandingPageActivity;
import com.skilex.serviceprovider.activity.loginmodule.OTPVerificationActivity;
import com.skilex.serviceprovider.helper.AlertDialogHelper;
import com.skilex.serviceprovider.languagesupport.BaseActivity;
import com.skilex.serviceprovider.utils.CommonUtils;
import com.skilex.serviceprovider.utils.PreferenceStorage;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

    private Button btnStart;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.haveNetworkConnection(getApplicationContext())) {
            if (v == btnStart) {
                Intent i = new Intent(WelcomeActivity.this, LandingPageActivity.class);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                startActivity(i);
                finish();
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
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
}
