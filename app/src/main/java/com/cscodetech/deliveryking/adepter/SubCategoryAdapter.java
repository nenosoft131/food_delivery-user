package com.cscodetech.deliveryking.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.CatlistItem;
import com.cscodetech.deliveryking.retrofit.APIClient;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<CatlistItem> mCatlist;
    private RecyclerTouchListener listener;


    public interface RecyclerTouchListener {
        public void onClickCategoryItem(String titel, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public RelativeLayout lvlclick;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            thumbnail = view.findViewById(R.id.imageView);
            lvlclick = view.findViewById(R.id.lvl_itemclick);

        }
    }

    public SubCategoryAdapter(Context mContext, List<CatlistItem> mCatlist, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subcategory, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        CatlistItem category = mCatlist.get(position);
        holder.title.setText(category.getTitle());
        Glide.with(mContext).load(APIClient.baseUrl + "/" + category.getCatImg()).thumbnail(Glide.with(mContext).load(R.drawable.emty)).into(holder.thumbnail);
        holder.lvlclick.setOnClickListener(v -> {
            listener.onClickCategoryItem(category.getId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();


    }
}