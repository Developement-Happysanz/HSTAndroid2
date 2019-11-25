package com.skilex.serviceprovider.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.adapter.ExpertListAdapter;
import com.skilex.serviceprovider.bean.support.ServicePerson;
import com.skilex.serviceprovider.bean.support.ServicePersonList;
import com.skilex.serviceprovider.helper.AlertDialogHelper;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;
import com.skilex.serviceprovider.serviceinterfaces.IServiceListener;
import com.skilex.serviceprovider.utils.PreferenceStorage;
import com.skilex.serviceprovider.utils.SkilExConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.util.Log.d;

public class AcceptedFragment extends Fragment implements AdapterView.OnItemClickListener, IServiceListener {
    private View rootView;
    private static final String TAG = AcceptedFragment.class.getName();
    protected ListView loadMoreListView;
    protected ExpertListAdapter upcomingHolidayListAdapter;
    protected ArrayList<ServicePerson> upcomingHolidayArrayList;
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    protected boolean isLoadingForFirstTime = true;
    int pageNumber = 0, totalCount = 0;

    public AcceptedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);
        loadMoreListView = rootView.findViewById(R.id.serviceList);
        loadMoreListView.setOnItemClickListener(this);
        initializeEventHelpers();
        loadCat();
        return rootView;
    }

    private void loadCat() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SkilExConstants.USER_MASTER_ID, PreferenceStorage.getUserMasterId(getActivity()));
            jsonObject.put(SkilExConstants.EXPERT_STATUS, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = SkilExConstants.BUILD_URL + SkilExConstants.API_CHECK_SERVICE_PERSON_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    protected void initializeEventHelpers() {
        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());
        upcomingHolidayArrayList = new ArrayList<>();
    }

    private void LoadListView(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
//        loadMoreListView.onLoadMoreComplete();
        Gson gson = new Gson();
        ServicePersonList upcomingHolidayList = gson.fromJson(response.toString(), ServicePersonList.class);
        if (upcomingHolidayList != null) {
            Log.d(TAG, "fetched all event list count" + upcomingHolidayList.getCount());
        }
//        updateListAdapter(eventsList.getEvents());
        int totalNearbyCount = 0;
        if (upcomingHolidayList.getservicePersonArrayList() != null && upcomingHolidayList.getservicePersonArrayList().size() > 0) {


            isLoadingForFirstTime = false;
            totalCount = upcomingHolidayList.getCount();
            updateListAdapter(upcomingHolidayList.getservicePersonArrayList());
        }
    }

    protected void updateListAdapter(ArrayList<ServicePerson> upcomingHolidayArrayList) {
        this.upcomingHolidayArrayList.addAll(upcomingHolidayArrayList);
       /* if (mNoEventsFound != null)
            mNoEventsFound.setVisibility(View.GONE);*/

        if (upcomingHolidayListAdapter == null) {
            upcomingHolidayListAdapter = new ExpertListAdapter(getActivity(), this.upcomingHolidayArrayList);
            loadMoreListView.setAdapter(upcomingHolidayListAdapter);
        } else {
            upcomingHolidayListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInsuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(SkilExConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInsuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
                    } else {
                        signInsuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInsuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();

        if (validateSignInResponse(response)) {
            LoadListView(response);
        }
    }

    @Override
    public void onError(String error) {

    }
}
