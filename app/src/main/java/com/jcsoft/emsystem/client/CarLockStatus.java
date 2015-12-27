package com.jcsoft.emsystem.client;


public class CarLockStatus
{
	public enum EnumCLS
	{
		CLS_UNKNOWN,
		CLS_Locked,     //0x03
		CLS_Unlocked,   //0x04
		CLS_Unusable    //0x05
	}
	
	private int _devId = -1;
	private EnumCLS _status = EnumCLS.CLS_UNKNOWN;
	
	public CarLockStatus()
	{
		
	}
	
	public CarLockStatus(int devId, EnumCLS status)
	{
		_devId = devId;
		_status = status;		
	}
	
	public int getDevId()
	{
		return _devId;
	}
	
	public void setDevId(int devId)
	{
		_devId = devId;
	}
	
	public EnumCLS getStatus()
	{
		return _status;
	}
	
	public void setStatus(EnumCLS status)
	{
		_status = status;
	}
	
	
}
