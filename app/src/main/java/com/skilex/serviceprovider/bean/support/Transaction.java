package com.skilex.serviceprovider.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transaction implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("service_date")
    @Expose
    private String service_date;

    @SerializedName("total_service_per_day")
    @Expose
    private String total_service_per_day;

    @SerializedName("serv_total_amount")
    @Expose
    private String serv_total_amount;

    @SerializedName("serv_prov_commission_amt")
    @Expose
    private String serv_prov_commission_amt;

    @SerializedName("skilex_commission_amt")
    @Expose
    private String skilex_commission_amt;

    @SerializedName("online_transaction_amt")
    @Expose
    private String online_transaction_amt;

    @SerializedName("offline_transaction_amt")
    @Expose
    private String offline_transaction_amt;

    @SerializedName("taxable_amount")
    @Expose
    private String taxable_amount;

    @SerializedName("serv_prov_closing_status")
    @Expose
    private String serv_prov_closing_status;

    @SerializedName("online_skilex_commission")
    @Expose
    private String online_skilex_commission;

    @SerializedName("offline_skilex_commission")
    @Expose
    private String offline_skilex_commission;

    @SerializedName("online_serv_prov_commission")
    @Expose
    private String online_serv_prov_commission;

    @SerializedName("offline_serv_prov_commission")
    @Expose
    private String offline_serv_prov_commission;

    @SerializedName("pay_to_ser_provider_flag")
    @Expose
    private String pay_to_ser_provider_flag;

    @SerializedName("skilex_closing_status")
    @Expose
    private String skilex_closing_status;

    @SerializedName("pay_to_serv_prov")
    @Expose
    private String pay_to_serv_prov;


    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The service_date
     */
    public String getService_date() {
        return service_date;
    }

    /**
     * @param service_date The service_date
     */
    public void setService_date(String service_date) {
        this.service_date = service_date;
    }

    /**
     * @return The total_service_per_day
     */
    public String getTotal_service_per_day() {
        return total_service_per_day;
    }

    /**
     * @param total_service_per_day The total_service_per_day
     */
    public void setTotal_service_per_day(String total_service_per_day) {
        this.total_service_per_day = total_service_per_day;
    }

    /**
     * @return The serv_total_amount
     */
    public String getServ_total_amount() {
        return serv_total_amount;
    }

    /**
     * @param serv_total_amount The serv_total_amount
     */
    public void setServ_total_amount(String serv_total_amount) {
        this.serv_total_amount = serv_total_amount;
    }

    /**
     * @return The serv_prov_commission_amt
     */
    public String getServ_prov_commission_amt() {
        return serv_prov_commission_amt;
    }

    /**
     * @param serv_prov_commission_amt The serv_prov_commission_amt
     */
    public void setServ_prov_commission_amt(String serv_prov_commission_amt) {
        this.serv_prov_commission_amt = serv_prov_commission_amt;
    }

    /**
     * @return The skilex_commission_amt
     */
    public String getSkilex_commission_amt() {
        return skilex_commission_amt;
    }

    /**
     * @param skilex_commission_amt The skilex_commission_amt
     */
    public void setSkilex_commission_amt(String skilex_commission_amt) {
        this.skilex_commission_amt = skilex_commission_amt;
    }

    /**
     * @return The online_transaction_amt
     */
    public String getOnline_transaction_amt() {
        return online_transaction_amt;
    }

    /**
     * @param online_transaction_amt The online_transaction_amt
     */
    public void setOnline_transaction_amt(String online_transaction_amt) {
        this.online_transaction_amt = online_transaction_amt;
    }

    /**
     * @return The offline_transaction_amt
     */
    public String getOffline_transaction_amt() {
        return offline_transaction_amt;
    }

    /**
     * @param offline_transaction_amt The offline_transaction_amt
     */
    public void setOffline_transaction_amt(String offline_transaction_amt) {
        this.offline_transaction_amt = offline_transaction_amt;
    }

    /**
     * @return The taxable_amount
     */
    public String getTaxable_amount() {
        return taxable_amount;
    }

    /**
     * @param taxable_amount The taxable_amount
     */
    public void setTaxable_amount(String taxable_amount) {
        this.taxable_amount = taxable_amount;
    }

    /**
     * @return The online_skilex_commission
     */
    public String getOnline_skilex_commission() {
        return online_skilex_commission;
    }

    /**
     * @param online_skilex_commission The online_skilex_commission
     */
    public void setOnline_skilex_commission(String online_skilex_commission) {
        this.online_skilex_commission = online_skilex_commission;
    }

    /**
     * @return The offline_skilex_commission
     */
    public String getOffline_skilex_commission() {
        return offline_skilex_commission;
    }

    /**
     * @param offline_skilex_commission The offline_skilex_commission
     */
    public void setOffline_skilex_commission(String offline_skilex_commission) {
        this.offline_skilex_commission = offline_skilex_commission;
    }
    /**
     * @return The online_serv_prov_commission
     */
    public String getOnline_serv_prov_commission() {
        return online_serv_prov_commission;
    }

    /**
     * @param online_serv_prov_commission The online_serv_prov_commission
     */
    public void setOnline_serv_prov_commission(String online_serv_prov_commission) {
        this.online_serv_prov_commission = online_serv_prov_commission;
    }

    /**
     * @return The offline_serv_prov_commission
     */
    public String getOffline_serv_prov_commission() {
        return offline_serv_prov_commission;
    }

    /**
     * @param offline_serv_prov_commission The offline_serv_prov_commission
     */
    public void setOffline_serv_prov_commission(String offline_serv_prov_commission) {
        this.offline_serv_prov_commission = offline_serv_prov_commission;
    }

    /**
     * @return The serv_prov_closing_status
     */
    public String getPay_to_ser_provider_flag() {
        return pay_to_ser_provider_flag;
    }

    /**
     * @param pay_to_ser_provider_flag The pay_to_ser_provider_flag
     */
    public void setPay_to_ser_provider_flag(String pay_to_ser_provider_flag) {
        this.pay_to_ser_provider_flag = pay_to_ser_provider_flag;
    }

    /**
     * @return The skilex_closing_status
     */
    public String getSkilex_closing_status() {
        return skilex_closing_status;
    }

    /**
     * @param skilex_closing_status The skilex_closing_status
     */
    public void setSkilex_closing_status(String skilex_closing_status) {
        this.skilex_closing_status = skilex_closing_status;
    }

    /**
     * @return The serv_prov_closing_status
     */
    public String getServ_prov_closing_status() {
        return serv_prov_closing_status;
    }

    /**
     * @param serv_prov_closing_status The serv_prov_closing_status
     */
    public void setServ_prov_closing_status(String serv_prov_closing_status) {
        this.serv_prov_closing_status = serv_prov_closing_status;
    }

    /**
     * @return The pay_to_serv_prov
     */
    public String getPay_to_serv_prov() {
        return pay_to_serv_prov;
    }

    /**
     * @param pay_to_serv_prov The pay_to_serv_prov
     */
    public void setPay_to_serv_prov(String pay_to_serv_prov) {
        this.pay_to_serv_prov = pay_to_serv_prov;
    }
}