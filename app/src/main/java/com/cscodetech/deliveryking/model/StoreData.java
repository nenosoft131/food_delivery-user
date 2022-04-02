package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreData {
    @SerializedName("restbanner")
    @Expose
    private List<BannerItem> restbanner = null;
    @SerializedName("restcat")
    @Expose
    private List<CatlistItem> restcat = null;
    @SerializedName("restuarant_data")
    @Expose
    private List<RestDataItem> restuarantData = null;
    @SerializedName("popular_restuarant")
    @Expose
    private List<RestDataItem> popularRestuarant = null;

    public List<BannerItem> getRestbanner() {
        return restbanner;
    }

    public void setRestbanner(List<BannerItem> restbanner) {
        this.restbanner = restbanner;
    }

    public List<CatlistItem> getRestcat() {
        return restcat;
    }

    public void setRestcat(List<CatlistItem> restcat) {
        this.restcat = restcat;
    }

    public List<RestDataItem> getRestuarantData() {
        return restuarantData;
    }

    public void setRestuarantData(List<RestDataItem> restuarantData) {
        this.restuarantData = restuarantData;
    }

    public List<RestDataItem> getPopularRestuarant() {
        return popularRestuarant;
    }

    public void setPopularRestuarant(List<RestDataItem> popularRestuarant) {
        this.popularRestuarant = popularRestuarant;
    }

}
