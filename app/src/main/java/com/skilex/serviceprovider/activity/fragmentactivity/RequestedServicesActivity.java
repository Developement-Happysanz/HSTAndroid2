package com.skilex.serviceprovider.activity.fragmentactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.languagesupport.BaseActivity;

public class RequestedServicesActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
