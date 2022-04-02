package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

public class PopularStoreItem{

	@SerializedName("rest_charge")
	private String restCharge;

	@SerializedName("rest_dcharge")
	private String restDcharge;

	@SerializedName("rest_distance")
	private String restDistance;

	@SerializedName("rest_id")
	private String restId;

	@SerializedName("rest_img")
	private String restImg;

	@SerializedName("rest_title")
	private String restTitle;

	@SerializedName("rest_is_open")
	private String restIsOpen;

	@SerializedName("rest_rating")
	private String restRating;

	@SerializedName("rest_morder")
	private String restMorder;

	@SerializedName("rest_sdesc")
	private String restSdesc;

	@SerializedName("IS_FAVOURITE")
	private int iSFAVOURITE;

	@SerializedName("rest_is_veg")
	private String restIsVeg;

	@SerializedName("rest_full_address")
	private String restFullAddress;

	public void setRestCharge(String restCharge){
		this.restCharge = restCharge;
	}

	public String getRestCharge(){
		return restCharge;
	}

	public void setRestDcharge(String restDcharge){
		this.restDcharge = restDcharge;
	}

	public String getRestDcharge(){
		return restDcharge;
	}

	public void setRestDistance(String restDistance){
		this.restDistance = restDistance;
	}

	public String getRestDistance(){
		return restDistance;
	}

	public void setRestId(String restId){
		this.restId = restId;
	}

	public String getRestId(){
		return restId;
	}

	public void setRestImg(String restImg){
		this.restImg = restImg;
	}

	public String getRestImg(){
		return restImg;
	}

	public void setRestTitle(String restTitle){
		this.restTitle = restTitle;
	}

	public String getRestTitle(){
		return restTitle;
	}

	public void setRestIsOpen(String restIsOpen){
		this.restIsOpen = restIsOpen;
	}

	public String getRestIsOpen(){
		return restIsOpen;
	}

	public void setRestRating(String restRating){
		this.restRating = restRating;
	}

	public String getRestRating(){
		return restRating;
	}

	public void setRestMorder(String restMorder){
		this.restMorder = restMorder;
	}

	public String getRestMorder(){
		return restMorder;
	}

	public void setRestSdesc(String restSdesc){
		this.restSdesc = restSdesc;
	}

	public String getRestSdesc(){
		return restSdesc;
	}

	public void setISFAVOURITE(int iSFAVOURITE){
		this.iSFAVOURITE = iSFAVOURITE;
	}

	public int getISFAVOURITE(){
		return iSFAVOURITE;
	}

	public void setRestIsVeg(String restIsVeg){
		this.restIsVeg = restIsVeg;
	}

	public String getRestIsVeg(){
		return restIsVeg;
	}

	public void setRestFullAddress(String restFullAddress){
		this.restFullAddress = restFullAddress;
	}

	public String getRestFullAddress(){
		return restFullAddress;
	}
}