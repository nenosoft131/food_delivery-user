package com.cscodetech.deliveryking.fregment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.activity.OrderStoreActivity;
import com.cscodetech.deliveryking.activity.RestorentOrderActivity;
import com.cscodetech.deliveryking.adepter.RestorentOrderAdp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListFragment extends Fragment implements RestorentOrderAdp.RecyclerTouchListener {
    @BindView(R.id.recycler_orderlist)
    RecyclerView recyclerOrderlist;
    @BindView(R.id.notfound)
    ImageView notfound;
    int mPosition = 0;

    public OrderListFragment() {
    }
    public static OrderListFragment newInstance(int param1, String param2) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt("position", param1);
        args.putString("rid", param2);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        ButterKnife.bind(this, view);
        mPosition = getArguments().getInt("position");
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerOrderlist.setLayoutManager(mLayoutManager1);
        if (RestorentOrderActivity.getInstance().order.getOrderHistory().get(mPosition).getHistoryData().size() == 0) {
            notfound.setVisibility(View.VISIBLE);
            recyclerOrderlist.setVisibility(View.GONE);
        } else {
            notfound.setVisibility(View.GONE);
            recyclerOrderlist.setVisibility(View.VISIBLE);


        }
        RestorentOrderAdp orderAdp = new RestorentOrderAdp(getActivity(), RestorentOrderActivity.getInstance().order.getOrderHistory().get(mPosition).getHistoryData(), this);
        recyclerOrderlist.setAdapter(orderAdp);
        return view;
    }

    @Override
    public void onOrderItem(String oid) {
        startActivity(new Intent(getActivity(), OrderStoreActivity.class).putExtra("oid", oid).putExtra("type", RestorentOrderActivity.getInstance().order.getOrderHistory().get(mPosition).getTypeid()));
    }
}