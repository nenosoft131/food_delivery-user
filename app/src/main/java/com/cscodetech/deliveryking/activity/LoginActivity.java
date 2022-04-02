package com.cscodetech.deliveryking.activity;

import static com.cscodetech.deliveryking.utility.SessionManager.login;
import static com.cscodetech.deliveryking.utility.SessionManager.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.Login;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.txt_forgotpassword)
    TextView txtForgotpassword;
    @BindView(R.id.btn_sign)
    TextView btnSign;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);

    }

    private void loginuser() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mobile", edUsername.getText().toString());
            jsonObject.put("password", edPassword.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().loginUser(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }


    @OnClick({R.id.img_back, R.id.btn_login, R.id.txt_forgotpassword, R.id.btn_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_login:
                loginuser();
                break;
            case R.id.txt_forgotpassword:
                startActivity(new Intent(this, ForgotActivity.class));
                break;
            case R.id.btn_sign:
                startActivity(new Intent(this, CreateAccountActivity.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
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

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Login loginUser = gson.fromJson(result.toString(), Login.class);
                Toast.makeText(this, loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setUserDetails("", loginUser.getUserLogin());
                    sessionManager.setIntData(wallet, Integer.parseInt(loginUser.getUserLogin().getWallet()));
                    sessionManager.setBooleanData(login, true);
                    OneSignal.sendTag("userid", loginUser.getUserLogin().getId());
                    startActivity(new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }
        } catch (Exception e) {
            Log.e("error-->", "-->" + e.toString());
        }
    }
}