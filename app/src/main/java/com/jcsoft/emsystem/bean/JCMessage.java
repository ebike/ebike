package com.jcsoft.emsystem.bean;

public class JCMessage
{

	private int _id;
	private String _type;
	private String _time;
	private String _content;
	private int _isRead;
	private int _devId;

	public JCMessage(int id, String type, String time, String content,
					 int isRead, int devId)
	{
		_id = id;
		setType(type);
		setTime(time);
		setContent(content);
		setIsRead(isRead);
		setDevId(devId);
	}

	public JCMessage(String type, String time, String content, int isRead,
					 int devId)
	{
		setType(type);
		setTime(time);
		setContent(content);
		setIsRead(isRead);
		setDevId(devId);
	}

	public int getId()
	{
		return _id;
	}

	public void setId(int newId)
	{
		_id = newId;
	}

	public String getType()
	{
		return _type;
	}

	public void setType(String type)
	{
		this._type = type;
	}

	public String getTime()
	{
		return _time;
	}

	public void setTime(String time)
	{
		this._time = time;
	}

	public String getContent()
	{
		return _content;
	}

	public void setContent(String content)
	{
		this._content = content;
	}

	public int getIsRead()
	{
		return _isRead;
	}

	public void setIsRead(int isRead)
	{
		this._isRead = isRead;
	}

	public int getDevId()
	{
		return _devId;
	}

	public void setDevId(int devId)
	{
		_devId = devId;
	}

}
