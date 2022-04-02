package com.cscodetech.deliveryking.fregment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.activity.HomeActivity;
import com.cscodetech.deliveryking.activity.RestaurantActivity;
import com.cscodetech.deliveryking.adepter.BannerAdapter;
import com.cscodetech.deliveryking.adepter.RestaurantsAdp;
import com.cscodetech.deliveryking.adepter.RestaurantsAllAdp;
import com.cscodetech.deliveryking.adepter.SubCategoryAdapter;
import com.cscodetech.deliveryking.model.MyAddress;
import com.cscodetech.deliveryking.model.Store;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class RestaurantFragment extends Fragment implements RestaurantsAdp.RecyclerTouchListener, RestaurantsAllAdp.RecyclerTouchListener, GetResult.MyListener, SubCategoryAdapter.RecyclerTouchListener {

    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerBanner;
    @BindView(R.id.my_category)
    RecyclerView myCategory;
    @BindView(R.id.recycler_vendor)
    RecyclerView recyclerRestorent;
    @BindView(R.id.recycler_vendorall)
    RecyclerView recyclerRestorentAll;
    String cid;

    LinearLayoutManager layoutManager;
    int position;
    Timer timer;
    TimerTask timerTask;


    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    MyAddress myAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        cid = bundle.getString("cid");

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        myAddress = sessionManager.getAddress();

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecyclerBanner.setLayoutManager(layoutManager);
        setbanner();

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myCategory.setLayoutManager(layoutManager);


        recyclerRestorent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        recyclerRestorentAll.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getStorelist();
        return view;
    }


    private void getStorelist() {

        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());
            jsonObject.put("cid", cid);
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getRestorent(bodyRequest);
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
                Store store = gson.fromJson(result.toString(), Store.class);
                if (store.getResult().equalsIgnoreCase("true")) {
                    BannerAdapter bannerAdapter = new BannerAdapter(getActivity(), store.getRestData().getRestbanner());
                    myRecyclerBanner.setAdapter(bannerAdapter);

                    SubCategoryAdapter categoryAdapter = new SubCategoryAdapter(getActivity(), store.getRestData().getRestcat(), this);
                    myCategory.setAdapter(categoryAdapter);

                    RestaurantsAdp itemAdp = new RestaurantsAdp(getActivity(), store.getRestData().getPopularRestuarant(), this);
                    recyclerRestorent.setAdapter(itemAdp);

                    RestaurantsAllAdp itemAdpAllAdp = new RestaurantsAllAdp(getActivity(), store.getRestData().getRestuarantData(), this);
                    recyclerRestorentAll.setAdapter(itemAdpAllAdp);

                }

            }
        } catch (Exception e) {
e.toString();
        }
    }

    @Override
    public void onClickCategoryItem(String cid, int position) {

        RestaurantListFragment fragment = new RestaurantListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid", cid);
        fragment.setArguments(bundle);
        HomeActivity.getInstance().openFragment(fragment);
    }

    @Override
    public void onRestaurantsItem(String rid, int position) {
        startActivity(new Intent(getActivity(), RestaurantActivity.class)
                .putExtra("cid", cid)
                .putExtra("rid", rid));

    }

    @Override
    public void RestaurantsAllAdp(String rid, int position) {
        startActivity(new Intent(getActivity(), RestaurantActivity.class)
                .putExtra("cid", cid)
                .putExtra("rid", rid));

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


}