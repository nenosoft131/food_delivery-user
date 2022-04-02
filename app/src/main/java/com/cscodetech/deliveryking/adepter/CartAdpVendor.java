package com.cscodetech.deliveryking.adepter;

import static com.cscodetech.deliveryking.utility.SessionManager.coupon;
import static com.cscodetech.deliveryking.utility.SessionManager.currency;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.activity.CartActivityVendor;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.utility.DatabaseHelper;
import com.cscodetech.deliveryking.utility.MyCart;
import com.cscodetech.deliveryking.utility.SessionManager;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdpVendor extends RecyclerView.Adapter<CartAdpVendor.ViewHolder> {
    final int[] count = {0};
    double[] totalAmount = {0};
    private List<MyCart> mData;
    private LayoutInflater mInflater;
    Context mContext;
    SessionManager sessionManager;
    DatabaseHelper helper;

    public CartAdpVendor(Context context, List<MyCart> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        sessionManager = new SessionManager(context);
        helper = new DatabaseHelper(context);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custome_mycard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        MyCart myCart = mData.get(i);
        if (myCart.getDiscount() != 0) {

            double res = (Double.parseDouble(myCart.getProductPrice()) * myCart.getDiscount()) / 100;
            res = Double.parseDouble(myCart.getProductPrice()) - res;
            holder.txtPrice.setText(sessionManager.getStringData(currency) + new DecimalFormat("##.##").format(res));
            holder.txtDscount.setPaintFlags(holder.txtDscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtDscount.setText(sessionManager.getStringData(currency) + myCart.getProductPrice());
            holder.lvlOffer.setVisibility(View.VISIBLE);
            holder.txtOffer.setText(myCart.getDiscount() + "% OFF ");
        } else {
            holder.txtPrice.setText(sessionManager.getStringData(currency) + myCart.getProductPrice());
            holder.lvlOffer.setVisibility(View.GONE);
            holder.txtDscount.setVisibility(View.GONE);

        }
        Glide.with(mContext).load(APIClient.baseUrl + "/" + myCart.getProductImage()).thumbnail(Glide.with(mContext).load(R.drawable.emty)).centerCrop().into(holder.imgIcon);

        holder.txtTitle.setText("" + myCart.getProductName());
        holder.txtPtype.setText("" + myCart.getProductType());

        int qrt = helper.getCard(myCart.getAttributeId());
        if (qrt != -1) {
            count[0] = qrt;
            holder.txtcount.setText("" + count[0]);
            holder.txtcount.setVisibility(View.VISIBLE);
            holder.imgMins.setVisibility(View.VISIBLE);

        } else {
            holder.txtcount.setVisibility(View.INVISIBLE);
            holder.imgMins.setVisibility(View.INVISIBLE);
        }
        double ress = (Double.parseDouble(myCart.getProductPrice()) / 100.0f) * myCart.getDiscount();
        ress = Double.parseDouble(myCart.getProductPrice()) - ress;
        double temp = ress * qrt;
        totalAmount[0] = totalAmount[0] + temp;
        holder.imgMins.setOnClickListener(v -> {
            sessionManager.setIntData(coupon, 0);

            count[0] = Integer.parseInt(holder.txtcount.getText().toString());
            count[0] = count[0] - 1;
            if (count[0] <= 0) {
                holder.txtcount.setVisibility(View.INVISIBLE);
                holder.imgMins.setVisibility(View.INVISIBLE);
                holder.txtcount.setText("" + count[0]);
                helper.deleteRData(myCart.getAttributeId());
                mData.remove(myCart);

                totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getProductPrice());
                Toast.makeText(mContext, "" + myCart.getProductName() + "  is Remove", Toast.LENGTH_LONG).show();

                notifyDataSetChanged();
                CartActivityVendor.getInstance().updateCount();
            } else {
                holder.txtcount.setVisibility(View.VISIBLE);
                holder.txtcount.setText("" + count[0]);
                myCart.setQty(String.valueOf(count[0]));
                totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getProductPrice());
                helper.insertData(myCart);
                notifyDataSetChanged();
                CartActivityVendor.getInstance().updateCount();
            }
        });
        holder.imgPlus.setOnClickListener(v -> {
                sessionManager.setIntData(coupon, 0);
                holder.txtcount.setVisibility(View.VISIBLE);
                holder.imgMins.setVisibility(View.VISIBLE);
                count[0] = Integer.parseInt(holder.txtcount.getText().toString());
                totalAmount[0] = totalAmount[0] + Double.parseDouble(myCart.getProductPrice());
                count[0] = count[0] + 1;
                holder.txtcount.setText("" + count[0]);
                myCart.setQty(String.valueOf(count[0]));
                helper.insertData(myCart);
                CartActivityVendor.getInstance().updateCount();
        });
        holder.imgDelete.setOnClickListener(v -> {
            sessionManager.setIntData(coupon, 0);
            AlertDialog myDelete = new AlertDialog.Builder(mContext)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete?")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Delete", (dialog, whichButton) -> {
                        Log.d("sdj", "" + whichButton);
                        dialog.dismiss();
                        totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getProductPrice());
                        helper.deleteRData(myCart.getAttributeId());
                        mData.remove(myCart);
                        CartActivityVendor.getInstance().updateCount();
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                        Log.d("sdj", "" + which);
                        dialog.dismiss();
                    })
                    .create();
            myDelete.show();
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_dscount)
        TextView txtDscount;
        @BindView(R.id.txt_price)
        TextView txtPrice;
        @BindView(R.id.img_delete)
        ImageView imgDelete;
        @BindView(R.id.img_mins)
        LinearLayout imgMins;
        @BindView(R.id.txtcount)
        TextView txtcount;
        @BindView(R.id.img_plus)
        LinearLayout imgPlus;
        @BindView(R.id.lvl_addremove)
        LinearLayout lvlAddremove;
        @BindView(R.id.txt_offer)
        TextView txtOffer;
        @BindView(R.id.txt_ptype)
        TextView txtPtype;
        @BindView(R.id.lvl_offer)
        LinearLayout lvlOffer;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}