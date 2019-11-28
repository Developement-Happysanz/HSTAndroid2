package com.skilex.serviceprovider.activity.fragmentactivity.transaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.LandingPageActivity;
import com.skilex.serviceprovider.activity.fragmentactivity.cancelled.CancelRequestedServiceActivity;
import com.skilex.serviceprovider.activity.fragmentactivity.requested.RequestedServiceDetailActivity;
import com.skilex.serviceprovider.bean.support.Transaction;
import com.skilex.serviceprovider.bean.support.StoreServicePerson;
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

import org.apache.http.entity.mime.content.ContentBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.util.Log.d;

public class TransactionHistoryDetailActivity extends BaseActivity implements IServiceListener, DialogClickListener,
        View.OnClickListener {

    private static final String TAG = RequestedServiceDetailActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    Transaction transaction;
    private TextView txtTransactionDate, txtTransactionCount, txtTransactionAmt, onlineProviderComm,
            offlineProviderComm, onlineSkilexComm, offlineSkilexComm, txtTransactionProviderComm3,
            txtTransactionSkilexComm3, onlinePayment, offlinePayment, fromSkilex, toSkilex, payStatus, paySkilex;
    private String orderId, orderMainId;
    private LinearLayout to, from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasanction_history_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        transaction = (Transaction) getIntent().getSerializableExtra("serviceObj");

        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        orderId = randomNum.toString() + "-" + PreferenceStorage.getUserMasterId(getApplicationContext());

        setUpUI();
        transdetail();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void setUpUI() {

        txtTransactionDate = findViewById(R.id.trans_date);
        txtTransactionCount = findViewById(R.id.trans_count);
        txtTransactionAmt = findViewById(R.id.trans_amount);
        onlineProviderComm = findViewById(R.id.self_amount);
        onlineSkilexComm = findViewById(R.id.skilex_amount);
        offlineProviderComm = findViewById(R.id.offline_self_amount);
        offlineSkilexComm = findViewById(R.id.offline_skilex_amount);
        onlinePayment = findViewById(R.id.online_payment);
        offlinePayment = findViewById(R.id.offline_payment);
        txtTransactionProviderComm3 = findViewById(R.id.net_self_commission_amount);
        txtTransactionSkilexComm3 = findViewById(R.id.net_skilex_commission_amount);
        fromSkilex = findViewById(R.id.from_skilex);
        toSkilex = findViewById(R.id.to_skilex);
        payStatus = findViewById(R.id.pay_status);
        paySkilex = findViewById(R.id.pay_skilex);
        paySkilex.setOnClickListener(this);

        to = findViewById(R.id.to_skilex_layout);
        from = findViewById(R.id.from_skilex_layout);


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
        try {
            if (validateSignInResponse(response)) {
                JSONObject userData = response.getJSONObject("transactionResult");


                txtTransactionDate.setText(userData.getString("service_date"));
                txtTransactionCount.setText("No of transactions: " + userData.getString("total_service_per_day"));
                txtTransactionAmt.setText("Total amount: ₹" + userData.getString("serv_total_amount"));
                onlineProviderComm.setText("₹" + userData.getString("online_serv_prov_commission"));
                onlineSkilexComm.setText("₹" + userData.getString("online_skilex_commission"));
                onlinePayment.setText("₹" + userData.getString("online_transaction_amt"));
                offlinePayment.setText("₹" + userData.getString("offline_transaction_amt"));
                offlineProviderComm.setText("₹" + userData.getString("offline_serv_prov_commission"));
                offlineSkilexComm.setText("₹" + userData.getString("offline_skilex_commission"));
                fromSkilex.setText("₹" + userData.getString("serv_prov_commission_amt"));
                toSkilex.setText("₹" + userData.getString("pay_to_serv_prov"));
                txtTransactionSkilexComm3.setText("₹" + userData.getString("pay_to_serv_prov"));
                txtTransactionProviderComm3.setText("₹" + userData.getString("serv_prov_commission_amt"));
                if(userData.getString("pay_to_ser_provider_flag").equalsIgnoreCase("Yes")) {
                    to.setVisibility(View.VISIBLE);
                    from.setVisibility(View.GONE);
                    if (userData.getString("serv_prov_closing_status").equalsIgnoreCase("Paid")) {
                        paySkilex.setVisibility(View.GONE);
                        payStatus.setText("Paid");
                        payStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.button_round_green_filled));
                    } else {
                        paySkilex.setVisibility(View.VISIBLE);
                        payStatus.setText("Pending");
                        payStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.button_round_red_filled));
                    }
                } else {
                    to.setVisibility(View.GONE);
                    from.setVisibility(View.VISIBLE);
                    paySkilex.setVisibility(View.GONE);
                    if (userData.getString("serv_prov_closing_status").equalsIgnoreCase("Paid")) {
                        payStatus.setText("Paid");
                        payStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.button_round_green_filled));
                    } else {
                        payStatus.setText("Pending");
                        payStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.button_round_red_filled));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onClick(View v) {
        if (v == paySkilex) {
            PreferenceStorage.savePaymentType(getApplicationContext(), "advance");
            Intent i = new Intent(getApplicationContext(), InitialScreenActivity.class);
            String[] parts = toSkilex.getText().toString().split("₹");
            String part1 = parts[0]; // empty
            String part2 = parts[1]; // Amt
            i.putExtra("amount", part2);
            orderMainId = orderId +"-"+ transaction.getId();
            i.putExtra("orderid", orderMainId);
            startActivity(i);
            finish();
        }
    }
    private void transdetail() {

        String userMasterId = PreferenceStorage.getUserMasterId(getApplicationContext());
        String Id = transaction.getId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SkilExConstants.USER_MASTER_ID, userMasterId);
            jsonObject.put(SkilExConstants.DAILY_ID, Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = SkilExConstants.BUILD_URL + SkilExConstants.TRANSACTION_LIST_DETAIL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }
}
