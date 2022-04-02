package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

public class PackageCategoryItem{

	@SerializedName("status")
	private String catStatus;

	@SerializedName("img")
	private String catImg;

	@SerializedName("title")
	private String catName;

	@SerializedName("id")
	private String id;

	public String getCatStatus(){
		return catStatus;
	}

	public String getCatImg(){
		return catImg;
	}

	public String getCatName(){
		return catName;
	}

	public String getId(){
		return id;
	}
}