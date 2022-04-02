package com.cscodetech.deliveryking.activity;

import static com.cscodetech.deliveryking.utility.SessionManager.currency;
import static com.cscodetech.deliveryking.utility.SessionManager.restid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.cscodetech.deliveryking.fregment.VendorProductFragment;
import com.cscodetech.deliveryking.model.CouponItem;
import com.cscodetech.deliveryking.model.MyAddress;
import com.cscodetech.deliveryking.model.RestDataItem;
import com.cscodetech.deliveryking.model.Restorent;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.DatabaseHelper;
import com.cscodetech.deliveryking.utility.MyCart;
import com.cscodetech.deliveryking.utility.MyTabLayout;
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

public class VendorActivity extends AppCompatActivity implements GetResult.MyListener {
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
    @BindView(R.id.txt_review)
    TextView txtReview;
    @BindView(R.id.txt_dist)
    TextView txtDist;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_itemtotal)
    TextView txtItemtotal;
    @BindView(R.id.linear_offers)
    LinearLayout linearOffers;
    @BindView(R.id.lvl_vegnonveg)
    LinearLayout lvlVegnonveg;
    @BindView(R.id.lvl_actionsearch)
    LinearLayout lvlActionsearch;
    @BindView(R.id.lvl_cart)
    CardView lvlCart;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.swich)
    SwitchCompat swich;
    @BindView(R.id.tab_layout)
    MyTabLayout tabLayout;
    @BindView(R.id.viewpager)
    androidx.viewpager.widget.ViewPager viewpager;


    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    MyAddress myAddress;
    public Restorent restorent = new Restorent();
    DatabaseHelper myHelper;
    public static VendorActivity activity;

    public static VendorActivity getInstance() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        myAddress = sessionManager.getAddress();
        activity = this;
        myHelper = new DatabaseHelper(VendorActivity.this);
        sessionManager.setStringData(restid, getIntent().getStringExtra("rid"));
        getStoreItem("0");


    }

    private void getStoreItem(String fid) {

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
        Call<JsonObject> call = APIClient.getInterface().getStoreData(bodyRequest);
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
                    if (restorent.getResultData().getRestuarantData().get(0).getRestIsVeg().equalsIgnoreCase("2")) {
                        lvlVegnonveg.setVisibility(View.GONE);

                    } else {
                        lvlVegnonveg.setVisibility(View.VISIBLE);
                        swich.setOnCheckedChangeListener((compoundButton, b) -> {
                            if (b) {
                                getStoreItem("1");
                            } else {
                                getStoreItem("0");
                            }
                        });
                    }
                    txtTitle.setText("" + restorent.getResultData().getRestuarantData().get(0).getRestTitle());
                    txtLadmark.setText("" + restorent.getResultData().getRestuarantData().get(0).getRestLandmark());
                    txtReview.setText("" + restorent.getResultData().getRestuarantData().get(0).getRestRating());
                    txtDist.setText("" + restorent.getResultData().getRestuarantData().get(0).getRestDistance());
                    cartview();

                }
            }

        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }
    }

    private void setCouponList(LinearLayout lnrView, List<CouponItem> orderData) {
        lnrView.removeAllViews();
        if (orderData != null && !orderData.isEmpty()) {
            for (int i = 0; i < orderData.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(VendorActivity.this);
                View view = inflater.inflate(R.layout.offers_item, null);
                TextView txt_title = view.findViewById(R.id.txt_title);
                txt_title.setText("" + orderData.get(i).getSubtitle());
                lnrView.addView(view);
            }

        } else {
            lnrView.setVisibility(View.GONE);
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return VendorProductFragment.newInstance(position, getIntent().getStringExtra("rid"));

        }

        @Override
        public int getCount() {
            return restorent.getResultData().getStoreProductData().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return restorent.getResultData().getStoreProductData().get(position).getTitle();

        }


    }

    public void cartview() {

        myHelper = new DatabaseHelper(this);
        List<MyCart> list = myHelper.getAllData();
        if (list.size() == 0) {
            lvlCart.setVisibility(View.GONE);
        } else {
            lvlCart.setVisibility(View.VISIBLE);
            double subtotal = 0;
            double dic = 0;
            for (int i = 0; i < list.size(); i++) {
                MyCart rModel = list.get(i);
                if (rModel.getDiscount() != 0) {
                    dic = (Double.parseDouble(rModel.getProductPrice()) * rModel.getDiscount()) / 100;
                    dic = Double.parseDouble(rModel.getProductPrice()) - dic;
                } else {
                    dic = Double.parseDouble(rModel.getProductPrice());
                }
                double temp = Integer.parseInt(rModel.getQty()) * dic;
                subtotal = subtotal + temp;
            }
            txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + new DecimalFormat("##.##").format(subtotal));
            txtItemtotal.setText(list.size() + " Item ");
        }

    }

    @OnClick({R.id.img_back, R.id.txt_viewcart, R.id.txt_info, R.id.lvl_actionsearch})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_viewcart:
                startActivity(new Intent(VendorActivity.this, CartActivityVendor.class));
                break;
            case R.id.lvl_actionsearch:
                startActivity(new Intent(VendorActivity.this, SearchVendorItemActivity.class).putExtra("rid", getIntent().getStringExtra("rid")));
                break;
            case R.id.txt_info:
                bottonRestorentDetails(VendorActivity.this, restorent.getResultData().getRestuarantData().get(0));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    public void bottonCardClear() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.crearcard_layout, null);
        mBottomSheetDialog.setContentView(sheetView);

        TextView txtCrear = sheetView.findViewById(R.id.txt_crear);
        TextView txtNo = sheetView.findViewById(R.id.txt_no);


        mBottomSheetDialog.show();

        txtCrear.setOnClickListener(v -> {
            myHelper.deleteCard();
            mBottomSheetDialog.cancel();
            cartview();

        });
        txtNo.setOnClickListener(v -> mBottomSheetDialog.cancel());
    }

    public void bottonRestorentDetails(Context context, RestDataItem dataItem) {


        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.store_layout, null);
        mBottomSheetDialog.setContentView(rootView);

        ImageView imageView = rootView.findViewById(R.id.imageView);
        TextView txtTitle = rootView.findViewById(R.id.txt_title);
        TextView txtAddress = rootView.findViewById(R.id.txt_address);
        TextView txtDistance = rootView.findViewById(R.id.txt_distance);
        TextView txtReview = rootView.findViewById(R.id.txt_review);
        TextView txtDesc = rootView.findViewById(R.id.txt_desc);

        TextView txtMobile = rootView.findViewById(R.id.txt_mobile);
        TextView txtRestaurentcharge = rootView.findViewById(R.id.txt_restaurentcharge);
        TextView txtDeliverycharge = rootView.findViewById(R.id.txt_deliverycharge);
        TextView txtMinimumorder = rootView.findViewById(R.id.txt_minimumorder);


        txtTitle.setText("" + dataItem.getRestTitle());
        txtAddress.setText("" + dataItem.getRestFullAddress());
        txtDistance.setText("" + dataItem.getRestDistance());
        txtReview.setText("" + dataItem.getRestRating());
        txtDesc.setText("" + dataItem.getRestSdesc());
        txtMobile.setText("" + dataItem.getMobile());
        txtRestaurentcharge.setText(sessionManager.getStringData(currency) + dataItem.getRestCharge());
        txtDeliverycharge.setText(sessionManager.getStringData(currency) + dataItem.getRestDcharge());
        txtMinimumorder.setText(sessionManager.getStringData(currency) + dataItem.getRestMorder());

        Glide.with(context).load(APIClient.baseUrl + "/" + dataItem.getRestImg()).thumbnail(Glide.with(context).load(R.drawable.emty)).into(imageView);
        mBottomSheetDialog.show();


    }

}