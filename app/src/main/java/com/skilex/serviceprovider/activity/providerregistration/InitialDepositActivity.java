package com.skilex.serviceprovider.activity.providerregistration;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.loginmodule.OTPVerificationActivity;
import com.skilex.serviceprovider.activity.loginmodule.RegisterActivity;
import com.skilex.serviceprovider.ccavenue.activity.InitialScreenActivity;
import com.skilex.serviceprovider.ccavenue.utility.ServiceUtility;
import com.skilex.serviceprovider.helper.AlertDialogHelper;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.interfaces.DialogClickListener;
import com.skilex.serviceprovider.languagesupport.BaseActivity;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;
import com.skilex.serviceprovider.serviceinterfaces.IServiceListener;
import com.skilex.serviceprovider.utils.CommonUtils;
import com.skilex.serviceprovider.utils.PreferenceStorage;
import com.skilex.serviceprovider.utils.SkilExConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static android.util.Log.d;
import static android.util.Log.println;

public class InitialDepositActivity extends BaseActivity implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = InitialDepositActivity.class.getName();

    private Button btnPay;
    private String payment = "100.00", orderId;
    private TextView txtDeposit;

    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_deposit);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);
        txtDeposit = findViewById(R.id.txt_deposit_amount);

        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        orderId = randomNum.toString() + "-" + PreferenceStorage.getUserMasterId(getApplicationContext());

        getPayableAmount();

    }

    private void getPayableAmount() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SkilExConstants.USER_MASTER_ID, PreferenceStorage.getUserMasterId(getApplicationContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = SkilExConstants.BUILD_URL + SkilExConstants.API_DEPOSIT_AMOUNT;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.haveNetworkConnection(getApplicationContext())) {
            if (v == btnPay) {

                PreferenceStorage.savePaymentType(getApplicationContext(), "advance");
                Intent i = new Intent(getApplicationContext(), InitialScreenActivity.class);
                i.putExtra("amount", payment);
                i.putExtra("orderid", orderId);
                startActivity(i);
                finish();
            }

        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(SkilExConstants.PARAM_MESSAGE);
                d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);

                    } else {
                        signInSuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {

        progressDialogHelper.hideProgressDialog();

        if (validateSignInResponse(response)) {

            try {

//                JSONObject depositAmount = response.getJSONObject("deposit_data");
                String getDepositAmount = response.getString("deposit_data");
                double f = Double.parseDouble(getDepositAmount);
                payment = Double.toString(f);
                System.out.println(payment);
                txtDeposit.setText("₹ " + payment);


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void onError(String error) {

    }
}
