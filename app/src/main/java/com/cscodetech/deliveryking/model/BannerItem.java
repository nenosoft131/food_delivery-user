package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

public class BannerItem{

	@SerializedName("img")
	private String img;

	@SerializedName("id")
	private String id;

	public void setImg(String img){
		this.img = img;
	}

	public String getImg(){
		return img;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}