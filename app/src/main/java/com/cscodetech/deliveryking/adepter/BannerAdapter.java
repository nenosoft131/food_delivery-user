package com.cscodetech.deliveryking.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.BannerItem;
import com.cscodetech.deliveryking.retrofit.APIClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {
    private Context context;
    private List<BannerItem> mBanner;
    public BannerAdapter(Context context, List<BannerItem> mBanner) {
        this.context = context;
        this.mBanner = mBanner;
    }

    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_banner_item, parent, false);
        return new BannerHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, int position) {

        Glide.with(context).load(APIClient.baseUrl + "/" + mBanner.get(position).getImg()).thumbnail(Glide.with(context).load(R.drawable.emty)).centerCrop().into(holder.imgBanner);

    }
    @Override
    public int getItemCount() {
        return mBanner.size();

    }

    public class BannerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_banner)
        ImageView imgBanner;

        public BannerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}