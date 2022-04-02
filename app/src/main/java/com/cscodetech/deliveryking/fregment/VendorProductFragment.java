package com.cscodetech.deliveryking.fregment;

import static com.cscodetech.deliveryking.utility.SessionManager.currency;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.activity.VendorActivity;
import com.cscodetech.deliveryking.adepter.RestaurantProductAdp;
import com.cscodetech.deliveryking.adepter.VendorProductAdp;
import com.cscodetech.deliveryking.model.StoreDataItme;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VendorProductFragment extends Fragment implements RestaurantProductAdp.RecyclerTouchListener, VendorProductAdp.RecyclerTouchListener {
    @BindView(R.id.recycler_product)
    RecyclerView recyclerProduct;

    int mPosition = 0;
    String rID="0";
        public static VendorProductFragment newInstance(int param1, String param2) {
        VendorProductFragment fragment = new VendorProductFragment();
        Bundle args = new Bundle();
        args.putInt("position", param1);
        args.putString("rid", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_product, container, false);
        ButterKnife.bind(this, view);
        mPosition = getArguments().getInt("position");
        rID=getArguments().getString("rid");
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerProduct.setLayoutManager(mLayoutManager1);
        recyclerProduct.setAdapter(new VendorProductAdp(getActivity(), VendorActivity.getInstance().restorent.getResultData().getStoreProductData().get(mPosition).getStoreData(), this, VendorActivity.getInstance().restorent.getResultData().getRestuarantData().get(0).getRestIsopen(),rID));

        return view;
    }

    @Override
    public void onProductItem(String titel, int position) {


        bottonProductDetails(getActivity(),VendorActivity.getInstance().restorent.getResultData().getStoreProductData().get(mPosition).getStoreData().get(position));
    }


    public static void bottonProductDetails(Context context, StoreDataItme dataItem) {
        SessionManager sessionManager = new SessionManager(context);

        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.itemstore_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        ImageView imageView = rootView.findViewById(R.id.imageView);
        ImageView imgTypevag = rootView.findViewById(R.id.img_typevag);
        TextView txtPrize = rootView.findViewById(R.id.txt_prize);
        TextView txtTitle = rootView.findViewById(R.id.txt_title);
        TextView txtDesc = rootView.findViewById(R.id.txt_desc);
        Spinner spinner = rootView.findViewById(R.id.spinner);
        LinearLayout lvlOffer = rootView.findViewById(R.id.lvl_offer);
        TextView txtOffer = rootView.findViewById(R.id.txt_offer);
        TextView priceoofer = rootView.findViewById(R.id.txt_item_offer);


        Glide.with(context).load(APIClient.baseUrl + "/" + dataItem.getItemImg()).thumbnail(Glide.with(context).load(R.drawable.emty)).into(imageView);
        txtPrize.setText(sessionManager.getStringData(SessionManager.currency) + dataItem.getProductInfo().get(0).getProductPrice());
        txtDesc.setText(dataItem.getCdesc());
        txtTitle.setText(dataItem.getTitle());

        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < dataItem.getProductInfo().size(); i++) {
            arrayList.add(dataItem.getProductInfo().get(i).getProductType());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, R.layout.spinner_layout, arrayList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (dataItem.getProductInfo().get(position).getProductDiscount() == 0) {
                    lvlOffer.setVisibility(View.GONE);
                    priceoofer.setVisibility(View.GONE);
                    txtPrize.setText(sessionManager.getStringData(currency) + dataItem.getProductInfo().get(position).getProductPrice());
                } else {
                    lvlOffer.setVisibility(View.VISIBLE);
                    priceoofer.setVisibility(View.VISIBLE);
                    DecimalFormat format = new DecimalFormat("0.#");
                    txtOffer.setText(format.format(dataItem.getProductInfo().get(position).getProductDiscount()) + "% OFF");
                    double res = (Double.parseDouble(dataItem.getProductInfo().get(position).getProductPrice()) / 100.0f) * dataItem.getProductInfo().get(position).getProductDiscount();
                    res = Double.parseDouble(dataItem.getProductInfo().get(position).getProductPrice()) - res;
                    txtPrize.setText(sessionManager.getStringData(currency) + new DecimalFormat("##.##").format(res));
                    priceoofer.setPaintFlags(priceoofer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    priceoofer.setText(sessionManager.getStringData(currency) + dataItem.getProductInfo().get(position).getProductPrice());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (dataItem.getIsVeg() == 0) {
            imgTypevag.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nonveg));
        } else if (dataItem.getIsVeg() == 1) {
            imgTypevag.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_veg));

        } else if (dataItem.getIsVeg() == 2) {
            imgTypevag.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_egg));
        }
        mBottomSheetDialog.show();


    }
}