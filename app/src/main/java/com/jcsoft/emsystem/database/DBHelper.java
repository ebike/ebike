package com.jcsoft.emsystem.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private final static String _dbName = "jcsoft_em_police";
	private final static int _version = 1;
	private static DBHelper _instance = null;
	
	public static DBHelper instance(Context context)
	{
		if (_instance == null)
		{
			_instance = new DBHelper(context);
		}
		return _instance;
	}
	
	//在启动程序时需要调用instance(Context)进行初始化
	public static DBHelper instance()
	{
		if (_instance == null)
		{
			return null;
		}
		return _instance;
	}
	
	public DBHelper(Context context) {
		super(context, _dbName, null, _version);		
	}
	
	public DBHelper(Context context, String name, CursorFactory factory, int version){
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS Message(msg_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" msg_type VARCHAR(60), msg_time VARCHAR(60), msg_content TEXT, is_read INTEGER, dev_id INTEGER)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS JCConfig(config_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" key VARCHAR(60), value VARCHAR(60))");	
		
		//创建设备信息表，保存设备信息，包括设备ID，设备名，最后一次经纬度，最后一次定位时间，速度，高程，方向		
		db.execSQL("CREATE TABLE IF NOT EXISTS JCDevice(dev_index INTEGER PRIMARY KEY AUTOINCREMENT" +
				", dev_id INTEGER, name VARCHAR(60), tel VARCHAR(32), type INTEGER, lon INTEGER" +
				", lat INTEGER, last_time VARCHAR(60), speed INTEGER, height INTEGER, direction INTEGER" +
				", vf_status INTEGER, lock_status INTEGER)");	
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Message");
		db.execSQL("DROP TABLE IF EXISTS JCConfig");
		db.execSQL("DROP TABLE IF EXISTS JCDevice");
		onCreate(db);
	}

}
