package com.cscodetech.deliveryking.utility;


import static com.cscodetech.deliveryking.utility.SessionManager.restid;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cscodetech.deliveryking.activity.VendorActivity;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mydatabase1.db";
    public static final String TABLE_NAME = "storeapp";
    public static final String ICOL_1 = "ID";
    public static final String ICOL_2 = "PID";
    public static final String ICOL_3 = "productName";
    public static final String ICOL_4 = "productImage";
    public static final String ICOL_5 = "brandName";
    public static final String ICOL_6 = "shortDesc";
    public static final String ICOL_7 = "productPrice";
    public static final String ICOL_8 = "qty";
    public static final String ICOL_9 = "discount";
    public static final String ICOL_10 = "aid";
    public static final String ICOL_11 = "ptype";
    public static final String ICOL_12 = "storeid";
    public static final String ICOL_13 = "isveg";
    SessionManager sessionManager;
    Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        sessionManager = new SessionManager(context);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, PID TEXT , productName TEXT ,productImage TEXT , brandName TEXT , shortDesc TEXT, productPrice TEXT , qty int, discount Double , aid TEXT , ptype TEXT , storeid TEXT, isveg int )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(MyCart rModel) {

        int isV = isStore(sessionManager.getStringData(restid));
        if (isV != 0 && isV != Integer.parseInt(sessionManager.getStringData(restid))) {
            if (VendorActivity.getInstance() != null) {
                VendorActivity.getInstance().bottonCardClear();
            }
            return false;

        } else {
            if (getID(rModel.getAttributeId()) == -1) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(ICOL_2, rModel.getPid());
                contentValues.put(ICOL_3, rModel.getProductName());
                contentValues.put(ICOL_4, rModel.getProductImage());
                contentValues.put(ICOL_5, rModel.getBrandName());
                contentValues.put(ICOL_6, rModel.getShortDesc());
                contentValues.put(ICOL_7, rModel.getProductPrice());
                contentValues.put(ICOL_8, rModel.getQty());
                contentValues.put(ICOL_9, rModel.getDiscount());
                contentValues.put(ICOL_10, rModel.getAttributeId());
                contentValues.put(ICOL_11, rModel.getProductType());
                contentValues.put(ICOL_12, sessionManager.getStringData(restid));
                contentValues.put(ICOL_13, rModel.getIsVeg());
                long result = db.insert(TABLE_NAME, null, contentValues);
                if (result == -1) {
                    return false;
                } else {
                    if (VendorActivity.getInstance() != null)
                        VendorActivity.getInstance().cartview();

                    return true;
                }
            } else {
                return updateData(rModel.getAttributeId(), rModel.getQty());
            }
        }

    }

    @SuppressLint("Range")
    public int isStore(String storeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"storeid"}, "storeid =? ", new String[]{storeid}, null, null, null, null);
        if (c.moveToFirst()) { //if the row exist then return the id
            return c.getInt(c.getColumnIndex("storeid"));
        } else {
            List<MyCart> list = getAllData();
            if (list.size() != 0) {
                return -1;
            }
            return list.size();
        }
    }

    @SuppressLint("Range")
    private int getID(String aid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"PID"}, "aid =? ", new String[]{aid}, null, null, null, null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("PID"));
        return -1;
    }

    @SuppressLint("Range")
    public int getCard(String aid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"qty"}, "aid =? ", new String[]{aid}, null, null, null, null);
        if (c.moveToFirst()) { //if the row exist then return the id
            return c.getInt(c.getColumnIndex("qty"));
        } else {
            return -1;
        }
    }

    public List<MyCart> getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        List<MyCart> myCartList = new ArrayList<>();
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setId(res.getString(0));
            rModel.setPid(res.getString(1));
            rModel.setProductName(res.getString(2));
            rModel.setProductImage(res.getString(3));
            rModel.setBrandName(res.getString(4));
            rModel.setShortDesc(res.getString(5));
            rModel.setProductPrice(res.getString(6));
            rModel.setQty(res.getString(7));
            rModel.setDiscount(res.getInt(8));
            rModel.setAttributeId(res.getString(9));
            rModel.setProductType(res.getString(10));
            rModel.setStoreid(res.getString(11));
            rModel.setIsVeg(res.getInt(12));
            myCartList.add(rModel);
        }
        return myCartList;
    }

    public boolean updateData(String aid, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ICOL_8, qty);
        db.update(TABLE_NAME, contentValues, "aid = ? ", new String[]{aid});

        if (VendorActivity.getInstance() != null)
            VendorActivity.getInstance().cartview();
        return true;
    }

    public void deleteCard() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        if (VendorActivity.getInstance() != null)
            VendorActivity.getInstance().cartview();
    }

    public Integer deleteRData(String aid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer a = db.delete(TABLE_NAME, "aid = ? ", new String[]{aid});
        if (VendorActivity.getInstance() != null)
            VendorActivity.getInstance().cartview();
        return a;
    }
}