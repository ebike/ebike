package com.jcsoft.emsystem.client;

public class DevInfo 
{
	private String _name = "";
	private int _devId = 0;
	private String _tel = "";
	private int _type = 0;            //0表示电动车，1表示手表定位仪，2表示车载卫星定位仪
	private String _status = "";      //标明当前是否在线
	private JCLocation _currentStatus;
	private int _vfStatus = -1;
	private int _lockStatus = -1;
	
	public int getDevId() 
	{
		return _devId;
	}
	
	public void setDevId(int devId) 
	{
		this._devId = devId;
	}
	
	public JCLocation getCurrentStatus() 
	{
		return _currentStatus;
	}
	
	public void setCurrentStatus(JCLocation currentStatus) 
	{
		_currentStatus = currentStatus;
	}

	public String getTel()
	{
		return _tel;
	}

	public void setTel(String tel)
	{
		_tel = tel;
	}

	public int getType()
	{
		return _type;
	}

	public void setType(int type)
	{
		_type = type;
	}

	public String getStatus()
	{
		return _status;
	}

	public void setStatus(String status)
	{
		_status = status;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public int getVfStatus()
	{
		return _vfStatus;
	}

	public void setVfStatus(int vfStatus)
	{
		_vfStatus = vfStatus;
	}

	public int getLockStatus()
	{
		return _lockStatus;
	}

	public void setLockStatus(int lockStatus)
	{
		_lockStatus = lockStatus;
	}
}