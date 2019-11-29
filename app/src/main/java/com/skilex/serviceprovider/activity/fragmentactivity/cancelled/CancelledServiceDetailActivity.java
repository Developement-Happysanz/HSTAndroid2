package com.skilex.serviceprovider.activity.fragmentactivity.cancelled;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.languagesupport.BaseActivity;

public class CancelledServiceDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_service_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
