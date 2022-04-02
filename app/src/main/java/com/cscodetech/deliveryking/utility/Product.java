package com.cscodetech.deliveryking.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.activity.CartActivity;
import com.cscodetech.deliveryking.activity.RestaurantActivity;
import com.cscodetech.deliveryking.fregment.RestaurantProductFragment;
import com.cscodetech.deliveryking.model.AddonItem;
import com.cscodetech.deliveryking.model.Addondata;
import com.cscodetech.deliveryking.model.AddonsubItem;
import com.cscodetech.deliveryking.model.Addonsubdata;
import com.cscodetech.deliveryking.model.MenuitemDataItem;
import com.cscodetech.deliveryking.retrofit.APIClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class Product {

    public static void bottonProductDetails(Context context, MenuitemDataItem dataItem) {
        SessionManager sessionManager = new SessionManager(context);

        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.product_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        ImageView imageView = rootView.findViewById(R.id.imageView);
        ImageView imgTypevag = rootView.findViewById(R.id.img_typevag);
        TextView txtPrize = rootView.findViewById(R.id.txt_prize);
        TextView txtTitle = rootView.findViewById(R.id.txt_title);
        TextView txtDesc = rootView.findViewById(R.id.txt_desc);
        LinearLayout lvlCart = rootView.findViewById(R.id.lvl_cart);
        TextView txtCustamize = rootView.findViewById(R.id.txt_custamize);

        Glide.with(context).load(APIClient.baseUrl + "/" + dataItem.getItemImg()).thumbnail(Glide.with(context).load(R.drawable.emty)).into(imageView);
        txtPrize.setText(sessionManager.getStringData(SessionManager.currency) + dataItem.getPrice());
        txtDesc.setText(dataItem.getCdesc());
        txtTitle.setText(dataItem.getTitle());

        if (dataItem.getIsCustomize() == 1) {
            txtCustamize.setVisibility(View.VISIBLE);
        } else {
            txtCustamize.setVisibility(View.GONE);
        }
        if (dataItem.getIsVeg() == 0) {
            imgTypevag.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nonveg));
        } else if (dataItem.getIsVeg() == 1) {

            imgTypevag.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_veg));

        } else if (dataItem.getIsVeg() == 2) {
            imgTypevag.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_egg));
        }
        lvlCart.setOnClickListener(v -> mBottomSheetDialog.cancel());
        mBottomSheetDialog.show();
        setJoinPlayrList(lvlCart, dataItem, activity);

    }


    public static void setJoinPlayrList(LinearLayout lnrView, MenuitemDataItem dataItem, Context mContext) {
        lnrView.removeAllViews();
        SessionManager sessionManager = new SessionManager(mContext);

        Restaurent helper = new Restaurent(mContext);
        final int[] count = {0};
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.custome_cartadd, null);
        TextView txtcount = view.findViewById(R.id.txtcount);

        LinearLayout lvlAddremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvlAddcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout imgMins = view.findViewById(R.id.img_mins);
        LinearLayout imgPlus = view.findViewById(R.id.img_plus);
        if (dataItem.getRid() == null) {
            dataItem.setRid(sessionManager.getStringData(SessionManager.restid));
        }
        int qrt = helper.getCard(dataItem.getId());
        if (qrt >= 0) {
            count[0] = qrt;
            txtcount.setText("" + count[0]);
            lvlAddremove.setVisibility(View.VISIBLE);
            lvlAddcart.setVisibility(View.GONE);
        } else {
            lvlAddremove.setVisibility(View.GONE);
            lvlAddcart.setVisibility(View.VISIBLE);
        }

        imgMins.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] - 1;
            txtcount.setText("" + count[0]);

            if (count[0] <= 0) {
                txtcount.setText("" + count[0]);
                lvlAddremove.setVisibility(View.GONE);
                lvlAddcart.setVisibility(View.VISIBLE);
                helper.deleteRData(dataItem.getId());
                if (CartActivity.getInstance() != null)
                    CartActivity.getInstance().updateCount();

            } else {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                dataItem.setQty(count[0]);
                helper.insertData(dataItem);
            }


            if (RestaurantActivity.getInstance() != null) {
                RestaurantActivity.getInstance().cartview();
            }
            if (CartActivity.getInstance() != null)
                CartActivity.getInstance().updateCount();

        });
        imgPlus.setOnClickListener(v -> {

            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            txtcount.setText("" + count[0]);

            if (count[0] <= 0) {
                txtcount.setText("" + count[0]);
                lvlAddremove.setVisibility(View.GONE);
                lvlAddcart.setVisibility(View.VISIBLE);
                helper.deleteRData(dataItem.getId());
                if (CartActivity.getInstance() != null)
                    CartActivity.getInstance().updateCount();

            } else {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                dataItem.setQty(count[0]);
                helper.insertData(dataItem);
            }


            if (RestaurantActivity.getInstance() != null) {
                RestaurantActivity.getInstance().cartview();
            }
            if (CartActivity.getInstance() != null)
                CartActivity.getInstance().updateCount();

//            if (dataItem.getIsCustomize() == 1 && dataItem.getAddondata() != null) {
//
//                bottonRepeat(mContext, dataItem);
//
//
//            } else {
//                count[0] = Integer.parseInt(txtcount.getText().toString());
//                count[0] = count[0] + 1;
//                dataItem.setQty(count[0]);
//                helper.insertData(dataItem);
//                txtcount.setText("" + count[0]);
//                if (RestaurantActivity.getInstance() != null) {
//                    RestaurantActivity.getInstance().cartview();
//                }
//
//            }


        });
        lvlAddcart.setOnClickListener(v -> {

            if (dataItem.getIsCustomize() == 1) {
                count[0] = Integer.parseInt(txtcount.getText().toString());
                count[0] = count[0] + 2;
                dataItem.setQty(count[0]);
                txtcount.setText("" + count[0]);
                Restaurent myHelper = new Restaurent(mContext);
                int isV = myHelper.isStore(dataItem.getRid());
                if (isV != 0 && isV != Integer.parseInt(dataItem.getRid())) {
                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(mContext)
                            // set message, title, and icon
                            .setTitle("Items already in cart")
                            .setMessage("Your cart contains items from other restaurant. Would you like to reset your cart for adding items from this restaurant?")

                            .setPositiveButton("Yes", (dialog, whichButton) -> {
                                //your deleting code
                                dialog.dismiss();
                                helper.deleteCard();
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .create();

                    myQuittingDialogBox.show();


                } else {
                    bottonCustamazlist(mContext, dataItem);
                }

            } else {
                count[0] = Integer.parseInt(txtcount.getText().toString());
                count[0] = count[0] + 1;
                txtcount.setText("" + count[0]);
                lvlAddcart.setVisibility(View.GONE);
                lvlAddremove.setVisibility(View.VISIBLE);
                dataItem.setQty(count[0]);
                dataItem.setAddonID("0");
                if (helper.insertData(dataItem)) {
                   
                }
            }


        });
        lnrView.addView(view);
    }

    public static void bottonRepeat(Context context, MenuitemDataItem dataItem) {
        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.custmizanable_repeat, null);
        mBottomSheetDialog.setContentView(sheetView);
        TextView txtTitle = sheetView.findViewById(R.id.txt_title);
        TextView txtRepeat = sheetView.findViewById(R.id.txt_repeat);
        TextView txtChoose = sheetView.findViewById(R.id.txt_choose);

        txtTitle.setText("" + dataItem.getTitle());
        txtChoose.setOnClickListener(v -> {
            bottonCustamazlist(context, dataItem);
            mBottomSheetDialog.cancel();

        });

        txtRepeat.setOnClickListener(v -> {
            Restaurent myHelper = new Restaurent(context);
            int count = myHelper.getCard(dataItem.getId());
            count = count + 3;
            dataItem.setQty(count);

            myHelper.updateData(dataItem.getId(), dataItem.getQty());
            if (CartActivity.getInstance() != null)
                CartActivity.getInstance().updateCount();

            mBottomSheetDialog.cancel();


        });

        mBottomSheetDialog.show();

    }


    public static void bottonCustamazlist(Context context, MenuitemDataItem dataItem) {

        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.custmizanable_layoutmain, null);
        mBottomSheetDialog.setContentView(sheetView);

        RecyclerView packageRecyclerView = sheetView.findViewById(R.id.package_lst);
        LinearLayout lvl1 = sheetView.findViewById(R.id.lvl1);
        ImageView imgProduct = sheetView.findViewById(R.id.img_product);
        TextView btnAdditem = sheetView.findViewById(R.id.btn_additem);
        TextView txtTitle = sheetView.findViewById(R.id.txt_title);
        TextView txtTotoal = sheetView.findViewById(R.id.txt_totoal);
        TextView btnNextstep = sheetView.findViewById(R.id.btn_nextstep);
        txtTitle.setText("" + dataItem.getTitle());
        txtTotoal.setText(new SessionManager(context).getStringData(SessionManager.currency) + dataItem.getPrice());
        Glide.with(context).load(APIClient.baseUrl + "/" + dataItem.getItemImg()).thumbnail(Glide.with(context).load(R.drawable.emty)).centerCrop().into(imgProduct);
        mBottomSheetDialog.show();
        Restaurent helper = new Restaurent(context);


        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(context);
        packageRecyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(packageRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        packageRecyclerView.addItemDecoration(dividerItemDecoration);

        PackageRecyclerViewAdapter recyclerViewAdapter = new
                PackageRecyclerViewAdapter(dataItem.getAddondata(), context, lvl1);
        packageRecyclerView.setAdapter(recyclerViewAdapter);
        if (dataItem.getRequiredStep() == 1) {
            btnNextstep.setVisibility(View.VISIBLE);
        } else {
            btnAdditem.setVisibility(View.VISIBLE);

        }
        btnNextstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNextstep.setVisibility(View.GONE);
                btnAdditem.setVisibility(View.VISIBLE);


                for (int j = 0; j < dataItem.getAddondata().get(0).getAddonItemData().size(); j++) {
                    if (dataItem.getAddondata().get(0).getAddonItemData().get(j).isSelect()) {
                        Log.e("J", "--> " + dataItem.getAddondata().get(0).getAddonItemData().get(j).getTitle());
                        SubRecyclerViewAdapter recyclerViewAdapter1 = new
                                SubRecyclerViewAdapter(dataItem.getAddondata().get(0).getAddonItemData().get(j).getAddondata(), context, lvl1);
                        packageRecyclerView.setAdapter(recyclerViewAdapter1);
                    }
                }


            }
        });
        btnAdditem.setOnClickListener(v -> {


            String aid = null;
            String title = null;
            String price = null;
            for (int i = 0; i < dataItem.getAddondata().size(); i++) {

                for (int j = 0; j < dataItem.getAddondata().get(i).getAddonItemData().size(); j++) {
                    if (dataItem.getAddondata().get(i).getAddonItemData().get(j).isSelect()) {
                        Log.e("I", "--> " + i);
                        Log.e("J", "--> " + dataItem.getAddondata().get(i).getAddonItemData().get(j).getTitle());
                        for (int a = 0; a < dataItem.getAddondata().get(i).getAddonItemData().get(j).getAddondata().size(); a++) {

                            for (int jj = 0; jj < dataItem.getAddondata().get(i).getAddonItemData().get(j).getAddondata().get(a).getAddonItemData().size(); jj++) {
                                if (dataItem.getAddondata().get(i).getAddonItemData().get(j).getAddondata().get(a).getAddonItemData().get(jj).isSelect()) {
                                    if (aid == null || aid.length() == 0) {
                                        aid = dataItem.getAddondata().get(i).getAddonItemData().get(j).getAddondata().get(a).getAddonItemData().get(jj).getId();
                                        title = dataItem.getAddondata().get(i).getAddonItemData().get(j).getAddondata().get(a).getAddonItemData().get(jj).getTitle();
                                        price = dataItem.getAddondata().get(i).getAddonItemData().get(j).getAddondata().get(a).getAddonItemData().get(jj).getPrice();
                                    } else {
                                        aid = aid.concat("," + dataItem.getAddondata().get(i).getAddonItemData().get(j).getAddondata().get(a).getAddonItemData().get(jj).getId());
                                        title = title.concat("," + dataItem.getAddondata().get(i).getAddonItemData().get(j).getAddondata().get(a).getAddonItemData().get(jj).getTitle());
                                        price = price.concat("," + dataItem.getAddondata().get(i).getAddonItemData().get(j).getAddondata().get(a).getAddonItemData().get(jj).getPrice());

                                    }
                                }
                            }
                        }
                        if (aid == null || aid.length() == 0) {
                            aid = dataItem.getAddondata().get(i).getAddonItemData().get(j).getId();
                            title = dataItem.getAddondata().get(i).getAddonItemData().get(j).getTitle();
                            price = dataItem.getAddondata().get(i).getAddonItemData().get(j).getPrice();
                        } else {
                            aid = aid.concat("," + dataItem.getAddondata().get(i).getAddonItemData().get(j).getId());
                            title = title.concat("," + dataItem.getAddondata().get(i).getAddonItemData().get(j).getTitle());
                            price = price.concat("," + dataItem.getAddondata().get(i).getAddonItemData().get(j).getPrice());

                        }
                    }
                }
            }
            Log.e("aid", "-->" + aid);
            Log.e("title", "-->" + title);
            Log.e("price", "-->" + price);
            dataItem.setAddonID(aid);
            dataItem.setAddonTitel(title);
            dataItem.setAddonPrice(price);
            if (helper.insertData(dataItem)) {
                if (RestaurantActivity.getInstance() != null) {
                    RestaurantActivity.getInstance().cartview();
                    RestaurantProductFragment.getInstance().updateList();

                } else if (CartActivity.getInstance() != null) {
                    CartActivity.getInstance().updateCount();
                }
                mBottomSheetDialog.cancel();
            }
        });
    }

    public static class PackageRecyclerViewAdapter extends
            RecyclerView.Adapter<PackageRecyclerViewAdapter.ViewHolder> {
        SessionManager sessionManager;
        LinearLayout lvl1;
        private List<Addondata> packageList;
        private Context context;


        public PackageRecyclerViewAdapter(List<Addondata> packageListIn
                , Context ctx, LinearLayout lvl1) {
            packageList = packageListIn;
            context = ctx;
            sessionManager = new SessionManager(context);
            this.lvl1 = lvl1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.package_item, parent, false);

            ViewHolder viewHolder =
                    new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            Addondata packageModel = packageList.get(position);
            if (packageModel.getAddonLimit() == 0) {
                holder.packageName.setText(packageModel.getTitle());
            } else {
                holder.packageName.setText(packageModel.getTitle() + "(" + packageModel.getAddonLimit() + ")");

            }

            if (packageModel.getAddonIsRadio() == 1) {

                for (AddonItem item : packageModel.getAddonItemData()) {
                    RadioButton rb = new RadioButton(PackageRecyclerViewAdapter.this.context);
                    rb.setId(Integer.parseInt(item.getId()));
                    rb.setText(item.getTitle() + " " + sessionManager.getStringData(SessionManager.currency) + item.getPrice());

                    holder.priceGroup.addView(rb);
                }
            } else {
                for (AddonItem item : packageModel.getAddonItemData()) {
                    CheckBox box = new CheckBox(PackageRecyclerViewAdapter.this.context);
                    box.setId(Integer.parseInt(item.getId()));

                    box.setText(item.getTitle() + "  " + sessionManager.getStringData(SessionManager.currency) + item.getPrice());
                    box.setTextSize(12);
                    holder.lvlChackbox.addView(box);
                    if (item.isSelect()) {
                        box.setChecked(true);
                    }
                    box.setOnClickListener(v -> {
                        Log.e("text", "-->" + box.getText());
                        if (!box.isChecked()) {
                            packageModel.getAddonItemData().get(packageModel.getAddonItemData().indexOf(item)).setSelect(false);
                        } else {
                            if (packageModel.getAddonLimit() == 0) {
                                packageModel.getAddonItemData().get(packageModel.getAddonItemData().indexOf(item)).setSelect(box.isChecked());

                            } else {
                                int limitT = 0;
                                for (int a = 0; a < packageModel.getAddonItemData().size(); a++) {

                                    if (packageModel.getAddonItemData().get(a).isSelect()) {
                                        limitT++;
                                    }
                                }
                                if (limitT < packageModel.getAddonLimit()) {
                                    packageModel.getAddonItemData().get(packageModel.getAddonItemData().indexOf(item)).setSelect(box.isChecked());
                                } else {

                                    box.setChecked(false);


                                }
                            }
                        }


                    });
                }
            }
            holder.priceGroup.setOnCheckedChangeListener((radioGroup, i) -> {


                View radioButton = holder.priceGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                int idx = holder.priceGroup.indexOfChild(radioButton);
                for (int a = 0; a < packageModel.getAddonItemData().size(); a++) {

                    packageModel.getAddonItemData().get(a).setSelect(false);
                }
                packageModel.getAddonItemData().get(idx).setSelect(true);

            });


        }

        @Override
        public int getItemCount() {
            return packageList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView packageName;
            public RadioGroup priceGroup;
            public LinearLayout lvlChackbox;


            public ViewHolder(View view) {
                super(view);
                packageName = view.findViewById(R.id.package_name);
                priceGroup = view.findViewById(R.id.price_grp);
                lvlChackbox = view.findViewById(R.id.lvl_chackbox);


            }
        }
    }


    public static class SubRecyclerViewAdapter extends
            RecyclerView.Adapter<SubRecyclerViewAdapter.ViewHolder> {
        SessionManager sessionManager;
        LinearLayout lvl1;
        private List<Addonsubdata> packageList;
        private Context context;


        public SubRecyclerViewAdapter(List<Addonsubdata> packageListIn
                , Context ctx, LinearLayout lvl1) {
            packageList = packageListIn;
            context = ctx;
            sessionManager = new SessionManager(context);
            this.lvl1 = lvl1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.package_item, parent, false);

            ViewHolder viewHolder =
                    new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            Addonsubdata packageModel = packageList.get(position);
            if (packageModel.getAddonLimit() == 0) {
                holder.packageName.setText(packageModel.getTitle());
            } else {
                holder.packageName.setText(packageModel.getTitle() + "(" + packageModel.getAddonLimit() + ")");

            }

            if (packageModel.getAddonIsRadio() == 1) {

                for (AddonsubItem item : packageModel.getAddonItemData()) {
                    RadioButton rb = new RadioButton(SubRecyclerViewAdapter.this.context);
                    rb.setId(Integer.parseInt(item.getId()));
                    rb.setText(item.getTitle() + " " + sessionManager.getStringData(SessionManager.currency) + item.getPrice());

                    holder.priceGroup.addView(rb);
                }
            } else {
                for (AddonsubItem item : packageModel.getAddonItemData()) {
                    CheckBox box = new CheckBox(SubRecyclerViewAdapter.this.context);
                    box.setId(Integer.parseInt(item.getId()));

                    box.setText(item.getTitle() + "  " + sessionManager.getStringData(SessionManager.currency) + item.getPrice());
                    box.setTextSize(12);
                    holder.lvlChackbox.addView(box);
                    if (item.isSelect()) {
                        box.setChecked(true);
                    }
                    box.setOnClickListener(v -> {
                        Log.e("text", "-->" + box.getText());
                        if (!box.isChecked()) {
                            packageModel.getAddonItemData().get(packageModel.getAddonItemData().indexOf(item)).setSelect(false);
                        } else {
                            if (packageModel.getAddonLimit() == 0) {
                                packageModel.getAddonItemData().get(packageModel.getAddonItemData().indexOf(item)).setSelect(box.isChecked());

                            } else {
                                int limitT = 0;
                                for (int a = 0; a < packageModel.getAddonItemData().size(); a++) {

                                    if (packageModel.getAddonItemData().get(a).isSelect()) {
                                        limitT++;
                                    }
                                }
                                if (limitT < packageModel.getAddonLimit()) {
                                    packageModel.getAddonItemData().get(packageModel.getAddonItemData().indexOf(item)).setSelect(box.isChecked());
                                } else {

                                    box.setChecked(false);


                                }
                            }
                        }


                    });
                }
            }
            holder.priceGroup.setOnCheckedChangeListener((radioGroup, i) -> {


                View radioButton = holder.priceGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                int idx = holder.priceGroup.indexOfChild(radioButton);
                for (int a = 0; a < packageModel.getAddonItemData().size(); a++) {

                    packageModel.getAddonItemData().get(a).setSelect(false);
                }
                packageModel.getAddonItemData().get(idx).setSelect(true);

            });


        }

        @Override
        public int getItemCount() {
            return packageList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView packageName;
            public RadioGroup priceGroup;
            public LinearLayout lvlChackbox;


            public ViewHolder(View view) {
                super(view);
                packageName = view.findViewById(R.id.package_name);
                priceGroup = view.findViewById(R.id.price_grp);
                lvlChackbox = view.findViewById(R.id.lvl_chackbox);


            }
        }
    }


}
