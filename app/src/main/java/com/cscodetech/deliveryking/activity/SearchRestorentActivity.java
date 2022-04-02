package com.cscodetech.deliveryking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.adepter.RestaurantsAllAdp;
import com.cscodetech.deliveryking.adepter.VendorAllAdp;
import com.cscodetech.deliveryking.model.MyAddress;
import com.cscodetech.deliveryking.model.RetorentList;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SearchRestorentActivity extends AppCompatActivity implements GetResult.MyListener,  RestaurantsAllAdp.RecyclerTouchListener, VendorAllAdp.RecyclerTouchListener {

    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar toolbar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.my_recycler_Restaurant)
    RecyclerView myRecyclerViewRestotaunt;
    @BindView(R.id.my_recycler_view_Store)
    RecyclerView myRecyclerViewStore;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;

    @BindView(R.id.txt_restorent)
    TextView txtRestorent;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.txt_store)
    TextView txtStore;
    @BindView(R.id.view2)
    View view2;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    MyAddress myAddress;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restorent);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(SearchRestorentActivity.this);
        custPrograssbar = new CustPrograssbar();
        myAddress = sessionManager.getAddress();
        user = sessionManager.getUserDetails("");
        LinearLayoutManager mLayoutManager13 = new LinearLayoutManager(this);
        mLayoutManager13.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager mLayoutManager1= new LinearLayoutManager(this);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);

        myRecyclerViewRestotaunt.setLayoutManager(mLayoutManager13);
        myRecyclerViewRestotaunt.setItemAnimator(new DefaultItemAnimator());

        myRecyclerViewStore.setLayoutManager(mLayoutManager1);
        myRecyclerViewStore.setItemAnimator(new DefaultItemAnimator());
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!edSearch.getText().toString().isEmpty()) {
                    getSearchRestorent(edSearch.getText().toString());
                }
                return true;
            }
            return false;
        });
        getSearchRestorent(" ");
    }

    private void getSearchRestorent(String keyword) {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());
            jsonObject.put("keyword", keyword);
            jsonObject.put("lats", myAddress.getLatMap());
            jsonObject.put("longs", myAddress.getLongMap());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getSearchRestorent(bodyRequest);
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
                RetorentList home = gson.fromJson(result.toString(), RetorentList.class);
                if (home.getRestuarantData().size() != 0) {
                    lvlNotfound.setVisibility(View.GONE);
                    myRecyclerViewRestotaunt.setVisibility(View.VISIBLE);
                    RestaurantsAllAdp adapter3 = new RestaurantsAllAdp(this, home.getRestuarantData(), this);
                    myRecyclerViewRestotaunt.setAdapter(adapter3);

                    VendorAllAdp itemAdpAllAdp = new VendorAllAdp(this, home.getStoreData(), this);
                    myRecyclerViewStore.setAdapter(itemAdpAllAdp);

                } else if(home.getStoreData().size()!=0){
                    VendorAllAdp itemAdpAllAdp = new VendorAllAdp(this, home.getStoreData(), this);
                    myRecyclerViewStore.setAdapter(itemAdpAllAdp);

                }else {
                    lvlNotfound.setVisibility(View.VISIBLE);
                    myRecyclerViewRestotaunt.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            Log.e("error", "" + e.toString());
        }
    }


    @Override
    public void RestaurantsAllAdp(String rid, int position) {
        startActivity(new Intent(this, RestaurantActivity.class)
                .putExtra("cid", "0")
                .putExtra("rid", rid));
    }
    @Override
    public void VendorAllAdp(String rid, int position) {
        startActivity(new Intent(this, VendorActivity.class).putExtra("cid", "0").putExtra("rid", rid));

    }
    @OnClick({R.id.img_back, R.id.img_search,R.id.txt_restorent,R.id.txt_store})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_search:
                if (!edSearch.getText().toString().isEmpty()) {
                    getSearchRestorent(edSearch.getText().toString());
                }
                break;
            case R.id.txt_restorent:
                myRecyclerViewRestotaunt.setVisibility(View.VISIBLE);
                myRecyclerViewStore.setVisibility(View.GONE);
                txtRestorent.setTextColor(getResources().getColor(R.color.colorPrimary));
                view1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                txtStore.setTextColor(getResources().getColor(R.color.colorgrey2));
                view2.setBackgroundColor(getResources().getColor(R.color.colorgrey2));

                break;
            case R.id.txt_store:
                myRecyclerViewRestotaunt.setVisibility(View.GONE);
                myRecyclerViewStore.setVisibility(View.VISIBLE);
                txtRestorent.setTextColor(getResources().getColor(R.color.colorgrey2));
                view1.setBackgroundColor(getResources().getColor(R.color.colorgrey2));
                txtStore.setTextColor(getResources().getColor(R.color.colorPrimary));
                view2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                break;
            default:
                break;
        }
    }



}