package com.jcsoft.emsystem.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jcsoft.emsystem.bean.JCMessage;
import com.jcsoft.emsystem.client.JCLog;

import java.util.ArrayList;
import java.util.List;

public class MsgService
{

	private static MsgService _instance = null;

	public static MsgService instance()
	{
		if (_instance == null)
		{
			_instance = new MsgService();
		}
		return _instance;
	}

	private MsgService()
	{
	}

	public int insertMsg(JCMessage msg)
	{

		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			db.beginTransaction();
			db.execSQL(
					"INSERT INTO Message(msg_type, msg_time, msg_content, is_read, dev_id) VALUES(?, ?, ?, ?, ?);",
					new Object[] { msg.getType(), msg.getTime(),
							msg.getContent(), msg.getIsRead(), msg.getDevId() });
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception ex)
		{
			JCLog.e(MsgService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}

	public int deleteMsg(int msgId)
	{

		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			db.execSQL("DELETE FROM Message WHERE msg_id=?;",
					new Object[] { msgId });
		} catch (Exception ex)
		{
			JCLog.e(MsgService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}

	public int deleteMsg(JCMessage msg)
	{
		return deleteMsg(msg.getId());
	}

	public int updateMsgIsRead(int msgId, int isRead)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return -1;
		}

		try
		{
			db.execSQL("UPDATE Message SET is_read=? WHERE msg_id=?;",
					new Object[] { isRead, msgId });
		} catch (Exception ex)
		{
			JCLog.e(MsgService.this.toString(), ex.getMessage());
			return -1;
		}
		return 0;
	}

	public JCMessage getMessageById(int msgId)
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return null;
		}

		try
		{
			Cursor cursor = db.rawQuery(
					"SELECT * FROM Message WHERE msg_id = ? limit 0, 1;",
					new String[] { String.valueOf(msgId) });

			if (cursor.moveToNext())
			{
				return new JCMessage(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getInt(4), cursor.getInt(5));
			}
		} catch (Exception ex)
		{
			JCLog.e(MsgService.this.toString(), ex.getMessage());
			return null;
		}
		return null;
	}

	public List<JCMessage> getMsgs(int startIndex, int count)
	{
		List<JCMessage> msgs = new ArrayList<JCMessage>();

		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return null;
		}

		try
		{
			Cursor cursor = db.rawQuery(
					"SELECT * FROM Message LIMIT ?, ?;",
					new String[] { String.valueOf(startIndex),
							String.valueOf(count) });
			if (cursor.moveToNext())
			{
				msgs.add(new JCMessage(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3)
						, cursor.getInt(4), cursor.getInt(5)));
			}
		} catch (Exception ex)
		{
			JCLog.e(MsgService.this.toString(), ex.getMessage());
			return null;
		}

		return msgs;
	}

	public int getCount()
	{
		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return 0;
		}

		try
		{
			Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Message", null);
			if (cursor.moveToNext())
			{
				return cursor.getInt(0);
			}
		} catch (Exception ex)
		{
			JCLog.e(MsgService.this.toString(), ex.getMessage());
			return 0;
		}
		return 0;
	}

	public Cursor getMsgCursor()
	{

		SQLiteDatabase db = DBHelper.instance().getWritableDatabase();
		if (db == null)
		{
			return null;
		}

		try
		{
			Cursor cursor = db
					.rawQuery(
							"SELECT msg_id as _id, msg_type, msg_time, msg_content, is_read, dev_id FROM Message order by msg_id desc;",
							null);
			return cursor;
		} catch (Exception ex)
		{
			JCLog.e(MsgService.this.toString(), ex.getMessage());
			return null;
		}
	}

}
