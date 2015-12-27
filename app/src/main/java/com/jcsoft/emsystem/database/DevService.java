package com.jcsoft.emsystem.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcsoft.emsystem.client.DevInfo;
import com.jcsoft.emsystem.client.JCLocation;
import com.jcsoft.emsystem.client.JCLog;

import java.util.ArrayList;
import java.util.List;


public class DevService
{
	private static DevService _instance = null;

	public static DevService instance()
	{
		if (_instance == null)
		{
			_instance = new DevService();
		}
		return _instance;
	}
	
	private DevService()
	{		
	}
	
	public int insertDev(DevInfo dev)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null || dev == null)
		{
			return -1;
		}

		try
		{
			db.beginTransaction();
			db.execSQL(
					"INSERT INTO JCDevice(dev_id, name, tel, type, lon, lat, last_time, speed, height, direction" +
					", vf_status, lock_status)" +
					" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
					new Object[] {dev.getDevId(), dev.getName(), dev.getTel(), dev.getType(), dev.getCurrentStatus().getLon()
							, dev.getCurrentStatus().getLat(), dev.getCurrentStatus().getLastLocationTime()
							, dev.getCurrentStatus().getSpeed(), dev.getCurrentStatus().getHeight()
							, dev.getCurrentStatus().getDirection(), dev.getVfStatus(), dev.getLockStatus()});
			db.setTransactionSuccessful();
			db.endTransaction();
		} 
		catch (Exception ex)
		{
			JCLog.e(DevService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}
	
	public List<DevInfo> getDevs(int startIndex, int count)
	{
		List<DevInfo> devs = new ArrayList<DevInfo>();

		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return null;
		}

		try
		{
			Cursor cursor = db.rawQuery(
					"SELECT * FROM JCDevice LIMIT ?, ?;",
					new String[] { String.valueOf(startIndex),
							String.valueOf(count) });
			while (cursor.moveToNext())
			{
				DevInfo d = new DevInfo();
				d.setDevId(cursor.getInt(1));
				d.setName(cursor.getString(2));
				d.setTel(cursor.getString(3));
				d.setType(cursor.getInt(4));
				JCLocation ds = new JCLocation();
				ds.setLon(cursor.getInt(5));
				ds.setLat(cursor.getInt(6));
				ds.setLastLocationTime(cursor.getString(7));
				ds.setSpeed(cursor.getInt(8));
				ds.setHeight(cursor.getInt(9));
				ds.setDirection(cursor.getInt(10));
				d.setVfStatus(cursor.getInt(11));
				d.setLockStatus(cursor.getInt(12));
				
				d.setCurrentStatus(ds);				
				devs.add(d);
			}
		} 
		catch (Exception ex)
		{
			JCLog.e(DevService.this.toString(), ex.getMessage());
			return null;
		}

		return devs;
	}
	
	public DevInfo getDevById(int devId)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return null;
		}

		try
		{
			Cursor cursor = db.rawQuery(
					"SELECT * FROM JCDevice where dev_id = ?;",
					new String[] { String.valueOf(devId) });
			if (cursor.moveToNext())
			{
				DevInfo d = new DevInfo();
				d.setDevId(cursor.getInt(1));
				d.setName(cursor.getString(2));
				d.setTel(cursor.getString(3));
				d.setType(cursor.getInt(4));
				JCLocation ds = new JCLocation();
				ds.setLon(cursor.getInt(5));
				ds.setLat(cursor.getInt(6));
				ds.setLastLocationTime(cursor.getString(7));
				ds.setSpeed(cursor.getInt(8));
				ds.setHeight(cursor.getInt(9));
				ds.setDirection(cursor.getInt(10));
				d.setVfStatus(cursor.getInt(11));
				d.setLockStatus(cursor.getInt(12));
				
				d.setCurrentStatus(ds);				
				return d;
			}
		} 
		catch (Exception ex)
		{
			JCLog.e(DevService.this.toString(), ex.getMessage());
			return null;
		}

		return null;
	}
	
	public int deleteDev(int devId)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			db.execSQL("DELETE FROM JCDevice WHERE dev_id=?;",
					new Object[] { devId });
		} 
		catch (Exception ex)
		{
			JCLog.e(DevService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}
	
	public int updateDevBaseInfo(DevInfo dev)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			db.execSQL("UPDATE JCDevice SET name=?, lon=?, lat=?, last_time=?, speed=?, height=?, direction=?" +
					" WHERE dev_id=?;",
					new Object[] {dev.getName(), dev.getCurrentStatus().getLon()
					, dev.getCurrentStatus().getLat(), dev.getCurrentStatus().getLastLocationTime()
					, dev.getCurrentStatus().getSpeed(), dev.getCurrentStatus().getHeight()
					, dev.getCurrentStatus().getDirection(), dev.getDevId()});
		}
		catch (Exception ex)
		{
			JCLog.e(DevService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}
	
	public int updateVFStatus(int devId, int vfStatus)
	{
		String sql = "UPDATE JCDevice SET vf_status = ? WHERE dev_id=?;";
		return execSql(sql, new Object[]{vfStatus, devId});
	}
	
	public int updateLockStatus(int devId, int lockStatus)
	{
		String sql = "UPDATE JCDevice SET lock_status = ? WHERE dev_id=?;";
		return execSql(sql, new Object[]{lockStatus, devId});
	}
	
	private int execSql(String sql, Object[] objs)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			db.execSQL(sql, objs);
		}
		catch (Exception ex)
		{
			JCLog.e(DevService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}
	
	public boolean isDevExistAlready(DevInfo dev)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return false;
		}
		

		try
		{
			Cursor cursor = db.rawQuery("SELECT dev_id from JCDevice where dev_id = ? limit 0,1;"
					, new String[]{String.valueOf(dev.getDevId())});
			if (cursor.getCount() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		} 
		catch (Exception ex)
		{
			JCLog.e(DevService.this.toString(), ex.getMessage());
			return false;
		}
	}
	
	public int checkToInsertDev(DevInfo dev)
	{
		if (isDevExistAlready(dev))
		{
			
			return 0;//updateDevBaseInfo(dev);
		}
		else
		{
			return insertDev(dev);
		}
	}
	
	public Cursor getDevCursor()
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return null;
		}

		try
		{
			Cursor cursor = db.rawQuery(
							"SELECT dev_index as _id, dev_id, name, tel, type, lon, lat" +
							", last_time, speed, height, direction FROM JCDevice;",
							null);
			return cursor;
		}
		catch (Exception ex)
		{
			JCLog.e(DevService.this.toString(), ex.getMessage());
			return null;
		}
	}

}
