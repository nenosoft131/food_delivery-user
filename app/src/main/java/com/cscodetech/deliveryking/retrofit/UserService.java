package com.cscodetech.deliveryking.retrofit;


import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {

    @POST(APIClient.APPEND_URL + "m_mobile_check.php")
    Call<JsonObject> getMobileCheck(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_country_code.php")
    Call<JsonObject> getCodelist(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_reg_user.php")
    Call<JsonObject> createUser(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_login_user.php")
    Call<JsonObject> loginUser(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_address_user.php")
    Call<JsonObject> addressAdd(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_forget_password.php")
    Call<JsonObject> getForgot(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_home_data.php")
    Call<JsonObject> getHome(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_rest_section.php")
    Call<JsonObject> getRestorent(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_rest_data.php")
    Call<JsonObject> getRestoruntData(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_store_data.php")
    Call<JsonObject> getStoreData(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_cat_data.php")
    Call<JsonObject> getRestorant(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "s_cat_data.php")
    Call<JsonObject> getStore(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_cart_data.php")
    Call<JsonObject> getCartData(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_scart_data.php")
    Call<JsonObject> getCartStore(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_address_list.php")
    Call<JsonObject> addressList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_order_now.php")
    Call<JsonObject> getOrderNow(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "s_order_now.php")
    Call<JsonObject> getOrderNowStore(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_couponlist.php")
    Call<JsonObject> getCouponList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_check_coupon.php")
    Call<JsonObject> checkCoupon(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_order_history.php")
    Call<JsonObject> getOrderHistry(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_all_history.php")
    Call<JsonObject> getOrderHistryAll(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_order_information.php")
    Call<JsonObject> getOrderDetalis(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_map_info.php")
    Call<JsonObject> getOrderMaps(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "s_map_info.php")
    Call<JsonObject> getSOrderMaps(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_order_cancle.php")
    Call<JsonObject> orderCancel(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "s_order_history.php")
    Call<JsonObject> getOrderHistryStore(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_all_information.php")
    Call<JsonObject> getOrderDetalisStore(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "s_map_info.php")
    Call<JsonObject> getOrderMapsStore(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_add_delete.php")
    Call<JsonObject> removeAddress(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_rest_search.php")
    Call<JsonObject> getSearchRestorent(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_rest_product_search.php")
    Call<JsonObject> getSearchProduct(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "s_store_product_search.php")
    Call<JsonObject> getVendorSearchProduct(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_rate_data.php")
    Call<JsonObject> getRestorentRest(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "s_rate_data.php")
    Call<JsonObject> getStorer(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_rate_update.php")
    Call<JsonObject> getSendRates(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "s_rate_update.php")
    Call<JsonObject> getSendRatesstore(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_wallet_report.php")
    Call<JsonObject> walletReport(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_paymentgateway.php")
    Call<JsonObject> paymentlist(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_wallet_up.php")
    Call<JsonObject> walletUpdate(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_getdata.php")
    Call<JsonObject> getRefercode(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_pagelist.php")
    Call<JsonObject> getEpagelist(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "m_faq.php")
    Call<JsonObject> getFaq(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_pack_data.php")
    Call<JsonObject> getPackdata(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_paymentgateway.php")
    Call<JsonObject> getPaymentList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_pks_order.php")
    @Multipart
    Call<JsonObject> packData(@Part("uid") RequestBody uid,
                              @Part("p_method_id") RequestBody cid,
                              @Part("paddress") RequestBody paddress,
                              @Part("daddress") RequestBody daddress,
                              @Part("pmobile") RequestBody pmobile,
                              @Part("dmobile") RequestBody dmobile,
                              @Part("d_charge") RequestBody d_charge,
                              @Part("transaction_id") RequestBody transaction_id,
                              @Part("distance") RequestBody distance,
                              @Part("category") RequestBody category,
                              @Part("description") RequestBody description,
                              @Part("size") RequestBody size,
                              @Part List<MultipartBody.Part> parts);

    @POST(APIClient.APPEND_URL + "m_pkg_history.php")
    Call<JsonObject> getPkgOrder(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_p_order_cancle.php")
    Call<JsonObject> getOrderCancel(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_pkg_order_list.php")
    Call<JsonObject> getCuorierOrderHistory(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "m_profile_edit.php")
    Call<JsonObject> updateProfile(@Body RequestBody object);
}
