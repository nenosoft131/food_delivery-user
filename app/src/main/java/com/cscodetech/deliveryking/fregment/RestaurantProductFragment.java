package com.cscodetech.deliveryking.fregment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.activity.RestaurantActivity;
import com.cscodetech.deliveryking.adepter.RestaurantProductAdp;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RestaurantProductFragment extends Fragment implements RestaurantProductAdp.RecyclerTouchListener {
    @BindView(R.id.recycler_product)
    RecyclerView recyclerProduct;
    RestaurantProductAdp restaurantProductAdp;
    int mPosition = 0;
    String rID="0";
    public static RestaurantProductFragment fragment;
    public static RestaurantProductFragment getInstance(){
        return fragment;
    }
    public static RestaurantProductFragment newInstance(int param1, String param2) {
        RestaurantProductFragment fragment = new RestaurantProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_restaurant_product, container, false);
        ButterKnife.bind(this, view);
        fragment=this;
        mPosition = getArguments().getInt("position");
        rID=getArguments().getString("rid");
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerProduct.setLayoutManager(mLayoutManager1);
        restaurantProductAdp=new RestaurantProductAdp(getActivity(), RestaurantActivity.getInstance().restorent.getResultData().getProductData().get(mPosition).getMenuitemData(),  RestaurantActivity.getInstance().restorent.getResultData().getRestuarantData().get(0).getRestIsopen(),rID);
        recyclerProduct.setAdapter(restaurantProductAdp);

        return view;
    }

    @Override
    public void onProductItem(String titel, int position) {

    }
    public void updateList(){
        synchronized(restaurantProductAdp){
            restaurantProductAdp.notifyDataSetChanged();
        }

    }
}