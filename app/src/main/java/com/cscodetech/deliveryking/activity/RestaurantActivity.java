package com.cscodetech.deliveryking.activity;

import static com.cscodetech.deliveryking.utility.SessionManager.currency;
import static com.cscodetech.deliveryking.utility.SessionManager.restid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.fregment.RestaurantProductFragment;
import com.cscodetech.deliveryking.model.CouponItem;
import com.cscodetech.deliveryking.model.MenuitemDataItem;
import com.cscodetech.deliveryking.model.MyAddress;
import com.cscodetech.deliveryking.model.RestDataItem;
import com.cscodetech.deliveryking.model.Restorent;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.Restaurent;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RestaurantActivity extends AppCompatActivity implements GetResult.MyListener {
    @BindView(R.id.rootLayout)
    androidx.coordinatorlayout.widget.CoordinatorLayout rootLayout;
    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar toolbar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.lvl_title)
    LinearLayout lvlTitle;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.txt_ladmark)
    TextView txtLadmark;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_dist)
    TextView txtDist;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_itemtotal)
    TextView txtItemtotal;
    @BindView(R.id.linear_offers)
    LinearLayout linearOffers;
    @BindView(R.id.lvl_actionsearch)
    LinearLayout lvlActionsearch;
    @BindView(R.id.lvl_cart)
    CardView lvlCart;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.swich)
    SwitchCompat swich;
    @BindView(R.id.tab_layout)
    com.cscodetech.deliveryking.utility.MyTabLayout tabLayout;
    @BindView(R.id.viewpager)
    androidx.viewpager.widget.ViewPager viewpager;


    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    MyAddress myAddress;
    public static RestaurantActivity activity;

    public static RestaurantActivity getInstance() {
        return activity;
    }

    public Restorent restorent = new Restorent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);
        activity = this;
        sessionManager = new SessionManager(RestaurantActivity.this);
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        myAddress = sessionManager.getAddress();
        sessionManager.setStringData(restid, getIntent().getStringExtra("rid"));

        swich.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                getRestorentItem("1");
            } else {
                getRestorentItem("0");
            }

        });

        getRestorentItem("0");
    }

    public void cartview() {

        Restaurent myHelper = new Restaurent(this);
        List<MenuitemDataItem> itemList = myHelper.getCData();
        if (itemList.size() == 0) {
            lvlCart.setVisibility(View.GONE);
        } else {
            lvlCart.setVisibility(View.VISIBLE);
            double total = 0;
            double aprice = 0;
            for (int i = 0; i < itemList.size(); i++) {
                MenuitemDataItem item = itemList.get(i);
                aprice = 0;
                if (item.getAddonPrice() != null) {
                    String[] separated = item.getAddonPrice().split(",");
                    for (String price : separated) {
                        aprice = aprice + Integer.parseInt(price);
                    }
                }
                double temptotal = aprice + item.getPrice();
                total = total + (temptotal * item.getQty());
            }

            txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + new DecimalFormat("##.##").format(total));
            txtItemtotal.setText(itemList.size() + " Item ");
        }


    }

    private void setCouponList(LinearLayout lnrView, List<CouponItem> orderData) {
        lnrView.removeAllViews();
        if (orderData != null && !orderData.isEmpty()) {
            for (int i = 0; i < orderData.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(RestaurantActivity.this);
                View view = inflater.inflate(R.layout.offers_item, null);
                TextView txt_title = view.findViewById(R.id.txt_title);
                txt_title.setText("" + orderData.get(i).getSubtitle());
                lnrView.addView(view);
            }

        } else {
            lnrView.setVisibility(View.GONE);
        }

    }

    private void getRestorentItem(String fid) {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());
            jsonObject.put("cid", getIntent().getStringExtra("cid"));
            jsonObject.put("rid", getIntent().getStringExtra("rid"));

            jsonObject.put("fid", fid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getRestoruntData(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {

            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                restorent = gson.fromJson(result.toString(), Restorent.class);
                if (restorent.getResult().equalsIgnoreCase("true")) {
                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                    viewpager.setAdapter(adapter);
                    tabLayout.setupWithViewPager(viewpager);
                    if (restorent.getResultData().getCoupon().size() != 0) {
                        setCouponList(linearOffers, restorent.getResultData().getCoupon());
                    } else {
                        linearOffers.setVisibility(View.GONE);
                    }
                    txtTitle.setText("" + restorent.getResultData().getRestuarantData().get(0).getRestTitle());
                    txtLadmark.setText("" + restorent.getResultData().getRestuarantData().get(0).getRestLandmark());
                    txtTime.setText("" + restorent.getResultData().getRestuarantData().get(0).getRestDeliverytime());
                    txtDist.setText("" + restorent.getResultData().getRestuarantData().get(0).getRestDistance());

                    cartview();

                }
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    public void bottonRestorentDetails(Context context, RestDataItem dataItem) {


        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.restorent_layout, null);
        mBottomSheetDialog.setContentView(rootView);

        ImageView imageView = rootView.findViewById(R.id.imageView);
        TextView txtTitle = rootView.findViewById(R.id.txt_title);
        TextView txtAddress = rootView.findViewById(R.id.txt_address);
        TextView txtDistance = rootView.findViewById(R.id.txt_distance);
        TextView txtReview = rootView.findViewById(R.id.txt_review);
        TextView txtDesc = rootView.findViewById(R.id.txt_desc);
        TextView txtDtime = rootView.findViewById(R.id.txt_dtime);
        TextView txtCostfortwo = rootView.findViewById(R.id.txt_costfortwo);
        TextView txtMobile = rootView.findViewById(R.id.txt_mobile);
        TextView txtRestaurentcharge = rootView.findViewById(R.id.txt_restaurentcharge);
        TextView txtDeliverycharge = rootView.findViewById(R.id.txt_deliverycharge);
        TextView txtMinimumorder = rootView.findViewById(R.id.txt_minimumorder);


        txtTitle.setText("" + dataItem.getRestTitle());
        txtAddress.setText("" + dataItem.getRestFullAddress());
        txtDistance.setText("" + dataItem.getRestDistance());
        txtReview.setText("" + dataItem.getRestRating());
        txtDesc.setText("" + dataItem.getRestSdesc());
        txtDtime.setText("" + dataItem.getRestDeliverytime());
        txtCostfortwo.setText(sessionManager.getStringData(currency) + dataItem.getRestCostfortwo());
        txtMobile.setText("" + dataItem.getMobile());
        txtRestaurentcharge.setText(sessionManager.getStringData(currency) + dataItem.getRestCharge());
        txtDeliverycharge.setText(sessionManager.getStringData(currency) + dataItem.getRestDcharge());
        txtMinimumorder.setText(sessionManager.getStringData(currency) + dataItem.getRestMorder());

        Glide.with(context).load(APIClient.baseUrl + "/" + dataItem.getRestImg()).thumbnail(Glide.with(context).load(R.drawable.emty)).into(imageView);

        mBottomSheetDialog.show();


    }


    class ViewPagerAdapter extends FragmentPagerAdapter {


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return RestaurantProductFragment.newInstance(position, getIntent().getStringExtra("rid"));

        }

        @Override
        public int getCount() {
            return restorent.getResultData().getProductData().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return restorent.getResultData().getProductData().get(position).getTitle();

        }
    }

    @OnClick({R.id.img_back, R.id.txt_viewcart, R.id.txt_info, R.id.lvl_actionsearch})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_info:
                bottonRestorentDetails(this, restorent.getResultData().getRestuarantData().get(0));
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_viewcart:
                startActivity(new Intent(RestaurantActivity.this, CartActivity.class));
                break;
            case R.id.lvl_actionsearch:
                startActivity(new Intent(RestaurantActivity.this, SearchProductActivity.class).putExtra("rid", getIntent().getStringExtra("rid")));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

}