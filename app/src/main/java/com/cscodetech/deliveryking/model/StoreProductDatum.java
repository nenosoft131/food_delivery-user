package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreProductDatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("Menuitem_Data")
    @Expose
    private List<StoreDataItme> storeData = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<StoreDataItme> getStoreData() {
        return storeData;
    }

    public void setStoreData(List<StoreDataItme> menuitemData) {
        this.storeData = menuitemData;
    }

}