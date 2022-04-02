package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

public class Home{

	@SerializedName("HomeData")
	private HomeData homeData;

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public void setHomeData(HomeData homeData){
		this.homeData = homeData;
	}

	public HomeData getHomeData(){
		return homeData;
	}

	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}

	public void setResponseMsg(String responseMsg){
		this.responseMsg = responseMsg;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}
}