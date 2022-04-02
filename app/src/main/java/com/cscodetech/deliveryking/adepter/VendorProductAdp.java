package com.cscodetech.deliveryking.adepter;


import static com.cscodetech.deliveryking.utility.SessionManager.currency;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.model.ProductInfo;
import com.cscodetech.deliveryking.model.StoreDataItme;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.cscodetech.deliveryking.utility.DatabaseHelper;
import com.cscodetech.deliveryking.utility.MyCart;
import com.cscodetech.deliveryking.utility.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VendorProductAdp extends RecyclerView.Adapter<VendorProductAdp.MyViewHolder> {
    int isStore;
    String rID;
    private List<StoreDataItme> itemList;
    Context mContext;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;
    DatabaseHelper helper;

    public interface RecyclerTouchListener {
        public void onProductItem(String titel, int position);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_vegnonveg)
        ImageView txtVegnonveg;
        @BindView(R.id.txt_titele)
        TextView txtTitele;
        @BindView(R.id.spinner)
        Spinner spinner;
        @BindView(R.id.txt_atribut)
        TextView txtAtribut;
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
        @BindView(R.id.txt_offer)
        TextView txtOffer;
        @BindView(R.id.lvl_offer)
        LinearLayout lvlOffer;
        @BindView(R.id.priceoofer)
        TextView priceoofer;

        @BindView(R.id.lvl_itmeclik)
        LinearLayout lvlItmeclik;

        public MyViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    public VendorProductAdp(Context mContext, List<StoreDataItme> categoryList, final RecyclerTouchListener listener, int isStore, String rID) {
        this.isStore = isStore;
        this.rID = rID;
        this.mContext = mContext;
        this.itemList = categoryList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
        helper = new DatabaseHelper(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_storeproduct, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        StoreDataItme dataItem = itemList.get(position);


        holder.txtTitele.setText(dataItem.getTitle());
        if (dataItem.getItemImg() != null && !dataItem.getItemImg().isEmpty()) {
            holder.lvlImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(APIClient.baseUrl + "/" + dataItem.getItemImg()).thumbnail(Glide.with(mContext).load(R.drawable.emty)).into(holder.imageView);
        } else {
            holder.lvlImage.setVisibility(View.GONE);
        }
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

        if (dataItem.getProductInfo().size() <= 1) {
            holder.spinner.setVisibility(View.GONE);
            holder.txtAtribut.setVisibility(View.VISIBLE);
            holder.txtAtribut.setText("" + dataItem.getProductInfo().get(0).getProductType());
            if (dataItem.getProductInfo().get(0).getProductDiscount() == 0) {
                holder.lvlOffer.setVisibility(View.GONE);
                holder.priceoofer.setVisibility(View.GONE);
                holder.txtPrize.setText(sessionManager.getStringData(currency) + dataItem.getProductInfo().get(0).getProductPrice());
            } else {
                holder.lvlOffer.setVisibility(View.VISIBLE);
                holder.priceoofer.setVisibility(View.VISIBLE);
                DecimalFormat format = new DecimalFormat("0.#");
                holder.txtOffer.setText(format.format(dataItem.getProductInfo().get(0).getProductDiscount()) + "% OFF");
                double res = (Double.parseDouble(dataItem.getProductInfo().get(0).getProductPrice()) / 100.0f) * dataItem.getProductInfo().get(0).getProductDiscount();
                res = Double.parseDouble(dataItem.getProductInfo().get(0).getProductPrice()) - res;
                holder.txtPrize.setText(sessionManager.getStringData(currency) + new DecimalFormat("##.##").format(res));
                holder.priceoofer.setPaintFlags(holder.priceoofer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.priceoofer.setText(sessionManager.getStringData(currency) + dataItem.getProductInfo().get(0).getProductPrice());

            }
        } else {
            holder.txtAtribut.setVisibility(View.GONE);
            holder.spinner.setVisibility(View.VISIBLE);
            List<String> arrayList = new ArrayList<>();
            for (int i = 0; i < dataItem.getProductInfo().size(); i++) {
                arrayList.add(dataItem.getProductInfo().get(i).getProductType());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_layout, arrayList);
            holder.spinner.setAdapter(dataAdapter);
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (dataItem.getProductInfo().get(position).getProductDiscount() == 0) {
                        holder.lvlOffer.setVisibility(View.GONE);
                        holder.priceoofer.setVisibility(View.GONE);
                        holder.txtPrize.setText(sessionManager.getStringData(currency) + dataItem.getProductInfo().get(position).getProductPrice());
                    } else {
                        holder.lvlOffer.setVisibility(View.VISIBLE);
                        holder.priceoofer.setVisibility(View.VISIBLE);
                        DecimalFormat format = new DecimalFormat("0.#");
                        holder.txtOffer.setText(format.format(dataItem.getProductInfo().get(position).getProductDiscount()) + "% OFF");
                        double res = (Double.parseDouble(dataItem.getProductInfo().get(position).getProductPrice()) / 100.0f) * dataItem.getProductInfo().get(position).getProductDiscount();
                        res = Double.parseDouble(dataItem.getProductInfo().get(position).getProductPrice()) - res;
                        holder.txtPrize.setText(sessionManager.getStringData(currency) + new DecimalFormat("##.##").format(res));
                        holder.priceoofer.setPaintFlags(holder.priceoofer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.priceoofer.setText(sessionManager.getStringData(currency) + dataItem.getProductInfo().get(position).getProductPrice());

                    }
                    setJoinPlayrList(holder.lvlCart, dataItem, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }


        setJoinPlayrList(holder.lvlCart, dataItem, 0);
        holder.lvlItmeclik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onProductItem("", position);
            }
        });

    }

    private void setJoinPlayrList(LinearLayout lnrView, StoreDataItme medicine, int pos) {
        lnrView.removeAllViews();

        ProductInfo productPrice = medicine.getProductInfo().get(pos);
        final int[] count = {0};
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.custome_prize, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout txtOutstock = view.findViewById(R.id.txt_outstock);
        LinearLayout lvlAddremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvlAddcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout imgMins = view.findViewById(R.id.img_mins);
        LinearLayout imgPlus = view.findViewById(R.id.img_plus);
        MyCart myCart = new MyCart();
        myCart.setPid(medicine.getId());
        myCart.setProductName(medicine.getTitle());
        myCart.setProductPrice(productPrice.getProductPrice());
        myCart.setProductImage(medicine.getItemImg());
        myCart.setBrandName("medicine.get");
        myCart.setDiscount(productPrice.getProductDiscount());
        myCart.setShortDesc(medicine.getCdesc());
        myCart.setAttributeId(productPrice.getAttributeId());
        myCart.setProductType(productPrice.getProductType());
        myCart.setIsVeg(medicine.getIsVeg());

        if (productPrice.getProductOutStock().equalsIgnoreCase("1")) {
            txtOutstock.setVisibility(View.VISIBLE);
            lvlAddremove.setVisibility(View.GONE);
            lvlAddcart.setVisibility(View.GONE);

        } else {
            int qrt = helper.getCard(myCart.getAttributeId());
            if (qrt != -1) {
                count[0] = qrt;
                txtcount.setText("" + count[0]);
                lvlAddremove.setVisibility(View.VISIBLE);
                lvlAddcart.setVisibility(View.GONE);
            } else {
                lvlAddremove.setVisibility(View.GONE);
                lvlAddcart.setVisibility(View.VISIBLE);
            }
        }


        imgMins.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] - 1;
            if (count[0] <= 0) {
                txtcount.setText("" + count[0]);
                lvlAddremove.setVisibility(View.GONE);
                lvlAddcart.setVisibility(View.VISIBLE);
                helper.deleteRData(myCart.getAttributeId());
            } else {
                myCart.setQty(String.valueOf(count[0]));
                if (helper.insertData(myCart)) {

                    txtcount.setText("" + count[0]);

                }
                txtcount.setVisibility(View.VISIBLE);


            }
        });
        imgPlus.setOnClickListener(v -> {

            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;

            myCart.setQty(String.valueOf(count[0]));
            if (helper.insertData(myCart)) {

                txtcount.setText("" + count[0]);

            }
        });
        lvlAddcart.setOnClickListener(v -> {

            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            myCart.setQty(String.valueOf(count[0]));
            if (helper.insertData(myCart)) {
                txtcount.setText("" + count[0]);
                lvlAddcart.setVisibility(View.GONE);
                lvlAddremove.setVisibility(View.VISIBLE);
            }
        });
        lnrView.addView(view);
    }

    @Override
    public int getItemCount() {
        return itemList.size();


    }


}