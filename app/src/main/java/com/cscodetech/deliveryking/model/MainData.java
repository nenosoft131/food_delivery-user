package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.SerializedName;

public class MainData{

	@SerializedName("pdboy")
	private String pdboy;

	@SerializedName("wname")
	private String wname;

	@SerializedName("one_key")
	private String oneKey;

	@SerializedName("timezone")
	private String timezone;

	@SerializedName("rcredit")
	private String rcredit;

	@SerializedName("webname")
	private String webname;

	@SerializedName("is_tip")
	private int isTip;

	@SerializedName("weblogo")
	private String weblogo;

	@SerializedName("d_key")
	private String dKey;

	@SerializedName("is_dmode")
	private String isDmode;

	@SerializedName("tax")
	private String tax;

	@SerializedName("one_hash")
	private String oneHash;

	@SerializedName("pstore")
	private String pstore;

	@SerializedName("is_tax")
	private int isTax;

	@SerializedName("d_hash")
	private String dHash;

	@SerializedName("currency")
	private String currency;

	@SerializedName("tip")
	private String tip;

	@SerializedName("id")
	private String id;

	@SerializedName("scredit")
	private String scredit;

	public void setPdboy(String pdboy){
		this.pdboy = pdboy;
	}

	public String getPdboy(){
		return pdboy;
	}

	public void setWname(String wname){
		this.wname = wname;
	}

	public String getWname(){
		return wname;
	}

	public void setOneKey(String oneKey){
		this.oneKey = oneKey;
	}

	public String getOneKey(){
		return oneKey;
	}

	public void setTimezone(String timezone){
		this.timezone = timezone;
	}

	public String getTimezone(){
		return timezone;
	}

	public void setRcredit(String rcredit){
		this.rcredit = rcredit;
	}

	public String getRcredit(){
		return rcredit;
	}

	public void setWebname(String webname){
		this.webname = webname;
	}

	public String getWebname(){
		return webname;
	}

	public void setIsTip(int isTip){
		this.isTip = isTip;
	}

	public int getIsTip(){
		return isTip;
	}

	public void setWeblogo(String weblogo){
		this.weblogo = weblogo;
	}

	public String getWeblogo(){
		return weblogo;
	}

	public void setDKey(String dKey){
		this.dKey = dKey;
	}

	public String getDKey(){
		return dKey;
	}

	public void setIsDmode(String isDmode){
		this.isDmode = isDmode;
	}

	public String getIsDmode(){
		return isDmode;
	}

	public void setTax(String tax){
		this.tax = tax;
	}

	public String getTax(){
		return tax;
	}

	public void setOneHash(String oneHash){
		this.oneHash = oneHash;
	}

	public String getOneHash(){
		return oneHash;
	}

	public void setPstore(String pstore){
		this.pstore = pstore;
	}

	public String getPstore(){
		return pstore;
	}

	public void setIsTax(int isTax){
		this.isTax = isTax;
	}

	public int getIsTax(){
		return isTax;
	}

	public void setDHash(String dHash){
		this.dHash = dHash;
	}

	public String getDHash(){
		return dHash;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setTip(String tip){
		this.tip = tip;
	}

	public String getTip(){
		return tip;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setScredit(String scredit){
		this.scredit = scredit;
	}

	public String getScredit(){
		return scredit;
	}
}