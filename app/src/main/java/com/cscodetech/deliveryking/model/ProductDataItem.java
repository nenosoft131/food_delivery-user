package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDataItem{

	@SerializedName("Menuitem_Data")
	private List<MenuitemDataItem> menuitemData;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	public List<MenuitemDataItem> getMenuitemData(){
		return menuitemData;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}
}