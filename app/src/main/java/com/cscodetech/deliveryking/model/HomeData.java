package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeData{

	@SerializedName("wallet")
	private int wallet;

	@SerializedName("popular_rest")
	private List<RestDataItem> popularRest;

	@SerializedName("Main_Data")
	private MainData mainData;

	@SerializedName("Banner")
	private List<BannerItem> banner;

	@SerializedName("Catlist")
	private List<CatlistItem> catlist;

	@SerializedName("popular_store")
	private List<RestDataItem> popularStore;

	@SerializedName("store_homedata")
	private List<StoreHomedataItem> storeHomedata;

	@SerializedName("restuarant_homedata")
	private List<RestuarantHomedataItem> restuarantHomedata;

	public void setWallet(int wallet){
		this.wallet = wallet;
	}

	public int getWallet(){
		return wallet;
	}

	public void setPopularRest(List<RestDataItem> popularRest){
		this.popularRest = popularRest;
	}

	public List<RestDataItem> getPopularRest(){
		return popularRest;
	}

	public void setMainData(MainData mainData){
		this.mainData = mainData;
	}

	public MainData getMainData(){
		return mainData;
	}

	public void setBanner(List<BannerItem> banner){
		this.banner = banner;
	}

	public List<BannerItem> getBanner(){
		return banner;
	}

	public void setCatlist(List<CatlistItem> catlist){
		this.catlist = catlist;
	}

	public List<CatlistItem> getCatlist(){
		return catlist;
	}

	public void setPopularStore(List<RestDataItem> popularStore){
		this.popularStore = popularStore;
	}

	public List<RestDataItem> getPopularStore(){
		return popularStore;
	}

	public void setStoreHomedata(List<StoreHomedataItem> storeHomedata){
		this.storeHomedata = storeHomedata;
	}

	public List<StoreHomedataItem> getStoreHomedata(){
		return storeHomedata;
	}

	public void setRestuarantHomedata(List<RestuarantHomedataItem> restuarantHomedata){
		this.restuarantHomedata = restuarantHomedata;
	}

	public List<RestuarantHomedataItem> getRestuarantHomedata(){
		return restuarantHomedata;
	}
}