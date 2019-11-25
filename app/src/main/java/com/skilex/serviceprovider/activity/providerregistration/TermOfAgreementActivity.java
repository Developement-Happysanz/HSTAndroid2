package com.skilex.serviceprovider.activity.providerregistration;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.languagesupport.BaseActivity;
import com.skilex.serviceprovider.utils.PreferenceStorage;

public class TermOfAgreementActivity extends BaseActivity {

    private CheckBox chbAgreement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        chbAgreement = findViewById(R.id.chb_agreement);

        chbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    PreferenceStorage.saveTermOfAgreement(getApplicationContext(), "Agree");
                } else {
                    PreferenceStorage.saveTermOfAgreement(getApplicationContext(), "Not Agree");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
