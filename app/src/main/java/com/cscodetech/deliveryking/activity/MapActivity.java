package com.cscodetech.deliveryking.activity;

import static android.os.Build.VERSION.SDK_INT;
import static com.cscodetech.deliveryking.utility.SessionManager.login;
import static com.cscodetech.deliveryking.utility.Utility.newAddress;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.Home;
import com.cscodetech.deliveryking.model.MyAddress;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.cscodetech.deliveryking.utility.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GetResult.MyListener {

    @BindView(R.id.layoutMap)
    LinearLayout layoutMap;

    @BindView(R.id.locationMarker)
    LinearLayout locationMarker;
    @BindView(R.id.locationMarkertext)
    LinearLayout locationMarkertext;

    @BindView(R.id.imageMarker)
    ImageView imageMarker;
    @BindView(R.id.txt_society)
    TextView txtSociety;
    @BindView(R.id.txt_address)
    TextView txtAddress;

    @BindView(R.id.btn_location)
    TextView btnLocation;
    GoogleMap mMap;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;

    String userAddress = "";

    String userid = "0";
    String newuser = "user";
    String aid = "0";
    String atype = "Home";
    String hno = "";
    String landmark = "";


    double mLatitude = 0.0;
    double mLongitude = 0.0;

    double currentLatitude;
    double currentLongitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    Bundle addressBundle;
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    List<AsyncTask> filterTaskList = new ArrayList<>();
    boolean isZooming = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        custPrograssbar = new CustPrograssbar();
        ButterKnife.bind(this);
        sessionManager = new SessionManager(MapActivity.this);
        user = sessionManager.getUserDetails("");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        custPrograssbar.prograssCreate(MapActivity.this);
        addressBundle = new Bundle();
        fusedLocationProviderClient = getFusedLocationProviderClient(this);
        getLocationRequest();
        showCurrentLocationOnMap();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }
                Log.e("lat", "-->" + locationResult.getLocations().get(0).getLatitude());
                Log.e("Lon", "-->" + locationResult.getLocations().get(0).getLongitude());
                //Location fetched, update listener can be removed
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };


        Intent i = getIntent();
        if (i != null) {
            Bundle extras = i.getExtras();
            if (extras != null) {

                hno = getIntent().getStringExtra("hno");
                landmark = getIntent().getStringExtra("landmark");
                userid = getIntent().getStringExtra("userid");
                newuser = getIntent().getStringExtra("newuser");
                aid = getIntent().getStringExtra("aid");
                atype = getIntent().getStringExtra("atype");
                atype = getIntent().getStringExtra("atype");

                mLatitude = getIntent().getDoubleExtra("lat", 0);
                mLongitude = getIntent().getDoubleExtra("long", 0);

            }
        }
        if (savedInstanceState != null) {
            mLatitude = savedInstanceState.getDouble("latitude");
            mLongitude = savedInstanceState.getDouble("longitude");
            userAddress = savedInstanceState.getString("userAddress");
            currentLatitude = savedInstanceState.getDouble("currentLatitude");
            currentLongitude = savedInstanceState.getDouble("currentLongitude");
        }
    }

    private void getLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (mMap.isIndoorEnabled()) {
            mMap.setIndoorEnabled(false);
        }
        custPrograssbar.closePrograssBar();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMap.clear();

            }
        });

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mLatitude = latLng.latitude;
            mLongitude = latLng.longitude;
            Log.e("latlng", latLng + "");
            isZooming = true;
            onLocationChanged((Location) null);
            getAddressByGeoCodingLatLng();

        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.e("TAG", mMap.getCameraPosition().target.toString());
                CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(28.2734, 76.1401));

// moves camera to coordinates
                mMap.moveCamera(point);
