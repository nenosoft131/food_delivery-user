package com.cscodetech.deliveryking.activity;


import static com.cscodetech.deliveryking.utility.FileUtils.isLocal;
import static com.cscodetech.deliveryking.utility.Utility.newAddress;
import static com.cscodetech.deliveryking.utility.Utility.paymentId;
import static com.cscodetech.deliveryking.utility.Utility.paymentsucsses;
import static com.cscodetech.deliveryking.utility.Utility.tragectionID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.Address;
import com.cscodetech.deliveryking.model.MyAddress;
import com.cscodetech.deliveryking.model.PackData;
import com.cscodetech.deliveryking.model.Payment;
import com.cscodetech.deliveryking.model.PaymentItem;
import com.cscodetech.deliveryking.model.RestResponse;
import com.cscodetech.deliveryking.model.User;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.retrofit.GetResult;
import com.cscodetech.deliveryking.utility.CustPrograssbar;
import com.cscodetech.deliveryking.utility.FileUtils;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.cscodetech.deliveryking.utility.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuoriarPayNowActivity extends AppCompatActivity implements GetResult.MyListener {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView txtActiontitle;
    @BindView(R.id.lvl_address1)
    LinearLayout lvlAddress1;
    @BindView(R.id.txt_type1)
    TextView txtType1;
    @BindView(R.id.txt_address1)
    TextView txtAddress1;
    @BindView(R.id.img_editeaddress1)
    ImageView imgEditeaddress1;
    @BindView(R.id.lvl_noaddress1)
    LinearLayout lvlNoaddress1;
    @BindView(R.id.txt_addaddress1)
    ImageView txtAddaddress1;
    @BindView(R.id.lvl_address2)
    LinearLayout lvlAddress2;
    @BindView(R.id.txt_type2)
    TextView txtType2;
    @BindView(R.id.txt_address2)
    TextView txtAddress2;
    @BindView(R.id.img_editeaddress2)
    ImageView imgEditeaddress2;
    @BindView(R.id.lvl_noaddress2)
    LinearLayout lvlNoaddress2;
    @BindView(R.id.txt_addaddress2)
    ImageView txtAddaddress2;
    @BindView(R.id.txt_distans)
    TextView txtDistans;
    @BindView(R.id.txt_dcharge)
    TextView txtDcharge;
    @BindView(R.id.txt_paynow)
    TextView txtPaynow;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    @BindView(R.id.ed_mobile2)
    EditText edMobile2;
    @BindView(R.id.ed_mobile1)
    EditText edMobile1;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    List<MyAddress> addressLists = new ArrayList<>();
    int pickPosition = 0;
    int dropPosition = 1;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    String addresstype = "1";
    List<PaymentItem> paymentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuoriernow);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(CuoriarPayNowActivity.this);
        user = sessionManager.getUserDetails("");
        getAddress();

    }

    private void getAddress() {
        custPrograssbar.prograssCreate(CuoriarPayNowActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().addressList(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void getPayment() {
        custPrograssbar.prograssCreate(CuoriarPayNowActivity.this);

        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getPaymentList(bodyRequest);
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
                Address address = gson.fromJson(result.toString(), Address.class);
                if (address.getResult().equalsIgnoreCase("true")) {
                    addressLists = address.getAddressList();
                    setAddress();
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
                paymentList = payment.getPaymentdata();
                bottonPaymentList();
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }
    }

    private void uploadMultiFile(ArrayList<String> filePaths) {
        custPrograssbar.prograssCreate(CuoriarPayNowActivity.this);
        List<MultipartBody.Part> parts = new ArrayList<>();

        if (filePaths != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < filePaths.size(); i++) {
                parts.add(prepareFilePart("image" + i, filePaths.get(i)));
            }
        }


// create a map of data to pass along
        RequestBody uid = createPartFromString(user.getId());
        RequestBody pMethodId = createPartFromString(paymentId);
        RequestBody paddress = createPartFromString(txtAddress1.getText().toString());
        RequestBody daddress = createPartFromString(txtAddress2.getText().toString());
        RequestBody pmobile = createPartFromString(edMobile1.getText().toString());
        RequestBody dmobile = createPartFromString(edMobile2.getText().toString());
        RequestBody dCharge = createPartFromString(String.valueOf(aDCharge));
        RequestBody transactionId = createPartFromString(tragectionID);
        RequestBody distance = createPartFromString(String.valueOf(distant));
        RequestBody category = createPartFromString(CuoriaritemActivity.getInstance().categoryName);
        RequestBody description = createPartFromString(txtDesc.getText().toString());
        RequestBody size = createPartFromString("" + parts.size());

// finally, execute the request
        Call<JsonObject> call = APIClient.getInterface().packData(uid, pMethodId, paddress, daddress, pmobile, dmobile, dCharge, transactionId, distance, category, description, size, parts);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                custPrograssbar.closePrograssBar();
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(response.body(), RestResponse.class);
                Toast.makeText(CuoriarPayNowActivity.this, restResponse.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {

                    CuoriaritemActivity.getInstance().arrayListImage = new ArrayList<>();
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                custPrograssbar.closePrograssBar();

            }
        });
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_TEXT), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {
        // use the FileUtils to get the actual file by uri
        File file = getFile(fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static File getFile(String path) {
        if (path == null) {
            return null;
        }

        if (isLocal(path)) {
            return new File(path);
        }
        return null;
    }


    @OnClick({R.id.img_editeaddress1, R.id.txt_addaddress1, R.id.img_editeaddress2, R.id.txt_addaddress2, R.id.img_back, R.id.txt_addimage, R.id.txt_paynow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_editeaddress1:
                addresstype = "1";
                bottonAddress(addressLists);

                break;
            case R.id.txt_addaddress1:
                startActivity(new Intent(CuoriarPayNowActivity.this, MapActivity.class)
                        .putExtra("latitude", 0.0)
                        .putExtra("longitude", 0.0)
                        .putExtra("atype", "Home")
                        .putExtra("newuser", "curruntlat")
                        .putExtra("userid", user.getId())
                        .putExtra("aid", "0"));
                break;
            case R.id.img_editeaddress2:
                addresstype = "2";

                bottonAddress(addressLists);
                break;
            case R.id.txt_addaddress2:
                startActivity(new Intent(CuoriarPayNowActivity.this, MapActivity.class)
                        .putExtra("latitude", 0.0)
                        .putExtra("longitude", 0.0)
                        .putExtra("atype", "Other")
                        .putExtra("newuser", "curruntlat")
                        .putExtra("userid", user.getId())
                        .putExtra("aid", "0"));
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_paynow:
                if (!txtDesc.getText().toString().equalsIgnoreCase("Add task details")) {
                    getPayment();
                } else {
                    txtDesc.setError("Add Description");

                }
                break;
            case R.id.txt_addimage:
                startActivity(new Intent(CuoriarPayNowActivity.this, CuoriaritemActivity.class));
                break;

            default:
                break;
        }
    }


    BottomSheetDialog mBottomSheetDialog;

    public void bottonAddress(List<MyAddress> list) {


        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.addressmain_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        RecyclerView myRecyclerview = sheetView.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CuoriarPayNowActivity.this, LinearLayoutManager.VERTICAL, false);
        myRecyclerview.setLayoutManager(layoutManager);
        AdepterAddress adepterAddress = new AdepterAddress(CuoriarPayNowActivity.this, list);
        myRecyclerview.setAdapter(adepterAddress);
        mBottomSheetDialog.show();

    }

    public class AdepterAddress extends RecyclerView.Adapter<AdepterAddress.BannerHolder> {
        private Context context;
        private List<MyAddress> listItems;

        public AdepterAddress(Context context, List<MyAddress> mBanner) {
            this.context = context;
            this.listItems = mBanner;
        }

        @NonNull
        @Override
        public AdepterAddress.BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.addresss_item, parent, false);
            return new AdepterAddress.BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdepterAddress.BannerHolder holder, int position) {
            holder.txtType.setText("" + listItems.get(position).getType());
            holder.txtFulladdress.setText("" + listItems.get(position).getHno() + "," + listItems.get(position).getLandmark() + "," + listItems.get(position).getAddress());


            Glide.with(context).load(APIClient.baseUrl + "/" + listItems.get(position).getAddressImage()).into(holder.imgBanner);
            holder.txtEdit.setText("SELECT");
            holder.txtEdit.setOnClickListener(v -> {
                newAddress = 1;
                PopupMenu popup = new PopupMenu(context, holder.txtEdit);
                popup.inflate(R.menu.address_menu);
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_select:
                            Utility.changeAddress = true;
                            if (addresstype.equalsIgnoreCase("1")) {
                                pickPosition = position;
                            } else {
                                dropPosition = position;
                            }
                            setAddress();
                            mBottomSheetDialog.cancel();
                            break;
                        case R.id.menu_edit:
                            mBottomSheetDialog.cancel();

                            startActivity(new Intent(CuoriarPayNowActivity.this, MapActivity.class)
                                    .putExtra("lat", Double.parseDouble(listItems.get(position).getLatMap()))
                                    .putExtra("long", Double.parseDouble(listItems.get(position).getLongMap()))
                                    .putExtra("landmark", listItems.get(position).getLandmark())
                                    .putExtra("hno", listItems.get(position).getHno())
                                    .putExtra("atype", listItems.get(position).getType())
                                    .putExtra("newuser", "old")
                                    .putExtra("userid", user.getId())
                                    .putExtra("aid", listItems.get(position).getId()));

                            break;
                        default:
                            break;
                    }
                    return false;
                });
                popup.show();
            });

            holder.txtDelete.setVisibility(View.GONE);

        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class BannerHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_banner)
            ImageView imgBanner;

            @BindView(R.id.txt_type)
            TextView txtType;
            @BindView(R.id.txt_fulladdress)
            TextView txtFulladdress;

            @BindView(R.id.txt_edit)
            TextView txtEdit;

            @BindView(R.id.txt_delete)
            TextView txtDelete;

            @BindView(R.id.lvl_home)
            LinearLayout lvlHome;

            public BannerHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }



    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            dist = (int) Math.round(dist);

            return (dist);
        }
    }


    public void setAddress() {

        switch (addressLists.size()) {

            case 0:
                lvlAddress1.setVisibility(View.GONE);
                lvlNoaddress1.setVisibility(View.VISIBLE);
                lvlAddress2.setVisibility(View.GONE);
                lvlNoaddress2.setVisibility(View.VISIBLE);
                txtPaynow.setVisibility(View.GONE);

                break;
            case 1:
                lvlAddress1.setVisibility(View.VISIBLE);
                lvlNoaddress1.setVisibility(View.GONE);
                lvlAddress2.setVisibility(View.GONE);
                lvlNoaddress2.setVisibility(View.VISIBLE);

                txtType1.setText("" + addressLists.get(pickPosition).getType());
                txtAddress1.setText("" + addressLists.get(pickPosition).getHno() + "," + addressLists.get(pickPosition).getLandmark() + "," + addressLists.get(pickPosition).getAddress());
                txtPaynow.setVisibility(View.GONE);

                break;
            case 2:
                lvlAddress1.setVisibility(View.VISIBLE);
                lvlNoaddress1.setVisibility(View.GONE);
                lvlAddress2.setVisibility(View.VISIBLE);
                lvlNoaddress2.setVisibility(View.GONE);

                txtType1.setText("" + addressLists.get(pickPosition).getType());
                txtAddress1.setText("" + addressLists.get(pickPosition).getHno() + "," + addressLists.get(pickPosition).getLandmark() + "," + addressLists.get(pickPosition).getAddress());

                txtType2.setText("" + addressLists.get(dropPosition).getType());
                txtAddress2.setText("" + addressLists.get(dropPosition).getHno() + "," + addressLists.get(dropPosition).getLandmark() + "," + addressLists.get(dropPosition).getAddress());

                distant = distance(Double.parseDouble(addressLists.get(pickPosition).getLatMap()), Double.parseDouble(addressLists.get(pickPosition).getLongMap()), Double.parseDouble(addressLists.get(dropPosition).getLatMap()), Double.parseDouble(addressLists.get(dropPosition).getLongMap()));
                txtDistans.setText(" " + new DecimalFormat("##.#").format(distant) + " kms");
                PackData packData = CourierActivity.getInstance().packData;

                if (Integer.parseInt(packData.getResultData().getPriceData().getUkms()) < distant) {
                    aDCharge = Integer.parseInt(packData.getResultData().getPriceData().getAfprice()) * distant;
                    txtDcharge.setText(" " + sessionManager.getStringData(SessionManager.currency) + new DecimalFormat("##.#").format(aDCharge));
                } else {
                    txtDcharge.setText(" " + sessionManager.getStringData(SessionManager.currency) + packData.getResultData().getPriceData().getUtprice());
                }
                txtPaynow.setVisibility(View.VISIBLE);
                break;


            default:
                if (3 <= addressLists.size()) {
                    lvlAddress1.setVisibility(View.VISIBLE);
                    lvlNoaddress1.setVisibility(View.GONE);
                    lvlAddress2.setVisibility(View.VISIBLE);
                    lvlNoaddress2.setVisibility(View.GONE);

                    txtType1.setText("" + addressLists.get(pickPosition).getType());
                    txtAddress1.setText("" + addressLists.get(pickPosition).getHno() + "," + addressLists.get(pickPosition).getLandmark() + "," + addressLists.get(pickPosition).getAddress());

                    txtType2.setText("" + addressLists.get(dropPosition).getType());
                    txtAddress2.setText("" + addressLists.get(dropPosition).getHno() + "," + addressLists.get(dropPosition).getLandmark() + "," + addressLists.get(dropPosition).getAddress());

                    distant = distance(Double.parseDouble(addressLists.get(pickPosition).getLatMap()), Double.parseDouble(addressLists.get(pickPosition).getLongMap()), Double.parseDouble(addressLists.get(dropPosition).getLatMap()), Double.parseDouble(addressLists.get(dropPosition).getLongMap()));
                    txtDistans.setText(" " + new DecimalFormat("##.#").format(distant) + " kms");
                    PackData packData1 = CourierActivity.getInstance().packData;

                    if (Integer.parseInt(packData1.getResultData().getPriceData().getUkms()) < distant) {
                        aDCharge = Integer.parseInt(packData1.getResultData().getPriceData().getAfprice()) * distant;
                        txtDcharge.setText(" " + sessionManager.getStringData(SessionManager.currency) + new DecimalFormat("##.#").format(aDCharge));
                    } else {
                        txtDcharge.setText(" " + sessionManager.getStringData(SessionManager.currency) + packData1.getResultData().getPriceData().getUtprice());
                    }
                    txtPaynow.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    double distant = 0;
    double aDCharge = 0;

    public class ImageAdp extends RecyclerView.Adapter<ImageAdp.MyViewHolder> {
        private List<String> arrayList;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView remove;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);

                thumbnail = view.findViewById(R.id.image_pic);
                remove = view.findViewById(R.id.image_remove);
            }
        }

        public ImageAdp(List<String> arrayList) {
            this.arrayList = arrayList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.imageview_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {


            Glide.with(CuoriarPayNowActivity.this)
                    .load(arrayList.get(position))
                    .into(holder.thumbnail);
            holder.remove.setOnClickListener(v -> {
                arrayList.remove(position);
                if (!arrayList.isEmpty()) {
                    notifyDataSetChanged();
                }

            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null && CuoriaritemActivity.getInstance() != null) {
            txtDesc.setText("" + CuoriaritemActivity.getInstance().edNote.getText().toString());
            ImageAdp imageAdp = new ImageAdp(CuoriaritemActivity.getInstance().arrayListImage);
            recyclerView.setAdapter(imageAdp);
        }
        if (sessionManager != null) {
            getAddress();
        }
        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            uploadMultiFile(CuoriaritemActivity.getInstance().arrayListImage);
        }
    }

    public void bottonPaymentList() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.custome_payment, null);
        LinearLayout listView = sheetView.findViewById(R.id.lvl_list);
        TextView txtTotal = sheetView.findViewById(R.id.txt_total);
        txtTotal.setText("item total " + sessionManager.getStringData(SessionManager.currency) + aDCharge);
        for (int i = 0; i < paymentList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(CuoriarPayNowActivity.this);
            PaymentItem paymentItem = paymentList.get(i);
            if (paymentList.get(i).getpShow().equalsIgnoreCase("1")) {
                View view = inflater.inflate(R.layout.custome_paymentitem, null);
                ImageView imageView = view.findViewById(R.id.img_icon);
                TextView txtTitle = view.findViewById(R.id.txt_title);
                TextView txtSubtitel = view.findViewById(R.id.txt_subtitel);
                txtTitle.setText("" + paymentList.get(i).getmTitle());
                txtSubtitel.setText("" + paymentList.get(i).getSubtitle());
                Glide.with(CuoriarPayNowActivity.this).load(APIClient.baseUrl + "/" + paymentList.get(i).getmImg()).thumbnail(Glide.with(CuoriarPayNowActivity.this).load(R.drawable.emty)).into(imageView);
                int finalI = i;
                view.setOnClickListener(v -> {
                    paymentId = paymentList.get(finalI).getmId();
                    try {
                        switch (paymentList.get(finalI).getmTitle()) {
                            case "Razorpay":
                                int temtoal = (int) Math.round(aDCharge);
                                startActivity(new Intent(CuoriarPayNowActivity.this, RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentItem));
                                break;
                            case "Cash On Delivery":
                                break;
                            case "Paypal":
                                startActivity(new Intent(CuoriarPayNowActivity.this, PaypalActivity.class).putExtra("amount", aDCharge).putExtra("detail", paymentItem));
                                break;
                            case "Stripe":
                                startActivity(new Intent(CuoriarPayNowActivity.this, StripPaymentActivity.class).putExtra("amount", aDCharge).putExtra("detail", paymentItem));
                                break;
                            case "PayStack":
                                int temtoal1 = (int) Math.round(aDCharge);
                                startActivity(new Intent(this, PaystackActivity.class).putExtra("amount", temtoal1).putExtra("detail", paymentItem));
                                break;
                            default:
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                listView.addView(view);
            }

        }
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }
}