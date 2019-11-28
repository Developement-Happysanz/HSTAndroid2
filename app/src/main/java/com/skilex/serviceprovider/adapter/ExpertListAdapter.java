package com.skilex.serviceprovider.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.bean.support.ServicePerson;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;

import java.util.ArrayList;

public class ExpertListAdapter  extends BaseAdapter {

    //    private final Transformation transformation;
    private Context context;
    private ArrayList<ServicePerson> services;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    Boolean click = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

//    RejectedFragment dsf = new RejectedFragment();

    public ExpertListAdapter(Context context, ArrayList<ServicePerson> services) {
        this.context = context;
        this.services = services;
//        Collections.reverse(services);
//        transformation = new RoundedTransformationBuilder()
//                .cornerRadiusDp(0)
//                .oval(false)
//                .build();
        mSearching = false;
    }

    @Override
    public int getCount() {
        if (mSearching) {
            if (!mAnimateSearch) {
                mAnimateSearch = true;
            }
            return mValidSearchIndices.size();
        } else {
            return services.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return services.get(mValidSearchIndices.get(position));
        } else {
            return services.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ExpertListAdapter.ViewHolder holder;
//        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.service_person_list_item, parent, false);

            holder = new ExpertListAdapter.ViewHolder();
            holder.txtCatName = convertView.findViewById(R.id.service_person_name);
            holder.txtSubCatName = convertView.findViewById(R.id.service_person_number);
            holder.status = convertView.findViewById(R.id.service_person_status);
            holder.txtCatName.setText(services.get(position).getFull_name());
            holder.txtSubCatName.setText("Phone: " +services.get(position).getPhone_no());

            holder.txtCatName.setText(services.get(position).getFull_name());
            holder.txtSubCatName.setText(services.get(position).getPhone_no());
            if(services.get(position).getServ_pers_verify_status().equalsIgnoreCase("Approved")){
                holder.status.setVisibility(View.VISIBLE);
                holder.status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_servicebook_success));
            } else if (services.get(position).getServ_pers_verify_status().equalsIgnoreCase("Rejected")){
                holder.status.setVisibility(View.VISIBLE);
                holder.status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_servicebook_failed));
            } else {
                holder.status.setVisibility(View.GONE);
            }

            convertView.setTag(holder);

        } else {
            holder = (ExpertListAdapter.ViewHolder) convertView.getTag();

            holder.txtCatName = convertView.findViewById(R.id.service_person_name);
            holder.txtSubCatName = convertView.findViewById(R.id.service_person_number);
            /*if (PreferenceStorage.getLang(context).equalsIgnoreCase("tamil")) {
                holder.txtCatName.setText(services.get(position).getServiceCategoryMainNameTA());
                holder.txtSubCatName.setText(services.get(position).getServiceNameTA());
            } else {*/
            holder.txtCatName.setText(services.get(position).getFull_name());
            holder.txtSubCatName.setText(services.get(position).getPhone_no());
            if(services.get(position).getServ_pers_verify_status().equalsIgnoreCase("Approved")){
                holder.status.setVisibility(View.VISIBLE);
                holder.status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_servicebook_success));
            } else if (services.get(position).getServ_pers_verify_status().equalsIgnoreCase("Rejected")){
                holder.status.setVisibility(View.VISIBLE);
                holder.status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_servicebook_failed));
            } else {
                holder.status.setVisibility(View.GONE);
            }
        }

        if (mSearching) {
            position = mValidSearchIndices.get(position);

        } else {
            Log.d("Event List Adapter", "getview pos called" + position);
        }

        return convertView;
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("EventListAdapter", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < services.size(); i++) {
            String homeWorkTitle = services.get(i).getService_person_id();
            if ((homeWorkTitle != null) && !(homeWorkTitle.isEmpty())) {
                if (homeWorkTitle.toLowerCase().contains(eventName.toLowerCase())) {
                    mValidSearchIndices.add(i);
                }
            }
        }
        Log.d("Event List Adapter", "notify" + mValidSearchIndices.size());
    }

    public void exitSearch() {
        mSearching = false;
        mValidSearchIndices.clear();
        mAnimateSearch = false;
    }

    public void clearSearchFlag() {
        mSearching = false;
    }

    public class ViewHolder {
        public TextView txtCatName, txtSubCatName;
        public ImageView status;
    }

    public boolean ismSearching() {
        return mSearching;
    }

    public int getActualEventPos(int selectedSearchpos) {
        if (selectedSearchpos < mValidSearchIndices.size()) {
            return mValidSearchIndices.get(selectedSearchpos);
        } else {
            return 0;
        }
    }
}
