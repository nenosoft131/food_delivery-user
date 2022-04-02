package com.cscodetech.deliveryking.fregment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.activity.CourierActivity;
import com.cscodetech.deliveryking.activity.HomeActivity;
import com.cscodetech.deliveryking.activity.MapActivity;
import com.cscodetech.deliveryking.activity.RestaurantActivity;
import com.cscodetech.deliveryking.activity.VendorActivity;
import com.cscodetech.deliveryking.adepter.BannerAdapter;
import com.cscodetech.deliveryking.adepter.CategoryAdapter;
import com.cscodetech.deliveryking.adepter.RestaurantsAdp;
import com.cscodetech.deliveryking.adepter.VendorHomeAdapter;
import com.cscodetech.deliveryking.model.Home;
import com.cscodetech.deliveryking.model.MyAddress;
import com.cscodetech.deliveryking.model.RestuarantHomedataItem;
import com.cscodetech.deliveryking.model.StoreHomedataItem;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class HomeFragment extends Fragment implements CategoryAdapter.RecyclerTouchListener, RestaurantsAdp.RecyclerTouchListener, VendorHomeAdapter.RecyclerTouchListener, GetResult.MyListener {
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerBanner;
    @BindView(R.id.recycler_category)
    RecyclerView recyclerCategory;
    @BindView(R.id.restorentproduct)
    LinearLayout restorentproduct;
    @BindView(R.id.storeproduct)
    LinearLayout storeproduct;
    @BindView(R.id.recycler_restorent)
    RecyclerView recyclerRestorent;
    @BindView(R.id.recycler_vendor)
    RecyclerView recyclerVendor;

    LinearLayoutManager layoutManager;
    int position;
    Timer timer;
    TimerTask timerTask;
    User user;
    MyAddress myAddress;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        myAddress = sessionManager.getAddress();
        if (myAddress == null || myAddress.getLatMap() == null || myAddress.getLongMap() == null) {
            startActivity(new Intent(getActivity(), MapActivity.class));
        }
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecyclerBanner.setLayoutManager(layoutManager);
        setbanner();


        recyclerCategory.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerCategory.setItemAnimator(new DefaultItemAnimator());


        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerVendor.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerRestorent.setLayoutManager(layoutManager);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHome();
                pullToRefresh.setRefreshing(false);
            }
        });

        getHome();

        return view;
    }

    private void getHome() {

        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getHome(bodyRequest);
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
                Home home = gson.fromJson(result.toString(), Home.class);
                if (home.getResult().equalsIgnoreCase("true")) {

                    sessionManager.setIntData(SessionManager.istip, home.getHomeData().getMainData().getIsTip());
                    sessionManager.setStringData(SessionManager.tips, home.getHomeData().getMainData().getTip());

                    sessionManager.setIntData(SessionManager.istax, home.getHomeData().getMainData().getIsTax());
                    sessionManager.setStringData(SessionManager.taxs, home.getHomeData().getMainData().getTax());
                    sessionManager.setStringData(SessionManager.walletname, home.getHomeData().getMainData().getWname());

                    sessionManager.setStringData(SessionManager.currency, home.getHomeData().getMainData().getCurrency());

                    BannerAdapter bannerAdapter = new BannerAdapter(getActivity(), home.getHomeData().getBanner());
                    myRecyclerBanner.setAdapter(bannerAdapter);

                    CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), home.getHomeData().getCatlist(), this);
                    recyclerCategory.setAdapter(categoryAdapter);

                    setRestorentList(restorentproduct, home.getHomeData().getRestuarantHomedata());
                    setStoreList(storeproduct, home.getHomeData().getStoreHomedata());

                    VendorHomeAdapter vendorAdapter = new VendorHomeAdapter(getActivity(), home.getHomeData().getPopularStore(), this);
                    recyclerVendor.setAdapter(vendorAdapter);

                    RestaurantsAdp itemAdp = new RestaurantsAdp(getActivity(), home.getHomeData().getPopularRest(), this);
                    recyclerRestorent.setAdapter(itemAdp);

                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }

    }

    private void setRestorentList(LinearLayout lnrView, List<RestuarantHomedataItem> dataList) {
        lnrView.removeAllViews();
        for (int i = 0; i < dataList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_home_item, null);
            TextView itemTitle = view.findViewById(R.id.itemTitle);
            RecyclerView recyclerViewList = view.findViewById(R.id.recycler_view_list);
            itemTitle.setText(dataList.get(i).getHomeTitle());
            RestaurantsAdp itemAdp = new RestaurantsAdp(getActivity(), dataList.get(i).getRestData(), this);
            recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewList.setAdapter(itemAdp);
            lnrView.addView(view);
        }
    }

    private void setStoreList(LinearLayout lnrView, List<StoreHomedataItem> dataList) {
        lnrView.removeAllViews();
        for (int i = 0; i < dataList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_home_item, null);
            TextView itemTitle = view.findViewById(R.id.itemTitle);
            RecyclerView recyclerViewList = view.findViewById(R.id.recycler_view_list);
            itemTitle.setText(dataList.get(i).getHomeTitle());

            VendorHomeAdapter vendorAdapter = new VendorHomeAdapter(getActivity(), dataList.get(i).getRestData(), this);
            recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewList.setAdapter(vendorAdapter);
            lnrView.addView(view);
        }
    }

    @Override
    public void onClickCategoryItem(String cid, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("cid", cid);
        if (cid.equalsIgnoreCase("1")) {
            RestaurantFragment fragment = new RestaurantFragment();
            fragment.setArguments(bundle);
            HomeActivity.getInstance().openFragment(fragment);
        } else if (cid.equalsIgnoreCase("2") || cid.equalsIgnoreCase("4") || cid.equalsIgnoreCase("5")|| cid.equalsIgnoreCase("6")) {
            VendorFragment fragment = new VendorFragment();
            fragment.setArguments(bundle);
            HomeActivity.getInstance().openFragment(fragment);
        } else if (cid.equalsIgnoreCase("3")) {
            startActivity(new Intent(getActivity(), CourierActivity.class));
        }
    }

    @Override
    public void onRestaurantsItem(String rid, int position) {

        startActivity(new Intent(getActivity(), RestaurantActivity.class)
                .putExtra("cid", "0")
                .putExtra("rid", rid));

    }

    @Override
    public void onClickVendorItem(String rid, int position) {

        startActivity(new Intent(getActivity(), VendorActivity.class).putExtra("cid", "0").putExtra("rid", rid));

    }

    private void stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask.cancel();
            timer.cancel();
            timer = null;
            timerTask = null;
            position = layoutManager.findFirstCompletelyVisibleItemPosition();
        }
    }

    private void runAutoScrollBanner() {
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (position == myRecyclerBanner.getAdapter().getItemCount() - 1) {
                            position = 0;
                            myRecyclerBanner.smoothScrollBy(5, 0);
                            myRecyclerBanner.smoothScrollToPosition(position);

                        } else {
                            position++;
                            myRecyclerBanner.smoothScrollToPosition(position);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            };
            timer.schedule(timerTask, 4000, 4000);
        }

    }

    private void setbanner() {
        position = 0;
        myRecyclerBanner.scrollToPosition(position);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(myRecyclerBanner);
        myRecyclerBanner.smoothScrollBy(5, 0);
        myRecyclerBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1) {
                    stopAutoScrollBanner();
                } else if (newState == 0) {
                    position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    runAutoScrollBanner();
                }
            }
        });
    }


}