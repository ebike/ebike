package com.jcsoft.emsystem.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jcsoft.emsystem.base.LocationJson;

import java.util.ArrayList;
import java.util.List;

public class DistrictInfoDao {
    private CityInfoHelper helper;
    private ProvinceHelper provinceHelper;

    public DistrictInfoDao(Context context) {
        super();
        helper = new CityInfoHelper(context);
        provinceHelper = new ProvinceHelper(context);
    }

    public boolean insert(LocationJson locationJson) {
        provinceHelper.openDatabase();
        SQLiteDatabase db = mGetDataBase();
        String sql = "insert into " + CityInfoHelper.DISTRICT_TABLE_NAME
                + "(did,name,city_id) values ( "
                + locationJson.getId()
                + " ," + "'" + locationJson.getName()
                + "' , " + locationJson.getCity_id()
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
        Cursor cursor = db.query(CityInfoHelper.DISTRICT_TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            LocationJson locationJson = new LocationJson();
            locationJson.setId(cursor.getInt(cursor.getColumnIndex("did")));
            locationJson.setName(cursor.getString(cursor.getColumnIndex("name")));
            locationJson.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
            list.add(locationJson);
        }
        cursor.close();
        db.close();
        provinceHelper.closeDatabase();
        return list;
    }

    public List<LocationJson> queryByCityId(int cityId) {
        provinceHelper.openDatabase();
        SQLiteDatabase db = mGetDataBase();
        List<LocationJson> list = new ArrayList<LocationJson>();
        Cursor cursor = db.query(CityInfoHelper.DISTRICT_TABLE_NAME, new String[]{"did", "name", "city_id"},  "city_id=?", new String[]{cityId + ""}, null, null, null);

        while (cursor.moveToNext()) {
            LocationJson locationJson = new LocationJson();
            locationJson.setId(cursor.getInt(cursor.getColumnIndex("did")));
            locationJson.setName(cursor.getString(cursor.getColumnIndex("name")));
            locationJson.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
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
        Cursor cursor = db.query(CityInfoHelper.DISTRICT_TABLE_NAME, new String[]{"did", "name", "city_id"}, "did=?", new String[]{id + ""}, null, null, null);
        while (cursor.moveToNext()) {
            locationJson.setId(cursor.getInt(cursor.getColumnIndex("did")));
            locationJson.setName(cursor.getString(cursor.getColumnIndex("name")));
            locationJson.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
        }
        cursor.close();
        db.close();
        provinceHelper.closeDatabase();
        return locationJson;
    }

    public void clearFeedTable() {
        provinceHelper.openDatabase();
        String sql = "DELETE FROM " + CityInfoHelper.DISTRICT_TABLE_NAME + ";";
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
