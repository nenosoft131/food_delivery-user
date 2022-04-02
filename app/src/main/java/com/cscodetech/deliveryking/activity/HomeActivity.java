package com.cscodetech.deliveryking.activity;

import static com.cscodetech.deliveryking.utility.SessionManager.login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.fregment.HomeFragment;
import com.cscodetech.deliveryking.model.MyAddress;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.txt_location)
    TextView txtLocation;

    public static HomeActivity homeActivity;

    public static HomeActivity getInstance() {
        return homeActivity;
    }

    SessionManager sessionManager;
    MyAddress myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        homeActivity = this;
        sessionManager = new SessionManager(HomeActivity.this);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        myAddress = new MyAddress();
        myAddress = sessionManager.getAddress();
        if (myAddress != null && myAddress.getAddress() != null) {
            txtLocation.setText("" + myAddress.getAddress());
        }

        openFragment(new HomeFragment());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    @OnClick({R.id.rlt_cart1, R.id.rlt_cart, R.id.lvl_changelocation, R.id.lvl_actionsearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_cart1:
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(this, MywalletActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));

                }
                break;
            case R.id.rlt_cart:
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(HomeActivity.this, ReferlActivity.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.lvl_actionsearch:
                startActivity(new Intent(HomeActivity.this, SearchRestorentActivity.class));
                break;
            case R.id.lvl_changelocation:
                startActivity(new Intent(HomeActivity.this, MapActivity.class));
                break;
            default:

                break;
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof HomeFragment && fragment.isVisible()) {
            finish();
        } else {
            super.onBackPressed();

        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                openFragment(new HomeFragment());
                return true;
            case R.id.navigation_sreach:
                startActivity(new Intent(HomeActivity.this, SearchRestorentActivity.class));
                return true;

            case R.id.navigation_orders:
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(HomeActivity.this, RestorentOrderActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));

                }
                return true;

            case R.id.navigation_user:
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(HomeActivity.this, AccountActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));

                }
                return true;


            default:
                return false;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}