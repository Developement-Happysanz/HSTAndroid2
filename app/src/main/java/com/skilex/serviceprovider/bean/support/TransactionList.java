package com.skilex.serviceprovider.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TransactionList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("transactionResult")
    @Expose
    private ArrayList<Transaction> TransactionArrayList = new ArrayList<>();

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return The TransactionArrayList
     */
    public ArrayList<Transaction> getTransactionArrayList() {
        return TransactionArrayList;
    }

    /**
     * @param TransactionArrayList The TransactionArrayList
     */
    public void setTransactionArrayList(ArrayList<Transaction> TransactionArrayList) {
        this.TransactionArrayList = TransactionArrayList;
    }
}
