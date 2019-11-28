package com.skilex.serviceprovider.activity.fragmentactivity.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.fragmentactivity.requested.RequestedServicesActivity;
import com.skilex.serviceprovider.adapter.ExpertTabAdapter;
import com.skilex.serviceprovider.bean.support.RequestedServiceList;
import com.skilex.serviceprovider.helper.AlertDialogHelper;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.interfaces.DialogClickListener;
import com.skilex.serviceprovider.languagesupport.BaseActivity;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;
import com.skilex.serviceprovider.serviceinterfaces.IServiceListener;
import com.skilex.serviceprovider.utils.CommonUtils;
import com.skilex.serviceprovider.utils.PreferenceStorage;
import com.skilex.serviceprovider.utils.SkilExConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.util.Log.d;

public class TransactionDetailsActivity extends BaseActivity implements IServiceListener, DialogClickListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String TAG = TransactionDetailsActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    TextView yesterdaySelfCommission, yesterdaySkilexCommission, yesterdayOnline, yesterdayCash, yesterdayTransCount, yesterdayTransAmt;
    TextView overallSelfCommission, overallSkilexCommission, overallOnline, overallCash, overallTransCount, overallTransAmt;
    RelativeLayout goNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        yesterdaySelfCommission = (TextView) findViewById(R.id.self_commission);
        yesterdaySkilexCommission = (TextView) findViewById(R.id.skilex_commission);
        yesterdayOnline = (TextView) findViewById(R.id.today_online_payment);
        yesterdayCash = (TextView) findViewById(R.id.today_cash_payment);
        yesterdayTransCount = (TextView) findViewById(R.id.today_trans_count);
        yesterdayTransAmt = (TextView) findViewById(R.id.today_trans_amount);

        overallSelfCommission = (TextView) findViewById(R.id.overall_self_commission);
        overallSkilexCommission = (TextView) findViewById(R.id.overall_skilex_commission);
        overallOnline = (TextView) findViewById(R.id.overall_online_payment);
        overallCash = (TextView) findViewById(R.id.overall_cash_payment);
        overallTransCount = (TextView) findViewById(R.id.overall_trans_count);
        overallTransAmt = (TextView) findViewById(R.id.overall_trans_amount);

        goNext = (RelativeLayout) findViewById(R.id.go_to_trans_list);
        goNext.setOnClickListener(this);

        callReqService();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void callReqService() {
        if (CommonUtils.isNetworkAvailable(this)) {
            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            loadTrans();
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    private void loadTrans() {
        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = PreferenceStorage.getUserMasterId(this);
        try {
            jsonObject.put(SkilExConstants.USER_MASTER_ID, id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = SkilExConstants.BUILD_URL + SkilExConstants.TRANSACTION_DETAILS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {
        if (v == goNext) {
            Intent intent = new Intent(this, TransactionHistoryActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                JSONArray getoverallResultData = response.getJSONArray("overallResult");
                JSONObject getyesterdayResultData = response.getJSONObject("yesterdayResult");
                yesterdaySelfCommission.setText("₹" + getyesterdayResultData.getString("serv_prov_commission_amt"));
                yesterdaySkilexCommission.setText("₹" + getyesterdayResultData.getString("skilex_commission_amt"));
                yesterdayOnline.setText("₹" + getyesterdayResultData.getString("online_transaction_amt"));
                yesterdayCash.setText("₹" + getyesterdayResultData.getString("offline_transaction_amt"));
                yesterdayTransCount.setText("No of transactions: " +getyesterdayResultData.getString("total_service_per_day"));
                yesterdayTransAmt.setText("Total amount: ₹" + getyesterdayResultData.getString("serv_total_amount"));

                overallSelfCommission.setText("₹" + getoverallResultData.getJSONObject(0).getString("total_serv_prov_commission"));
                overallSkilexCommission.setText("₹" + getoverallResultData.getJSONObject(0).getString("total_skilex_commission"));
                overallOnline.setText("₹" + getoverallResultData.getJSONObject(0).getString("total_online_transaction"));
                overallCash.setText("₹" + getoverallResultData.getJSONObject(0).getString("total_offline_transaction"));
                overallTransCount.setText("No of transactions: " + getoverallResultData.getJSONObject(0).getString("total_services"));
                overallTransAmt.setText("Total amount: ₹" + getoverallResultData.getJSONObject(0).getString("total_amount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }
}

