package com.cscodetech.deliveryking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class CreateAccountActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_refercode)
    EditText edRefercode;


    @BindView(R.id.spinner)
    Spinner spinner;

    List<CountryCodeItem> cCodes = new ArrayList<>();
    String codeSelect;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("Test", "part");
            }
        });


        getCode();
    }

    @OnClick({R.id.img_back, R.id.btn_signup, R.id.btn_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_signup:
                if (validationCreate()) {
                    checkUser();

                }
                break;
            case R.id.btn_sign:
                finish();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    public boolean validationCreate() {
        if (TextUtils.isEmpty(edName.getText().toString())) {
            edName.setError("Enter Name");
            return false;
        }
        if (TextUtils.isEmpty(edEmail.getText().toString())) {
            edEmail.setError("Enter Email");
            return false;
        }
        if (TextUtils.isEmpty(edMobile.getText().toString())) {
            edMobile.setError("Enter Mobile");
            return false;
        }


        if (TextUtils.isEmpty(edPassword.getText().toString())) {
            edPassword.setError("Enter Password");
            return false;
        }
        return true;
    }

    public void showHidePass(View view) {

        if (view.getId() == R.id.show_pass_btn) {

            if (edPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eye_close);

                //Show Password
                edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eye);

                //Hide Password
                edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
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


    private void checkUser() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("ccode", codeSelect);
            jsonObject.put("mobile", edMobile.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().createUser(bodyRequest);
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
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {
                    Toast.makeText(CreateAccountActivity.this, "please check your number ", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User();
                    user.setName(edName.getText().toString());
                    user.setEmail(edEmail.getText().toString());
                    user.setCcode(codeSelect);
                    user.setMobile(edMobile.getText().toString());
                    user.setPassword(edPassword.getText().toString());
                    user.setRefercode(edRefercode.getText().toString());
                    sessionManager.setUserDetails("", user);
                    Utility.isvarification = 1;
                    startActivity(new Intent(this, VerifyPhoneActivity.class));
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
            e.toString();
        }
    }
}