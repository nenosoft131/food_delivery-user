package com.cscodetech.deliveryking.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.adepter.RestorentOrderAdp;
import com.cscodetech.deliveryking.fregment.OrderListFragment;
import com.cscodetech.deliveryking.model.Order;
import com.cscodetech.deliveryking.model.OrderHistoryItem;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.MyTabLayout;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RestorentOrderActivity extends AppCompatActivity implements GetResult.MyListener {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.my_order)
    ViewPager myOrder;
    @BindView(R.id.tab_layout)
    MyTabLayout tabLayout;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;

    public Order order;
    public static RestorentOrderActivity orderActivity;

    public static RestorentOrderActivity getInstance() {
        return orderActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restorent_order);
        ButterKnife.bind(this);
        orderActivity = this;
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();

        getOrders();
    }

    private void getOrders() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getOrderHistryAll(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }


    @OnClick({R.id.img_back})
    public void onClick(View view) {

        if (view.getId() == R.id.img_back) {
            finish();
        } else {
            throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    List<OrderHistoryItem> orderHistoryItems = new ArrayList<>();
    RestorentOrderAdp orderAdp;

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                order = gson.fromJson(result.toString(), Order.class);
                if (order.getResult().equalsIgnoreCase("true")) {
                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                    myOrder.setAdapter(adapter);
                    tabLayout.setupWithViewPager(myOrder);
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return OrderListFragment.newInstance(position, getIntent().getStringExtra("rid"));

        }

        @Override
        public int getCount() {
            return order.getOrderHistory().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return order.getOrderHistory().get(position).getTitle();

        }


    }

}