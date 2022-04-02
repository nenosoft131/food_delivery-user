package com.cscodetech.deliveryking.fregment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.activity.VendorActivity;
import com.cscodetech.deliveryking.adepter.VendorAllAdp;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class VendorListFragment extends Fragment implements GetResult.MyListener, VendorAllAdp.RecyclerTouchListener {

    @BindView(R.id.recycler_list)
    RecyclerView recyclerList;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    MyAddress myAddress;
    String cid;

    public VendorListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_vendor_list, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();

        cid = bundle.getString("cid");
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        myAddress = sessionManager.getAddress();
        custPrograssbar = new CustPrograssbar();
        recyclerList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
        Call<JsonObject> call = APIClient.getInterface().getStore(bodyRequest);
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
                    VendorAllAdp itemAdpAllAdp = new VendorAllAdp(getActivity(), store.getRestuarantData(), this);
                    recyclerList.setAdapter(itemAdpAllAdp);
                }
            }
        } catch (Exception e) {
            Log.e("Error->", "-->" + e.toString());
        }
    }


    @Override
    public void VendorAllAdp(String rid, int position) {

        startActivity(new Intent(getActivity(), VendorActivity.class).putExtra("cid", cid).putExtra("rid", rid));
    }
}