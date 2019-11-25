package com.skilex.serviceprovider.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.bean.support.Transaction;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;

import java.util.ArrayList;

public class TransactionListAdapter extends BaseAdapter {

    //    private final Transformation transformation;
    private Context context;
    private ArrayList<Transaction> services;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    Boolean click = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

//    RejectedFragment dsf = new RejectedFragment();

    public TransactionListAdapter(Context context, ArrayList<Transaction> services) {
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
        final TransactionListAdapter.ViewHolder holder;
//        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.trasanction_history_list_item, parent, false);

            holder = new TransactionListAdapter.ViewHolder();
            holder.txtTransactionDate = convertView.findViewById(R.id.trans_date);
            holder.txtTransactionCount = convertView.findViewById(R.id.trans_count);
            holder.txtTransactionAmt = convertView.findViewById(R.id.trans_amount);
            holder.payStatus = convertView.findViewById(R.id.pay_status);
            holder.to = convertView.findViewById(R.id.to_skilex_layout);
            holder.from = convertView.findViewById(R.id.from_skilex_layout);
            if(services.get(position).getPay_to_ser_provider_flag().equalsIgnoreCase("Yes")) {
                holder.to.setVisibility(View.VISIBLE);
                holder.from.setVisibility(View.GONE);
                if (services.get(position).getServ_prov_closing_status().equalsIgnoreCase("Paid")) {
                    holder.payStatus.setText("Paid");
                    holder.payStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.button_round_green_filled));
                } else {
                    holder.payStatus.setText("Pending");
                    holder.payStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.button_round_red_filled));
                }
            } else {
                holder.to.setVisibility(View.GONE);
                holder.from.setVisibility(View.VISIBLE);
                if (services.get(position).getServ_prov_closing_status().equalsIgnoreCase("Paid")) {
                    holder.payStatus.setText("Paid");
                    holder.payStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.button_round_green_filled));
                } else {
                    holder.payStatus.setText("Pending");
                    holder.payStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.button_round_red_filled));
                }
            }
//            if (PreferenceStorage.getLang(context).equalsIgnoreCase("tamil")) {
//                holder.txtTransactionDate.setText(services.get(position).getServiceCategoryMainNameTA());
//                holder.txtTransactionAmt.setText(services.get(position).getServiceNameTA());
//            } else {
            holder.txtTransactionDate.setText(services.get(position).getService_date());
            holder.txtTransactionCount.setText("Total services - " + services.get(position).getTotal_service_per_day());
            holder.txtTransactionAmt.setText("Total amount: ₹" + services.get(position).getServ_total_amount());
//            }
            holder.txtTransactionProviderComm = convertView.findViewById(R.id.self_amount);
            holder.txtTransactionProviderComm.setText("₹" + services.get(position).getServ_prov_commission_amt());
            holder.txtTransactionSkilexComm = convertView.findViewById(R.id.skilex_amount);
            holder.txtTransactionSkilexComm.setText("₹" + services.get(position).getSkilex_commission_amt());
            holder.fromSkilex = convertView.findViewById(R.id.from_skilex);
            holder.fromSkilex.setText("₹" + services.get(position).getOnline_serv_prov_commission());
            holder.toSkilex = convertView.findViewById(R.id.to_skilex);
            holder.toSkilex.setText("₹" + services.get(position).getOffline_skilex_commission());
            convertView.setTag(holder);

        } else {
            holder = (TransactionListAdapter.ViewHolder) convertView.getTag();

            holder.txtTransactionDate = convertView.findViewById(R.id.trans_date);
            holder.txtTransactionCount = convertView.findViewById(R.id.trans_count);
            holder.txtTransactionAmt = convertView.findViewById(R.id.trans_amount);
            holder.payStatus = convertView.findViewById(R.id.pay_status);
            holder.to = convertView.findViewById(R.id.to_skilex_layout);
            holder.from = convertView.findViewById(R.id.from_skilex_layout);
            if(services.get(position).getPay_to_ser_provider_flag().equalsIgnoreCase("Yes")) {
                holder.to.setVisibility(View.VISIBLE);
                holder.from.setVisibility(View.GONE);
                if (services.get(position).getServ_prov_closing_status().equalsIgnoreCase("Paid")) {
                    holder.payStatus.setText("Paid");
                    holder.payStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.button_round_green_filled));
                } else {
                    holder.payStatus.setText("Pending");
                    holder.payStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.button_round_red_filled));
                }
            } else {
                holder.to.setVisibility(View.GONE);
                holder.from.setVisibility(View.VISIBLE);
                if (services.get(position).getServ_prov_closing_status().equalsIgnoreCase("Paid")) {
                    holder.payStatus.setText("Paid");
                    holder.payStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.button_round_green_filled));
                } else {
                    holder.payStatus.setText("Pending");
                    holder.payStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.button_round_red_filled));
                }
            }
            /*if (PreferenceStorage.getLang(context).equalsIgnoreCase("tamil")) {
                holder.txtTransactionDate.setText(services.get(position).getServiceCategoryMainNameTA());
                holder.txtTransactionAmt.setText(services.get(position).getServiceNameTA());
            } else {*/
            holder.txtTransactionDate.setText(services.get(position).getService_date());
            holder.txtTransactionCount.setText("Total services - " + services.get(position).getTotal_service_per_day());
            holder.txtTransactionAmt.setText("Total amount: ₹" + services.get(position).getServ_total_amount());
//            }
            holder.txtTransactionProviderComm = convertView.findViewById(R.id.self_amount);
            holder.txtTransactionProviderComm.setText("₹" + services.get(position).getServ_prov_commission_amt());
            holder.txtTransactionSkilexComm = convertView.findViewById(R.id.skilex_amount);
            holder.txtTransactionSkilexComm.setText("₹" + services.get(position).getSkilex_commission_amt());
            holder.fromSkilex = convertView.findViewById(R.id.from_skilex);
            holder.fromSkilex.setText("₹" + services.get(position).getOnline_serv_prov_commission());
            holder.toSkilex = convertView.findViewById(R.id.to_skilex);
            holder.toSkilex.setText("₹" + services.get(position).getOffline_skilex_commission());
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
            String homeWorkTitle = services.get(i).getId();
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
        public TextView txtTransactionDate, txtTransactionCount, txtTransactionAmt, txtTransactionProviderComm,
                txtTransactionSkilexComm, fromSkilex, toSkilex, payStatus;
        LinearLayout to, from;
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