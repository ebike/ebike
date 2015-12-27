package com.jcsoft.emsystem.client;

public class JCLocation
{
	private int _lat;     //纬度
	private int _lon;     //经度
	private int _speed;
	private String _lastLocationTime;
	private int _height;
	private int _direction;
	private byte _status;    //状态位
	
	public int getLat() 
	{
		return _lat;
	}
	
	public void setLat(int lat)
	{
		_lat = lat;
	}
	
	public int getLon()
	{
		return _lon;
	}
	
	public void setLon(int lon) 
	{
		_lon = lon;
	}
	
	public int getSpeed() 
	{
		return _speed;
	}
	
	public void setSpeed(int speed) 
	{
		_speed = speed;
	}
	
	public String getLastLocationTime()
	{
		return _lastLocationTime;
	}
	
	public void setLastLocationTime(String lastLocationTime)
	{
		_lastLocationTime = lastLocationTime;
	}

	public int getHeight()
	{
		return _height;
	}

	public void setHeight(int height)
	{
		_height = height;
	}

	public int getDirection()
	{
		return _direction;
	}

	public void setDirection(int direction)
	{
		_direction = direction;
	}

	public byte getStatus()
	{
		return _status;
	}

	public void setStatus(byte status)
	{
		_status = status;
	}	
}