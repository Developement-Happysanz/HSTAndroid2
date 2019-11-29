package com.skilex.serviceprovider.activity.fragmentactivity.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.fragmentactivity.requested.RequestedServiceDetailActivity;
import com.skilex.serviceprovider.adapter.TransactionListAdapter;
import com.skilex.serviceprovider.bean.support.RequestedServiceArray;
import com.skilex.serviceprovider.bean.support.Transaction;
import com.skilex.serviceprovider.bean.support.TransactionList;
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

import java.util.ArrayList;

import static android.util.Log.d;

public class TransactionHistoryActivity extends BaseActivity implements IServiceListener, DialogClickListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String TAG = TransactionDetailsActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    ListView transHistoryList;
    int totalCount = 0, checkrun = 0;
    protected boolean isLoadingForFirstTime = true;
    ArrayList<Transaction> transactionArrayList = new ArrayList<>();
    TransactionListAdapter transactionListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasanction_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        transHistoryList = (ListView) findViewById(R.id.trans_history);
        transHistoryList.setOnItemClickListener(this);

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
        String url = SkilExConstants.BUILD_URL + SkilExConstants.TRANSACTION_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {
        
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onEvent list item clicked" + position);
        Transaction service = null;
        if ((transactionListAdapter != null) && (transactionListAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = transactionListAdapter.getActualEventPos(position);
            Log.d(TAG, "actual index" + actualindex);
            service = transactionArrayList.get(actualindex);
        } else {
            service = transactionArrayList.get(position);
        }

        Intent intent = new Intent(this, TransactionHistoryDetailActivity.class);
        intent.putExtra("serviceObj", service);
        startActivity(intent);
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
            Gson gson = new Gson();
            TransactionList transactionList = gson.fromJson(response.toString(), TransactionList.class);
            if (transactionList.getTransactionArrayList() != null && transactionList.getTransactionArrayList().size() > 0) {
                totalCount = transactionList.getCount();
//                    this.ongoingServiceArrayList.addAll(ongoingServiceList.getTransactionArrayList());
                isLoadingForFirstTime = false;
                updateListAdapter(transactionList.getTransactionArrayList());
            } else {
                if (transactionArrayList != null) {
                    transactionArrayList.clear();
                    updateListAdapter(transactionList.getTransactionArrayList());
                }
            }
        }
    }

    protected void updateListAdapter(ArrayList<Transaction> transactionArray) {
        transactionArrayList.clear();
        transactionArrayList.addAll(transactionArray);
        if (transactionListAdapter == null) {
            transactionListAdapter = new TransactionListAdapter(this, transactionArrayList);
            transHistoryList.setAdapter(transactionListAdapter);
        } else {
            transactionListAdapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public void onError(String error) {

    }
}