package com.cscodetech.deliveryking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.Contry;
import com.cscodetech.deliveryking.model.CountryCodeItem;
import com.cscodetech.deliveryking.model.RestResponse;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.cscodetech.deliveryking.utility.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ForgotActivity extends AppCompatActivity implements GetResult.MyListener {

    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.spinner)
    Spinner spinner;
    List<CountryCodeItem> cCodes = new ArrayList<>();
    String codeSelect;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; go home
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();

        sessionManager = new SessionManager(ForgotActivity.this);

        getCode();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("tem", parent.toString());
            }
        });
    }

    @OnClick({R.id.btn_login, R.id.img_back})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_login && validation()) {
            forgotPassword();
        }
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }

    private void getCode() {
        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCodelist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");
    }

    public boolean validation() {
        if (edMobile.getText().toString().isEmpty()) {
            edMobile.setError("Enter Mobile");
            return false;
        }
        return true;
    }

    private void forgotPassword() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edMobile.getText().toString());
            jsonObject.put("ccode", codeSelect);

            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getMobileCheck(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(ForgotActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            Log.e("result", "-->" + result);

            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {
                    Toast.makeText(ForgotActivity.this, "please check your number ", Toast.LENGTH_LONG).show();
                } else {
                    Utility.isvarification = 0;
                    User user = new User();
                    user.setCcode(codeSelect);
                    user.setMobile("" + edMobile.getText().toString());
                    sessionManager.setUserDetails("", user);
                    startActivity(new Intent(ForgotActivity.this, VerifyPhoneActivity.class));
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Contry contry = gson.fromJson(result.toString(), Contry.class);
                cCodes = contry.getCountryCode();
                List<String> list = new ArrayList<>();
                for (int i = 0; i < cCodes.size(); i++) {
                    if (cCodes.get(i).getStatus().equalsIgnoreCase("1")) {
                        list.add(cCodes.get(i).getCcode());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}