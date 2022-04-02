package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Store {
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ResponseMsg")
    @Expose
    private String responseMsg;
    @SerializedName("RestData")
    @Expose
    private StoreData restData;

    @SerializedName("restuarant_data")
    @Expose
    private List<RestDataItem> restuarantData = null;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public StoreData getRestData() {
        return restData;
    }

    public void setRestData(StoreData restData) {
        this.restData = restData;
    }

    public List<RestDataItem> getRestuarantData() {
        return restuarantData;
    }

    public void setRestuarantData(List<RestDataItem> restuarantData) {
        this.restuarantData = restuarantData;
    }
}
