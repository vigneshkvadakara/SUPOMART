package com.lulixe.pulari.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lulixe.pulari.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AppDb extends SQLiteOpenHelper {

    private final String PRO_TABLE = "PRODUCTS";
    private final String ID = "ID";
    private final String IMAGE = "IMAGE";
    private final String PRO_ID = "PRO_ID";
    private final String PRICE = "PRICE";
    private final String PRO_NAME = "NAME";
    private final String CUSTOMER_QYT = "CUS_QTY";
    private final String DATE = "DATE";
    private final String VARIANT_ID = "UNIT_QTY";
    private final String VARIANT_NAME = "UNIT";

    public AppDb(@Nullable Context context) {
        super(context, "APP_DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+PRO_TABLE+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                IMAGE +" " + "TEXT,"+ PRO_ID+" INTEGER NOT NULL,"+PRICE+" REAL " +
                "NOT NULL,"+PRO_NAME+" TEXT,"+ CUSTOMER_QYT +" TEXT,"+DATE+" TEXT,"+ VARIANT_NAME +" TEXT,"+
                VARIANT_ID +" TEXT"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insert(Product pro){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(IMAGE,pro.getImage());
        cv.put(PRO_ID,pro.getId());
        cv.put(PRICE,pro.getPrice());
        cv.put(PRO_NAME,pro.getName());
        cv.put(CUSTOMER_QYT,pro.getCustomerQty());
        cv.put(DATE,pro.getDateTime());
        cv.put(VARIANT_NAME,pro.getUnit());
        cv.put(VARIANT_ID,pro.getVarID());

        db.insertOrThrow(PRO_TABLE,null,cv);

        return true;
    }

    public boolean update(Product pro){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CUSTOMER_QYT,pro.getCustomerQty());
        cv.put(DATE,pro.getDateTime());

        db.update(PRO_TABLE,cv,PRO_ID+"=? AND "+VARIANT_ID+"=?"
                ,new String[]{String.valueOf(pro.getId()),String.valueOf(pro.getVarID())});

        return true;
    }
    /*public boolean update(Product pro){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(IMAGE,pro.getId());
        cv.put(PRO_ID,pro.getId());
        cv.put(PRICE,pro.getPrice());
        cv.put(PRO_NAME,pro.getName());
        cv.put(CUSTOMER_QYT,pro.getCustomerQty());
        cv.put(DATE,pro.getDateTime());
        cv.put(VARIANT_NAME,pro.getUnit());
        cv.put(VARIANT_ID,pro.getVarID());

        db.update(PRO_TABLE,cv,PRO_ID+"=? AND "+VARIANT_ID+"=?"
                ,new String[]{String.valueOf(pro.getId()),String.valueOf(pro.getVarID())});

        return true;
    }*/

    public int getCartCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from "+PRO_TABLE,null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }
        return 0;
    }
    /*public List<Product> getAll(String TODAY_DATE){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Product>list = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM "+PRO_TABLE+" WHERE "+DATE+"=?",
                new String[]{TODAY_DATE});

        if(c.moveToFirst()){
            do {
                Product p = new Product();
                p.setId(c.getInt(0));
                p.setImage(c.getString(1));
                p.setProID(c.getInt(2));
                p.setPrice(c.getDouble(3));
                p.setName(c.getString(4));
                p.setCustomerQty(c.getString(5));
                p.setDateTime(c.getString(6));
                p.setUnit(c.getString(7));
                p.setVarID(c.getInt(8));

                list.add(p);
            }while (c.moveToNext());
        }

        return list;
    }*/

    public List<Product> getAll(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Product>list = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM "+PRO_TABLE, null);

        if(c.moveToFirst()){
            do {
                Product p = new Product();
                p.setDbID(c.getInt(0));
                p.setImage(c.getString(1));
                p.setId(c.getInt(2));
                p.setPrice(c.getDouble(3));
                p.setName(c.getString(4));
                p.setCustomerQty(c.getInt(5));
                p.setDateTime(c.getString(6));
                p.setUnit(c.getString(7));
                p.setVarID(c.getInt(8));

                list.add(p);
            }while (c.moveToNext());
        }

        return list;
    }

    public Product getSingle(int proID){
        SQLiteDatabase db = this.getReadableDatabase();

        Product p = new Product();

        Cursor c = db.rawQuery("SELECT * FROM "+PRO_TABLE+" WHERE "+PRO_ID+"=?",
                new String[]{String.valueOf(proID)});
        if(c.moveToFirst()){
            p.setId(c.getInt(0));
            p.setImage(c.getString(1));
            p.setId(c.getInt(2));
            p.setPrice(c.getDouble(3));
            p.setName(c.getString(4));
            p.setCustomerQty(c.getInt(5));
            p.setDateTime(c.getString(6));
            p.setVarID(c.getInt(7));
            p.setUnit(c.getString(8));
        }

        return p;
    }

    /*public boolean delSingle(int autoID){
        SQLiteDatabase db = this.getReadableDatabase();

        int i = db.delete(PRO_TABLE,ID+"=?",new String[]{String.valueOf(autoID)});
        if(i > 0) return true;
        else return false;
    }*/

    public boolean delSingle(int proId,int varID){
        SQLiteDatabase db = this.getReadableDatabase();

        int i = db.delete(PRO_TABLE,PRO_ID+"=? AND "+VARIANT_ID+"=?"
                ,new String[]{String.valueOf(proId),String.valueOf(varID)});
        if(i > 0) return true;
        else return false;
    }

    public boolean delAll(){
        SQLiteDatabase db = this.getReadableDatabase();

        int i = db.delete(PRO_TABLE,null,null);
        if(i > 0) return true;
        else return false;
    }

    public boolean delAll(String today_date) {
        SQLiteDatabase db = this.getReadableDatabase();

        int i = db.delete(PRO_TABLE,DATE+"<>?",new String[]{today_date});
        if(i > 0) return true;
        else return false;
    }

    /*public String[] getProQtyIfAvaliable(int proID){
        SQLiteDatabase db = this.getReadableDatabase();

        Log.e("url","SELECT "+ID+","+ CUSTOMER_QYT +" FROM "+PRO_TABLE+" WHERE "+PRO_ID+"="+proID);
        Cursor c = db.rawQuery("SELECT "+ID+","+ CUSTOMER_QYT +" FROM "+PRO_TABLE+" WHERE "+PRO_ID+"=?",
                new String[]{String.valueOf(proID)});
        if(c.moveToFirst()){
            return new String[]{c.getString(0),c.getString(1)};
        }

        return null;
    }*/

    public String[] getProQtyIfAvaliable(int proID,int varID){
        SQLiteDatabase db = this.getReadableDatabase();

        Log.e("url","SELECT "+ID+","+ CUSTOMER_QYT +" FROM "+PRO_TABLE+" WHERE "+PRO_ID+"="+proID);
        Cursor c = db.rawQuery("SELECT "+ID+","+ CUSTOMER_QYT +" FROM "+PRO_TABLE+" WHERE "+PRO_ID+"=?" +
                        " AND "+VARIANT_ID+"=?",
                new String[]{String.valueOf(proID),String.valueOf(varID)});
        if(c.moveToFirst()){
            return new String[]{c.getString(0),c.getString(1)};
        }

        return null;
    }

    public String[] getProQtyAddedinDB(int proID){
        SQLiteDatabase db = this.getReadableDatabase();

        Log.e("url","SELECT "+ID+","+ CUSTOMER_QYT +" FROM "+PRO_TABLE+" WHERE "+PRO_ID+"="+proID);
        Cursor c = db.rawQuery("SELECT "+ID+","+ CUSTOMER_QYT +" FROM "+PRO_TABLE+" WHERE "+PRO_ID+"=?",
                new String[]{String.valueOf(proID)});
        if(c.moveToFirst()){
            return new String[]{c.getString(0),c.getString(1)};
        }

        return null;
    }
}
