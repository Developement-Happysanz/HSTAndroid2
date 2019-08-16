package com.skilex.serviceprovider.activity.providerregistration;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.loginmodule.RegisterActivity;
import com.skilex.serviceprovider.languagesupport.BaseActivity;

public class InitialDepositActivity extends BaseActivity {

    private static final String TAG = InitialDepositActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_deposit);
    }
}
