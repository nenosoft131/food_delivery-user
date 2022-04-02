package com.cscodetech.deliveryking.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.deliveryking.R;
import com.cscodetech.deliveryking.imagepicker.ImageCompressionListener;
import com.cscodetech.deliveryking.imagepicker.ImagePicker;
import com.cscodetech.deliveryking.model.PackData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CuoriaritemActivity extends AppCompatActivity {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView txtActiontitle;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.ed_note)
    EditText edNote;
    @BindView(R.id.img_pick)
    ImageView imgPick;
    @BindView(R.id.btn_countinus)
    TextView btnCountinus;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ImagePicker imagePicker;
    ArrayList<String> arrayListImage = new ArrayList<>();
    public static CuoriaritemActivity activity;
    public static CuoriaritemActivity getInstance() {
        return activity;
    }

    PackData packData;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_dropitem);
        ButterKnife.bind(this);
        activity = this;
        imagePicker = new ImagePicker();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);

        packData = CourierActivity.getInstance().packData;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<String> statusList = new ArrayList<>();
        for (int i = 0; i < packData.getResultData().getPackageCategory().size(); i++) {
            statusList.add(packData.getResultData().getPackageCategory().get(i).getCatName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList);

        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryName = packData.getResultData().getPackageCategory().get(position).getCatName();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("Test", "part");
            }
        });

    }

    public class ImageAdp extends RecyclerView.Adapter<ImageAdp.MyViewHolder> {
        private List<String> arrayList;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView remove;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);

                thumbnail = view.findViewById(R.id.image_pic);
                remove = view.findViewById(R.id.image_remove);
            }
        }

        public ImageAdp(List<String> arrayList) {
            this.arrayList = arrayList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.imageview_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {


            Glide.with(CuoriaritemActivity.this)
                    .load(arrayList.get(position))
                    .into(holder.thumbnail);
            holder.remove.setOnClickListener(v -> {

                if (!arrayList.isEmpty()) {
                    arrayList.remove(position);
                    notifyDataSetChanged();
                }

            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @OnClick({R.id.img_pick, R.id.img_back, R.id.btn_countinus})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_pick:
                imagePicker.withActivity(CuoriaritemActivity.this).chooseFromGallery(true).chooseFromCamera(false).withCompression(true).start();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_countinus:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == RESULT_OK) {

            imagePicker.addOnCompressListener(new ImageCompressionListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onCompressed(String filePath) {
                    if (filePath != null) {
                        arrayListImage.add(filePath);
                        postImage(arrayListImage);
                    }
                }
            });
            String filePath = imagePicker.getImageFilePath(data);
            if (filePath != null) {
                arrayListImage.add(filePath);
                postImage(arrayListImage);
            }

        }
    }

    public void postImage(ArrayList<String> urilist) {
        ImageAdp imageAdp = new ImageAdp(urilist);
        recyclerView.setAdapter(imageAdp);

    }
}