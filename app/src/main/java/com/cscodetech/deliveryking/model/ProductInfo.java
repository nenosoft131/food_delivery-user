package com.cscodetech.deliveryking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductInfo {
    @SerializedName("attribute_id")
    @Expose
    private String attributeId;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("product_discount")
    @Expose
    private double productDiscount;
    @SerializedName("Product_Out_Stock")
    @Expose
    private String productOutStock;

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(double productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductOutStock() {
        return productOutStock;
    }

    public void setProductOutStock(String productOutStock) {
        this.productOutStock = productOutStock;
    }

}
