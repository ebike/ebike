package com.jcsoft.emsystem.client;

public class VFStatus
{
	public enum EnumVFS
	{
		VFS_UNKNOWN,
		VFS_Opened,      //0x01
		VFS_Closed,      //0x03
		VFS_Unusable,    //0x05
		VFS_Alarm        //0x06 报警中
	}
	
	private int _devId = -1;
	private EnumVFS _status = EnumVFS.VFS_UNKNOWN;
	//经度，发生此状态时的地点
	private int _longitude = 0;
	//纬度
	private int _latitude = 0;
	//变成此状态的时间
	private String _statusTime = "";
	
	public VFStatus()
	{
		
	}
	
	public VFStatus(int devId, EnumVFS status, int longitude, int latitude, String statusTime)
	{
		_devId = devId;
		_status = status;		
		_longitude = longitude;
		_latitude = latitude;
		_statusTime = statusTime;
	}
	
	public int getDevId()
	{
		return _devId;
	}
	
	public void setDevId(int devId)
	{
		_devId = devId;
	}
	
	public EnumVFS getStatus()
	{
		return _status;
	}
	
	public void setStatus(EnumVFS status)
	{
		_status = status;
	}

	public int getLongitude()
	{
		return _longitude;
	}

	public void setLongitude(int longitude)
	{
		_longitude = longitude;
	}

	public int getLatitude()
	{
		return _latitude;
	}

	public void setLatitude(int latitude)
	{
		_latitude = latitude;
	}

	public String getStatusTime()
	{
		return _statusTime;
	}

	public void setStatusTime(String statusTime)
	{
		_statusTime = statusTime;
	}
}
