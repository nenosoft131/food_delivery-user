package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackItem {

	@SerializedName("PriceData")
	private PriceData priceData;

	@SerializedName("Package_Category")
	private List<PackageCategoryItem> packageCategory;

	@SerializedName("Package_Banner")
	private List<Banner> packageBanner;

	public PriceData getPriceData(){
		return priceData;
	}

	public List<PackageCategoryItem> getPackageCategory(){
		return packageCategory;
	}

	public List<Banner> getPackageBanner(){
		return packageBanner;
	}
}