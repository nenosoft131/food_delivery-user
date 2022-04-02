package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("ResponseMsg")
    private String responseMsg;

    @SerializedName("OrderHistory")
    private List<OrderHistory> orderHistory;

    @SerializedName("Result")
    private String result;

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public String getResult() {
        return result;
    }
}