package com.jcsoft.emsystem.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jcsoft.emsystem.base.LocationJson;

import java.util.ArrayList;
import java.util.List;

public class ProvinceInfoDao {
    private CityInfoHelper helper;
    private ProvinceHelper provinceHelper;

    public ProvinceInfoDao(Context context) {
        super();
        helper = new CityInfoHelper(context);
        provinceHelper = new ProvinceHelper(context);
    }

    public boolean insert(LocationJson locationJson) {
        provinceHelper.openDatabase();
        SQLiteDatabase db = mGetDataBase();
        String sql = "insert into " + CityInfoHelper.PROVINCE_TABLE_NAME
                + "(pid,name,country_id) values ( "
                + locationJson.getId()
                + " ," + "'" + locationJson.getName()
                + "' , " + locationJson.getCountry_id()
                + " )";
        try {
            db.execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
            provinceHelper.closeDatabase();
        }
    }

    public List<LocationJson> queryAll() {
        provinceHelper.openDatabase();
        SQLiteDatabase db = mGetDataBase();
        List<LocationJson> list = new ArrayList<LocationJson>();
        Cursor cursor = db.query(CityInfoHelper.PROVINCE_TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            LocationJson locationJson = new LocationJson();
            locationJson.setId(cursor.getInt(cursor.getColumnIndex("pid")));
            locationJson.setName(cursor.getString(cursor.getColumnIndex("name")));
            locationJson.setCountry_id(cursor.getInt(cursor.getColumnIndex("country_id")));
            list.add(locationJson);
        }
        cursor.close();
        db.close();
        provinceHelper.closeDatabase();
        return list;
    }

    public List<LocationJson> queryByCountryId(int countryId) {
        provinceHelper.openDatabase();
        SQLiteDatabase db = mGetDataBase();
        List<LocationJson> list = new ArrayList<LocationJson>();
        Cursor cursor = db.query(CityInfoHelper.PROVINCE_TABLE_NAME, new String[]{"pid", "name", "country_id"},  "country_id=?", new String[]{countryId + ""}, null, null, null);

        while (cursor.moveToNext()) {
            LocationJson locationJson = new LocationJson();
            locationJson.setId(cursor.getInt(cursor.getColumnIndex("pid")));
            locationJson.setName(cursor.getString(cursor.getColumnIndex("name")));
            locationJson.setCountry_id(cursor.getInt(cursor.getColumnIndex("country_id")));
            list.add(locationJson);
        }
        cursor.close();
        db.close();
        provinceHelper.closeDatabase();
        return list;
    }

    public LocationJson queryByid(int id) {
        provinceHelper.openDatabase();
        SQLiteDatabase db = mGetDataBase();
        LocationJson locationJson = new LocationJson();
        Cursor cursor = db.query(CityInfoHelper.PROVINCE_TABLE_NAME, new String[]{"pid", "name", "country_id"}, "pid=?", new String[]{id + ""}, null, null, null);
        while (cursor.moveToNext()) {
            locationJson.setId(cursor.getInt(cursor.getColumnIndex("pid")));
            locationJson.setName(cursor.getString(cursor.getColumnIndex("name")));
            locationJson.setCountry_id(cursor.getInt(cursor.getColumnIndex("country_id")));
        }
        cursor.close();
        db.close();
        provinceHelper.closeDatabase();
        return locationJson;
    }

    public void clearFeedTable() {
        provinceHelper.openDatabase();
        String sql = "DELETE FROM " + CityInfoHelper.PROVINCE_TABLE_NAME + ";";
        SQLiteDatabase db = mGetDataBase();
        db.execSQL(sql);
        db.close();
        provinceHelper.closeDatabase();
    }

    private SQLiteDatabase mGetDataBase() {
        SQLiteDatabase db;
        if (helper.isNewDatabase) {
            db = helper.getReadableDatabase();
        } else {
            db = provinceHelper.getDatabase();
        }
        return db;
    }

}
