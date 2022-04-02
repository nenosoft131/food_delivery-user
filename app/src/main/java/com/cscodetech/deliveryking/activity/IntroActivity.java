package com.cscodetech.deliveryking.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.fregment.Info1Fragment;
import com.cscodetech.deliveryking.fregment.Info2Fragment;
import com.cscodetech.deliveryking.fregment.Info3Fragment;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.utility.AutoScrollViewPager;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.cscodetech.deliveryking.utility.Utility;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class IntroActivity extends AppCompatActivity {


    int selectPage = 0;
    SessionManager sessionManager;
    public static AutoScrollViewPager vpPager;
    MyPagerAdapter adapterViewPager;
    public static TextView btnNext;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        btnNext = findViewById(R.id.txt_next);
        vpPager = findViewById(R.id.vpPager);
        sessionManager = new SessionManager(IntroActivity.this);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        if (sessionManager.getBooleanData(SessionManager.intro)) {
            startActivity(new Intent(IntroActivity.this, HomeActivity.class));
            finish();
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utility.hasGPSDevice(IntroActivity.this)) {
                Toast.makeText(IntroActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
                Utility.enableLoc(IntroActivity.this);
            }
        }

        vpPager.setAdapter(adapterViewPager);
        DotsIndicator extensiblePageIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        extensiblePageIndicator.setViewPager(vpPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("data", "jsadlj");
            }

            @Override
            public void onPageSelected(int position) {
                selectPage = position;

                if (position == 0 || position == 1) {

                    btnNext.setText("Next");
                } else if (position == 2) {

                    btnNext.setText("Finish");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("sjlkj", "sjahdal");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.txt_next, R.id.txt_ignore})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_ignore:

                break;
            case R.id.txt_next:
                if (selectPage == 0) {
                    vpPager.setCurrentItem(1);
                } else if (selectPage == 1) {
                    vpPager.setCurrentItem(2);
                } else if (selectPage == 2) {
                    User user = new User();
                    user.setId("0");
                    user.setName("test");
                    sessionManager.setUserDetails("", user);
                    sessionManager.setBooleanData(SessionManager.intro, true);
                    startActivity(new Intent(IntroActivity.this, HomeActivity.class));
                    finish();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int numItems = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return numItems;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Info1Fragment.newInstance();
                case 1:
                    return Info2Fragment.newInstance();
                case 2:
                    return Info3Fragment.newInstance();
                default:
                    return null;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.e("page", "" + position);
            return "Page " + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            return fragment;
        }

    }


}