// animates camera to coordinates
                mMap.animateCamera(point);

            }
        });


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Log.e("cc", "curr " + mMap.getCameraPosition().target);
                if (latLng != null && latLng.latitude != 0.0 && latLng.longitude != 0.0) {
                    mMap.clear();
                    GetAddressFromLatLng asyncTask = new GetAddressFromLatLng();
                    asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, latLng.latitude, latLng.longitude);
                } else {
                    if (SDK_INT == Build.VERSION_CODES.R) {
                        LocationManager systemService = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        systemService.getCurrentLocation(LocationManager.NETWORK_PROVIDER, null, getMainExecutor(), (Consumer<Location>) (locationCallback -> {
                            Log.e("cc", "currLong: " + locationCallback.getLongitude());
                            LatLng latLng1 = new LatLng(locationCallback.getLatitude(), locationCallback.getLongitude());
                            GetAddressFromLatLng asyncTask = new GetAddressFromLatLng();
                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, latLng1.latitude, latLng1.longitude);

                        }));
                    }

                }

            }


        });


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null && location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
            onLocationChanged(location);
        } else {
            LocationManager systemService = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (SDK_INT == Build.VERSION_CODES.R) {
                systemService.getCurrentLocation(LocationManager.NETWORK_PROVIDER, null, getMainExecutor(), (Consumer<Location>) (locationCallback -> {
                    Log.e("cc", "currLong: " + locationCallback.getLongitude());
                    onLocationChanged(locationCallback);

                }));
                locationManager.requestLocationUpdates(provider, 20000, 0, this);
            } else {
                Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
                lastLocation.addOnSuccessListener(this, location1 -> {
                    if (location1 != null) {
                        mMap.clear();

                        //Go to Current Location
                        mLatitude = location1.getLatitude();
                        mLongitude = location1.getLongitude();
                        onLocationChanged(location1);
                        filterTaskList.clear();
                        GetAddressFromLatLng asyncTask = new GetAddressFromLatLng();
                        filterTaskList.add(asyncTask);
                        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mLatitude, mLongitude);

                    } else {
                        //Gps not enabled if loc is null
                        Utility.enableLoc(this);
                        Toast.makeText(this, "Location not Available", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(28.2734, 76.1401));

// moves camera to coordinates
        mMap.moveCamera(point);
// animates camera to coordinates
        mMap.animateCamera(point);
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude;
        double longitude;
        if (mLatitude != 0.0 && mLongitude != 0.0) {
            latitude = mLatitude;
            longitude = mLongitude;
        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        Log.e("test", "-->");
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }


    @OnClick({R.id.btn_location, R.id.img_back})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;
            case R.id.img_current:
                showCurrentLocationOnMap();
                break;
            case R.id.btn_location:
                if (sessionManager.getBooleanData(login) && newAddress == 1) {
                    bottonAddressadd();
                } else {
                    LatLng latLng = mMap.getCameraPosition().target;
                    MyAddress myAddress = new MyAddress();
                    myAddress.setLandmark(txtSociety.getText().toString());
                    myAddress.setAddress(txtAddress.getText().toString());
                    myAddress.setLatMap(String.valueOf(latLng.latitude));
                    myAddress.setLongMap(String.valueOf(latLng.longitude));
                    sessionManager.setAddress(myAddress);
                    startActivity(new Intent(MapActivity.this, HomeActivity.class));

                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }


    public void bottonAddressadd() {
        LatLng latLng = mMap.getCameraPosition().target;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View rootView = getLayoutInflater().inflate(R.layout.address_add, null);
        mBottomSheetDialog.setContentView(rootView);

        TextView txtSociety = rootView.findViewById(R.id.txt_society);
        TextView txtFullaadress = rootView.findViewById(R.id.txt_fullaadress);
        EditText edFlat = rootView.findViewById(R.id.ed_flat);
        EditText edAparment = rootView.findViewById(R.id.ed_aparment);
        TextView txtHome = rootView.findViewById(R.id.txt_home);
        TextView txtOffice = rootView.findViewById(R.id.txt_office);
        TextView txtAther = rootView.findViewById(R.id.txt_ather);
        TextView btnLocation = rootView.findViewById(R.id.btn_location);

        txtSociety.setText(this.txtSociety.getText().toString());
        txtFullaadress.setText(this.txtAddress.getText().toString());

        if (atype.equalsIgnoreCase("Home")) {
            txtHome.performClick();
        } else if (atype.equalsIgnoreCase("Office")) {
            txtOffice.performClick();
        } else if (atype.equalsIgnoreCase("Other")) {
            txtOffice.performClick();
        }

        txtHome.setOnClickListener(v -> {

            txtHome.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            txtOffice.setBackground(getResources().getDrawable(R.drawable.box));
            txtAther.setBackground(getResources().getDrawable(R.drawable.box));
            txtAther.setTextColor(getResources().getColor(R.color.black));
            txtOffice.setTextColor(getResources().getColor(R.color.black));
            txtHome.setTextColor(getResources().getColor(R.color.white));
            atype = "Home";

        });
        txtOffice.setOnClickListener(v -> {
            txtOffice.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            txtAther.setBackground(getResources().getDrawable(R.drawable.box));
            txtHome.setBackground(getResources().getDrawable(R.drawable.box));
            txtAther.setTextColor(getResources().getColor(R.color.black));
            txtHome.setTextColor(getResources().getColor(R.color.black));
            txtOffice.setTextColor(getResources().getColor(R.color.white));
            atype = "Office";
        });

        txtAther.setOnClickListener(v -> {
            txtAther.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            txtHome.setBackground(getResources().getDrawable(R.drawable.box));
            txtOffice.setBackground(getResources().getDrawable(R.drawable.box));
            txtHome.setTextColor(getResources().getColor(R.color.black));
            txtOffice.setTextColor(getResources().getColor(R.color.black));
            txtAther.setTextColor(getResources().getColor(R.color.white));
            atype = "Other";
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edFlat.getText().toString())) {
                    edFlat.setError("Enter House");
                    return;
                }

                if (TextUtils.isEmpty(edAparment.getText().toString())) {
                    edAparment.setError("Enter Apartment or Building or Landmark");
                    return;
                }

                custPrograssbar.prograssCreate(MapActivity.this);
                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("uid", user.getId());
                    jsonObject.put("address", txtFullaadress.getText().toString());
                    jsonObject.put("houseno", edFlat.getText().toString());
                    jsonObject.put("apartment", edAparment.getText().toString());
                    jsonObject.put("type", atype);
                    jsonObject.put("lat_map", latLng.latitude);
                    jsonObject.put("long_map", latLng.longitude);
                    jsonObject.put("aid", aid);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Call<JsonObject> call = APIClient.getInterface().addressAdd(bodyRequest);
                GetResult getResult = new GetResult();
                getResult.setMyListener(MapActivity.this::callback);
                getResult.callForLogin(call, "1");

            }
        });

        mBottomSheetDialog.show();


    }


    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Home home = gson.fromJson(result.toString(), Home.class);
                if (home.getResult().equalsIgnoreCase("true")) {

                    finish();
                }
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    private void showCurrentLocationOnMap() {

        if (checkAndRequestPermissions()) {
            @SuppressLint("MissingPermission")
            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
            lastLocation.addOnSuccessListener(this, location -> {
                if (location != null) {
                    mMap.clear();

                    //Go to Current Location
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();


                    getAddressByGeoCodingLatLng();
                } else {
                    //Gps not enabled if loc is null
                    Utility.enableLoc(this);
                    Toast.makeText(this, "Location not Available", Toast.LENGTH_SHORT).show();

                }
            });
            lastLocation.addOnFailureListener(e -> {
                Log.e("eror-->", "" + e.toString());
                //If perm provided then gps not enabled
                Toast.makeText(this, "Location Not Availabe", Toast.LENGTH_SHORT).show();

            });

        }


    }

    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermision = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarsePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 2);
            return false;
        }

        return true;

    }

    private void getAddressByGeoCodingLatLng() {

        //Get string address by geo coding from lat long
        if (mLatitude != 0 && mLongitude != 0) {

            if (Utility.popupWindow != null && Utility.popupWindow.isShowing()) {
                Utility.hideProgress();
            }

            Log.d("TAG", "getAddressByGeoCodingLatLng: START");
            //Cancel previous tasks and launch this one
            for (AsyncTask prevTask : filterTaskList) {
                prevTask.cancel(true);
            }

            filterTaskList.clear();
            GetAddressFromLatLng asyncTask = new GetAddressFromLatLng();
            filterTaskList.add(asyncTask);
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mLatitude, mLongitude);
        }
    }

    private class GetAddressFromLatLng extends AsyncTask<Double, Void, Bundle> {
        Double latitude;
        Double longitude;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(MapActivity.this);
        }

        @Override
        protected Bundle doInBackground(Double... doubles) {
            try {
                Utility.hideProgress();
                latitude = doubles[0];
                longitude = doubles[1];
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                StringBuilder sb = new StringBuilder();

                //get location from lat long if address string is null
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {

                    String address = addresses.get(0).getSubLocality();
                    if (address != null) {
                        addressBundle.putString("addressline2", address);

                    } else {
                        String address1 = addresses.get(0).getFeatureName();
                        addressBundle.putString("addressline2", address1);

                    }


                    String addline1 = addresses.get(0).getAddressLine(0);
                    if (addline1 != null)
                        sb.append(addline1).append(" ");


                    String city = addresses.get(0).getLocality();
                    if (city != null)
                        addressBundle.putString("city", city);
                    sb.append(city).append(" ");


                    String state = addresses.get(0).getAdminArea();
                    if (state != null)
                        addressBundle.putString("state", state);
                    sb.append(state).append(" ");


                    String country = addresses.get(0).getCountryName();
                    if (country != null)
                        addressBundle.putString("country", country);
                    sb.append(country).append(" ");

                    String postalCode = addresses.get(0).getPostalCode();
                    if (postalCode != null)
                        addressBundle.putString("postalcode", postalCode);
                    sb.append(postalCode).append(" ");

                    addressBundle.putString("fulladdress", sb.toString());

                    return addressBundle;
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                addressBundle.putBoolean("error", true);
                return addressBundle;

            }

        }


        @Override
        // setting address into different components
        protected void onPostExecute(Bundle userAddress) {
            super.onPostExecute(userAddress);

            try {

                String address = userAddress.getString("fulladdress"); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String knownName = userAddress.getString("addressline2");


                if (knownName != null) {
                    txtSociety.setText(knownName);

                }
                txtAddress.setText("" + address);

                btnLocation.setVisibility(View.VISIBLE);


                Utility.hideProgress();

            } catch (Exception e) {
e.toString();
            }

        }
    }



}