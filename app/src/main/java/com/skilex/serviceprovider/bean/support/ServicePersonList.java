package com.skilex.serviceprovider.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ServicePersonList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("applicant")
    @Expose
    private ArrayList<ServicePerson> servicePersonArrayList = new ArrayList<>();

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
     * @return The servicePersonArrayList
     */
    public ArrayList<ServicePerson> getservicePersonArrayList() {
        return servicePersonArrayList;
    }

    /**
     * @param servicePersonArrayList The servicePersonArrayList
     */
    public void setservicePersonArrayList(ArrayList<ServicePerson> servicePersonArrayList) {
        this.servicePersonArrayList = servicePersonArrayList;
    }
}
