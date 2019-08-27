package com.skilex.serviceprovider.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.fragmentactivity.TransactionDetailsActivity;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.interfaces.DialogClickListener;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;
import com.skilex.serviceprovider.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

public class HomeFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = HomeFragment.class.getName();
    Context context;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    Handler mHandler = new Handler();
    private View rootView;

    private LinearLayout layoutRequested, layoutAssigned, layoutOnGoing, layoutCompleted, layoutCancelled, layoutTransaction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Dashboard");
        init();
        return rootView;
    }

    void init() {
        layoutRequested = rootView.findViewById(R.id.llRequested);
        layoutRequested.setOnClickListener(this);
        layoutAssigned = rootView.findViewById(R.id.llAssigned);
        layoutAssigned.setOnClickListener(this);
        layoutOnGoing = rootView.findViewById(R.id.llOnGoing);
        layoutOnGoing.setOnClickListener(this);
        layoutCompleted = rootView.findViewById(R.id.llCompleted);
        layoutCompleted.setOnClickListener(this);
        layoutCancelled = rootView.findViewById(R.id.llCancelled);
        layoutCancelled.setOnClickListener(this);
        layoutTransaction = rootView.findViewById(R.id.llTransaction);
        layoutTransaction.setOnClickListener(this);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onClick(View v) {

        if (v == layoutRequested) {

        } else if (v == layoutAssigned) {

        } else if (v == layoutOnGoing) {

        } else if (v == layoutCompleted) {

        } else if (v == layoutCancelled) {

        } else if (v == layoutTransaction) {

            Intent intent = new Intent(getActivity(), TransactionDetailsActivity.class);
            startActivity(intent);

        }

    }
}