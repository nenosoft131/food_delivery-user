package com.cscodetech.deliveryking.adepter;


import static com.cscodetech.deliveryking.utility.SessionManager.currency;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.MenuitemDataItem;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.utility.Product;
import com.cscodetech.deliveryking.utility.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantProductAdp extends RecyclerView.Adapter<RestaurantProductAdp.MyViewHolder> {
    int isStore;
    String rID;
    private List<MenuitemDataItem> itemList;
    Context mContext;

    SessionManager sessionManager;
    public static RestaurantProductAdp restaurantProductAdp;

    public static RestaurantProductAdp getInstance() {
        return restaurantProductAdp;
    }

    public interface RecyclerTouchListener {
        public void onProductItem(String titel, int position);


    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_vegnonveg)
        ImageView txtVegnonveg;
        @BindView(R.id.txt_titele)
        TextView txtTitele;
        @BindView(R.id.txt_prize)
        TextView txtPrize;
        @BindView(R.id.txt_desc)
        TextView txtDesc;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.lvl_cart)
        LinearLayout lvlCart;
        @BindView(R.id.lvl_image)
        LinearLayout lvlImage;

        @BindView(R.id.txt_custamize)
        TextView txtCustamize;

        @BindView(R.id.lvl_itmeclik)
        LinearLayout lvlItmeclik;

        public MyViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    public RestaurantProductAdp(Context mContext, List<MenuitemDataItem> categoryList,  int isStore, String rID) {
        this.isStore = isStore;
        this.rID = rID;
        this.mContext = mContext;
        this.itemList = categoryList;

        sessionManager = new SessionManager(mContext);
        restaurantProductAdp = this;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        MenuitemDataItem dataItem = itemList.get(position);

        Log.e("RID", "-->" + dataItem.getRid());

        holder.txtTitele.setText(dataItem.getTitle());
        if (dataItem.getItemImg() != null && !dataItem.getItemImg().isEmpty()) {
            holder.lvlImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(APIClient.baseUrl + "/" + dataItem.getItemImg()).thumbnail(Glide.with(mContext).load(R.drawable.emty)).into(holder.imageView);
        } else {
            holder.lvlImage.setVisibility(View.GONE);
        }
        holder.txtPrize.setText(sessionManager.getStringData(currency) + dataItem.getPrice());
        holder.txtDesc.setText("" + dataItem.getCdesc());

        if (dataItem.getIsVeg() == 0) {
            holder.txtVegnonveg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_nonveg));
        } else if (dataItem.getIsVeg() == 1) {
            holder.txtVegnonveg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_veg));

        } else if (dataItem.getIsVeg() == 2) {
            holder.txtVegnonveg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_egg));
        } else if (dataItem.getIsVeg() == 3) {
            holder.txtVegnonveg.setVisibility(View.GONE);
        }
        if (dataItem.getIsCustomize() == 1) {
            holder.txtCustamize.setVisibility(View.VISIBLE);
        } else {
            holder.txtCustamize.setVisibility(View.GONE);
        }
        holder.lvlItmeclik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataItem.getItemImg() != null && !dataItem.getItemImg().isEmpty() && isStore == 1) {
                    Product.bottonProductDetails(mContext, dataItem);
                }
            }
        });
        if (isStore == 1) {
            Product.setJoinPlayrList(holder.lvlCart, dataItem, mContext);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}