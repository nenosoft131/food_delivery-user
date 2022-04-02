package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestuarantHomedataItem{

	@SerializedName("home_title")
	private String homeTitle;

	@SerializedName("rest_data")
	private List<RestDataItem> restData;

	public void setHomeTitle(String homeTitle){
		this.homeTitle = homeTitle;
	}

	public String getHomeTitle(){
		return homeTitle;
	}

	public void setRestData(List<RestDataItem> restData){
		this.restData = restData;
	}

	public List<RestDataItem> getRestData(){
		return restData;
	}
}