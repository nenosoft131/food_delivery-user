package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestorentData {

	@SerializedName("Coupon")
	private List<CouponItem> coupon;

	@SerializedName("Product_Data")
	private List<ProductDataItem> productData;

	@SerializedName("restuarant_data")
	private List<RestDataItem> restuarantData;

	@SerializedName("Store_Product_Data")
	@Expose
	private List<StoreProductDatum> storeProductData = null;

	@SerializedName("Gallery_Data")
	@Expose
	private List<String> galleryData = null;
	@SerializedName("Review_Data")
	@Expose
	private List<ReviewDatum> reviewData = null;


	public List<CouponItem> getCoupon(){
		return coupon;
	}

	public List<ProductDataItem> getProductData(){
		return productData;
	}

	public List<RestDataItem> getRestuarantData(){
		return restuarantData;
	}


	public List<ReviewDatum> getReviewData() {
		return reviewData;
	}

	public void setReviewData(List<ReviewDatum> reviewData) {
		this.reviewData = reviewData;
	}

	public List<StoreProductDatum> getStoreProductData() {
		return storeProductData;
	}

	public void setStoreProductData(List<StoreProductDatum> storeProductData) {
		this.storeProductData = storeProductData;
	}
}