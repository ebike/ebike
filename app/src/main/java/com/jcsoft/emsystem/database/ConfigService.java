package com.jcsoft.emsystem.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcsoft.emsystem.client.JCLog;


public class ConfigService
{
	private static ConfigService _instance = null;

	public static ConfigService instance()
	{
		if (_instance == null)
		{
			_instance = new ConfigService();
		}
		return _instance;
	}

	private ConfigService()
	{
	}

	public int insertConfigValue(String key, String value)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			db.execSQL("INSERT INTO JCConfig(key, value) VALUES(?, ?);",
					new Object[] { key, value });
		} 
		catch (Exception ex)
		{
			JCLog.e(ConfigService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}

	public String getConfigValue(String key)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return null;
		}

		try
		{
			Cursor cursor = db.rawQuery(
					"SELECT value FROM JCConfig WHERE key = ? limit 0, 1;",
					new String[] { key });

			if (cursor.moveToNext())
			{
				return cursor.getString(0);
			}
		} 
		catch (Exception ex)
		{
			JCLog.e(ConfigService.this.toString(), ex.getMessage());
			return "";
		}
		return "";
	}

	public int updateConfigValue(String key, String newValue)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			db.execSQL("UPDATE JCConfig SET value=? WHERE key=?;",
					new Object[] { newValue, key });
		}
		catch (Exception ex)
		{
			JCLog.e(ConfigService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}

	public int deleteConfigValue(String key)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			db.execSQL("DELETE FROM JCConfig WHERE key=?;",
					new Object[] { key });
		} 
		catch (Exception ex)
		{
			JCLog.e(ConfigService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}

	public int updateOrInsert(String key, String value)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			Cursor cursor = db.rawQuery(
					"SELECT * FROM JCConfig WHERE key=?;",
					new String[] { key });
			if (cursor.getCount() > 0)
			{
				updateConfigValue(key, value);
			}
			else
			{
				insertConfigValue(key, value);
			}
		} 
		catch (Exception ex)
		{
			JCLog.e(ConfigService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}

	public Cursor getConfigCursor()
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return null;
		}

		try
		{
			Cursor cursor = db.rawQuery(
					"SELECT config_id as _id, key, value FROM JCConfig;", null);
			return cursor;
		} 
		catch (Exception ex)
		{
			JCLog.e(ConfigService.this.toString(), ex.getMessage());
			return null;
		}
	}
}
