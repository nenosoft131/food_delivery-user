package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistory {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("typeid")
    @Expose
    private String typeid;
    @SerializedName("history_data")
    @Expose
    private List<OrderHistoryItem> historyData = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OrderHistoryItem> getHistoryData() {
        return historyData;
    }

    public void setHistoryData(List<OrderHistoryItem> historyData) {
        this.historyData = historyData;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
}
