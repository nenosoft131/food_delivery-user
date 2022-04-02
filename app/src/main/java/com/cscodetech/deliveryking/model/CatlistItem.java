package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

public class CatlistItem{

	@SerializedName("cat_img")
	private String catImg;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	public void setCatImg(String catImg){
		this.catImg = catImg;
	}

	public String getCatImg(){
		return catImg;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}
}