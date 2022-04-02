package com.cscodetech.deliveryking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.Help;
import com.cscodetech.deliveryking.model.Login;
import com.cscodetech.deliveryking.model.Pages;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AccountActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.recycler_product)
    RecyclerView recyclerProduct;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;

    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.txt_email)
    TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(AccountActivity.this);
        user = sessionManager.getUserDetails("");
        txtName.setText("" + user.getName());
        txtPhone.setText("" + user.getCcode() + user.getMobile());
        txtEmail.setText("" + user.getEmail());
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerProduct.setLayoutManager(mLayoutManager2);
        recyclerProduct.setItemAnimator(new DefaultItemAnimator());
        getPagelist();
    }

    private void getPagelist() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getEpagelist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @OnClick({R.id.img_back, R.id.txt_edit, R.id.txt_recentorder, R.id.lvl_address, R.id.lvl_wallet, R.id.lvl_refer, R.id.lvl_faq, R.id.lvl_logout, R.id.txt_cuorier})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_edit:
                editProfile();
                break;
            case R.id.txt_recentorder:
                startActivity(new Intent(this, RestorentOrderActivity.class));
                break;
            case R.id.lvl_address:
                startActivity(new Intent(this, AddressListActivity.class));
                break;
            case R.id.lvl_wallet:
                startActivity(new Intent(this, MywalletActivity.class));
                break;
            case R.id.lvl_refer:
                startActivity(new Intent(this, ReferlActivity.class));
                break;
            case R.id.lvl_faq:
                startActivity(new Intent(this, FaqActivity.class));
                break;
            case R.id.lvl_logout:
                sessionManager.logoutUser();
                finish();
                break;
            case R.id.txt_cuorier:
                startActivity(new Intent(this, CuoriarOrderActivity.class));
                break;
            default:

                break;
        }
    }

    public void editProfile() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.edit_profile, null);
        mBottomSheetDialog.setContentView(sheetView);
        TextView btnUpdate = sheetView.findViewById(R.id.btn_update);
        EditText edName = sheetView.findViewById(R.id.ed_name);
        TextView edEmail = sheetView.findViewById(R.id.ed_email);
        TextView edMobile = sheetView.findViewById(R.id.ed_mobile);
        EditText edPassword = sheetView.findViewById(R.id.ed_password);
        ImageView showPassBtn = sheetView.findViewById(R.id.show_pass_btn);
        edName.setText("" + user.getName());
        edMobile.setText("" + user.getCcode() + user.getMobile());
        edEmail.setText("" + user.getEmail());
        edPassword.setText("" + user.getPassword());

        btnUpdate.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            updateProfile(edName.getText().toString(), edPassword.getText().toString());
        });
        showPassBtn.setOnClickListener(v -> {
            if (edPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (v)).setImageResource(R.drawable.ic_eye_close);
                //Show Password
                edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (v)).setImageResource(R.drawable.ic_eye);

                //Hide Password
                edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }

        });


        mBottomSheetDialog.show();


    }

    private void updateProfile(String name, String password) {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("name", name);
            jsonObject.put("password", password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().updateProfile(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Help help = gson.fromJson(result.toString(), Help.class);
                if (help.getResult().equalsIgnoreCase("true")) {
                    recyclerProduct.setAdapter(new MyFaqAdepter(help.getPagelist()));
                }

            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Login loginUser = gson.fromJson(result.toString(), Login.class);
                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setUserDetails("", loginUser.getUserLogin());
                    recreate();
                }
            }
        } catch (Exception e) {
            e.toString();

        }
    }

    public class MyFaqAdepter extends RecyclerView.Adapter<MyFaqAdepter.ViewHolder> {
        private List<Pages> orderData;

        public MyFaqAdepter(List<Pages> orderData) {
            this.orderData = orderData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.halp_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            Log.e("position", "" + position);
            Pages order = orderData.get(position);
            holder.txtTital.setText("" + order.getTitle());

            holder.lvlClick.setOnClickListener(v -> startActivity(new Intent(AccountActivity.this, HelpDetailsActivity.class).putExtra("title", order.getTitle()).putExtra("desc", order.getDescription())));
        }

        @Override
        public int getItemCount() {
            return orderData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_tital)
            TextView txtTital;
            @BindView(R.id.lvl_click)
            LinearLayout lvlClick;


            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

}