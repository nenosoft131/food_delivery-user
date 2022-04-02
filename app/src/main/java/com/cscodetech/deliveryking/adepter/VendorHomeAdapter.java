package com.cscodetech.deliveryking.adepter;

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

import java.util.List;

public class VendorHomeAdapter extends RecyclerView.Adapter<VendorHomeAdapter.MyViewHolder> {
    private Context mContext;
    private List<RestDataItem> mCatlist;
    private RecyclerTouchListener listener;


    public interface RecyclerTouchListener {
        public void onClickVendorItem(String titel, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView txtSubtitle;
        public TextView txtRating;
        public TextView txtReview;
        public ImageView thumbnail;
        public LinearLayout lvlclick;

        public MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.txt_title);
            txtSubtitle =  view.findViewById(R.id.txt_subtitle);
            txtRating =  view.findViewById(R.id.txt_rating);
            txtReview =  view.findViewById(R.id.txt_review);
            thumbnail = view.findViewById(R.id.imageView);
            lvlclick = view.findViewById(R.id.lvl_itemclick);

        }
    }

    public VendorHomeAdapter(Context mContext, List<RestDataItem> mCatlist, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_homevendor, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        RestDataItem category = mCatlist.get(position);
        holder.title.setText(category.getRestTitle());
        holder.txtSubtitle.setText(category.getRestFullAddress());
        holder.txtRating.setText(category.getRestRating());
        holder.txtReview.setText(category.getRestNumReview());
        Glide.with(mContext).load(APIClient.baseUrl + "/" + category.getRestImg()).thumbnail(Glide.with(mContext).load(R.drawable.emty)).into(holder.thumbnail);
        holder.lvlclick.setOnClickListener(v -> {

                listener.onClickVendorItem(category.getRestId(), position);

        });
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();
    }
}