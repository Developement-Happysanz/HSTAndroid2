package com.skilex.serviceprovider.ccavenue.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.loginmodule.SplashScreenActivity;
import com.skilex.serviceprovider.activity.providerregistration.InitialDepositActivity;
import com.skilex.serviceprovider.bean.support.Preference;
import com.skilex.serviceprovider.ccavenue.utility.AvenuesParams;
import com.skilex.serviceprovider.ccavenue.utility.ServiceUtility;
import com.skilex.serviceprovider.utils.PreferenceStorage;

import static com.skilex.serviceprovider.utils.SkilExConstants.API_ADVANCE_PAYMENT_URL;
import static com.skilex.serviceprovider.utils.SkilExConstants.API_RSA_URL;


public class InitialScreenActivity extends AppCompatActivity {

    private EditText accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;
    private static int SPLASH_TIME_OUT = 500;

    private void init() {
        accessCode = (EditText) findViewById(R.id.accessCode);
        merchantId = (EditText) findViewById(R.id.merchantId);
        orderId = (EditText) findViewById(R.id.orderId);
        currency = (EditText) findViewById(R.id.currency);
        amount = (EditText) findViewById(R.id.amount);
        rsaKeyUrl = (EditText) findViewById(R.id.rsaUrl);
        redirectUrl = (EditText) findViewById(R.id.redirectUrl);
        cancelUrl = (EditText) findViewById(R.id.cancelUrl);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);
        init();
        String value = getIntent().getExtras().getString("amount");
        String redirectURL = PreferenceStorage.getPaymentType(getApplicationContext());
        String orderID = getIntent().getExtras().getString("orderid");

        orderId.setText(orderID);
        amount.setText(value);
        if (redirectURL.equalsIgnoreCase("advance")) {
            redirectUrl.setText(API_ADVANCE_PAYMENT_URL);
            cancelUrl.setText(API_ADVANCE_PAYMENT_URL);
            rsaKeyUrl.setText(API_RSA_URL);
        } else {
            redirectUrl.setText(API_ADVANCE_PAYMENT_URL);
            cancelUrl.setText(API_ADVANCE_PAYMENT_URL);
            rsaKeyUrl.setText((API_RSA_URL));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Mandatory parameters. Other parameters can be added if required.
                String vAccessCode = ServiceUtility.chkNull(accessCode.getText()).toString().trim();
                String vMerchantId = ServiceUtility.chkNull(merchantId.getText()).toString().trim();
                String vCurrency = ServiceUtility.chkNull(currency.getText()).toString().trim();
                String vAmount = ServiceUtility.chkNull(amount.getText()).toString().trim();
                if (!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                    intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount.getText()).toString().trim());

                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());

                    startActivity(intent);
                    finish();
//            finish();
                } else {
                    showToast("All parameters are mandatory.");
                }
            }
        }, SPLASH_TIME_OUT);


    }

    public void onClick(View view) {
        //Mandatory parameters. Other parameters can be added if required.
        String vAccessCode = ServiceUtility.chkNull(accessCode.getText()).toString().trim();
        String vMerchantId = ServiceUtility.chkNull(merchantId.getText()).toString().trim();
        String vCurrency = ServiceUtility.chkNull(currency.getText()).toString().trim();
        String vAmount = ServiceUtility.chkNull(amount.getText()).toString().trim();
        if (!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
            intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
            intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
            intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount.getText()).toString().trim());

            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            startActivity(intent);
//            finish();
        } else {
            showToast("All parameters are mandatory.");
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //generating new order number for every transaction
//        Integer randomNum = ServiceUtility.randInt(0, 9999999);
//        orderId.setText(randomNum.toString());
    }

}