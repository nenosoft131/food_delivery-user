package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewDatum {

    @SerializedName("rest_review")
    @Expose
    private String restReview;
    @SerializedName("review_title")
    @Expose
    private String reviewTitle;
    @SerializedName("order_complete_date")
    @Expose
    private String orderCompleteDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order_items")
    @Expose
    private String orderItems;

    public String getRestReview() {
        return restReview;
    }

    public void setRestReview(String restReview) {
        this.restReview = restReview;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getOrderCompleteDate() {
        return orderCompleteDate;
    }

    public void setOrderCompleteDate(String orderCompleteDate) {
        this.orderCompleteDate = orderCompleteDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

}
