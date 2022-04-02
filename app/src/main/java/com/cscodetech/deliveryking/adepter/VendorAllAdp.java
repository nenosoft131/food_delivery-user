package com.cscodetech.deliveryking.adepter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.RestDataItem;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.utility.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VendorAllAdp extends RecyclerView.Adapter<VendorAllAdp.MyViewHolder> {
    private Context mContext;
    private List<RestDataItem> itemList;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void VendorAllAdp(String rid, int position);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        public ImageView imageView;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_sdesc)
        TextView txtSdesc;
        @BindView(R.id.txt_location)
        TextView txtLocation;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_price)
        TextView txtPrice;
        @BindView(R.id.txt_offers)
        TextView txtOffers;
        @BindView(R.id.txt_star)
        TextView txtStar;
        @BindView(R.id.lvl_offerdata)
        LinearLayout lvlOfferdata;

        @BindView(R.id.lvl_itmeclik)
        LinearLayout lvlItmeclik;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }

    public VendorAllAdp(Context mContext, List<RestDataItem> categoryList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.itemList = categoryList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_allvendor, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        RestDataItem item = itemList.get(position);
        holder.txtTitle.setText(item.getRestTitle());
        holder.txtSdesc.setText(item.getRestSdesc());

        holder.txtLocation.setText(item.getRestFullAddress() + " | " + item.getRestDistance());
        holder.txtStar.setText(item.getRestRating());
        holder.txtTime.setText(item.getRestNumReview() + " Review");
        Glide.with(mContext).load(APIClient.baseUrl + "/" + item.getRestImg()).thumbnail(Glide.with(mContext).load(R.drawable.emty)).into(holder.imageView);

        holder.lvlItmeclik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.VendorAllAdp(item.getRestId(), position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();

    }


}