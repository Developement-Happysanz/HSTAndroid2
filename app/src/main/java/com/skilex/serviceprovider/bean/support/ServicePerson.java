package com.skilex.serviceprovider.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServicePerson implements Serializable {
    
    @SerializedName("id")
    @Expose
    private String service_person_id;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("phone_no")
    @Expose
    private String phone_no;

    @SerializedName("serv_pers_verify_status")
    @Expose
    private String serv_pers_verify_status;
    
    /**
     * @return The service_person_id
     */
    public String getService_person_id() {
        return service_person_id;
    }

    /**
     * @param service_person_id The service_person_id
     */
    public void setService_person_id(String service_person_id) {
        this.service_person_id = service_person_id;
    }

    /**
     * @return The full_name
     */
    public String getFull_name() {
        return full_name;
    }

    /**
     * @param full_name The full_name
     */
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    /**
     * @return The phone_no
     */
    public String getPhone_no() {
        return phone_no;
    }

    /**
     * @param phone_no The phone_no
     */
    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    /**
     * @return The serv_pers_verify_status
     */
    public String getServ_pers_verify_status() {
        return serv_pers_verify_status;
    }

    /**
     * @param serv_pers_verify_status The serv_pers_verify_status
     */
    public void setServ_pers_verify_status(String serv_pers_verify_status) {
        this.serv_pers_verify_status = serv_pers_verify_status;
    }
}
