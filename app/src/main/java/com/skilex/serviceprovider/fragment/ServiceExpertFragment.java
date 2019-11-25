package com.skilex.serviceprovider.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.serviceperson.ServicePersonCreationActivity;
import com.skilex.serviceprovider.activity.serviceperson.ServicePersonStatusActivity;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.interfaces.DialogClickListener;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;
import com.skilex.serviceprovider.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

public class ServiceExpertFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = HomeFragment.class.getName();
    Context context;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    Handler mHandler = new Handler();
    private View rootView;
    private TextView next;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_service_expert, container, false);
        getActivity().setTitle(R.string.service_expert);
        next = rootView.findViewById(R.id.service_person_status);
        next.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.service_person_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_service_person:
                Intent intent = new Intent(getActivity(), ServicePersonCreationActivity.class);
                startActivity(intent);
                break;
        }
        return true;
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
        if(v == next) {
            Intent intent = new Intent(getActivity(), ServicePersonStatusActivity.class);
            startActivity(intent);
        }
    }
}
