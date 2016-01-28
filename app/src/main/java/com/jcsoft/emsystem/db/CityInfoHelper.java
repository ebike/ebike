package com.jcsoft.emsystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CityInfoHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "ebike_city.db";
    public static String COUNTRY_TABLE_NAME = "countryList";
    public static String PROVINCE_TABLE_NAME = "provinceList";
	public static String CITY_TABLE_NAME = "cityList";
    public static String DISTRICT_TABLE_NAME = "districtList";

    public boolean isNewDatabase = false;

	public CityInfoHelper(Context context) {
		super(context, DB_NAME, null, 1004);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Create table
        String countrySql = "CREATE TABLE "+ COUNTRY_TABLE_NAME + "("
                + "_id INTEGER PRIMARY KEY,"
                + "cid INTEGER,"
                + "name TEXT);";
        db.execSQL(countrySql);
        String provinceSql = "CREATE TABLE "+ PROVINCE_TABLE_NAME + "("
                + "_id INTEGER PRIMARY KEY,"
                + "pid INTEGER,"
                + "name TEXT,"
                + "country_id INTEGER);";
        db.execSQL(provinceSql);
        String citySql = "CREATE TABLE "+ CITY_TABLE_NAME + "("
                + "_id INTEGER PRIMARY KEY,"
                + "cid INTEGER,"
                + "name TEXT,"
                + "province_id INTEGER);";
		db.execSQL(citySql);
        String districtSql = "CREATE TABLE "+ DISTRICT_TABLE_NAME + "("
                + "_id INTEGER PRIMARY KEY,"
                + "did INTEGER,"
                + "name TEXT,"
                + "city_id INTEGER);";
        db.execSQL(districtSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COUNTRY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROVINCE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DISTRICT_TABLE_NAME);
        onCreate(db);
	}



}
