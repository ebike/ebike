package com.jcsoft.emsystem.protocol;

public class NotifyReqFromServer extends JCProtocol
{
	private int _devId = 0;
	private int _type = 0;
	private int _value = 0;
	//经度，发生此状态时的地点，可能为0
	private int _longitude = 0;
	//纬度
	private int _latitude = 0;
	//变成此状态的时间
	private String _statusTime = "";

	public NotifyReqFromServer()
	{
		super(EnumDataType.DATA_NotifyReqFromServer);
	}

	@Override
	public void decodeContent()
	{
		_devId = byteArrayToInt(_content, 0, 4);
		_type = byteArrayToInt(_content, 4, 2);
		_value = byteArrayToInt(_content, 6, 2);
		
		if (_type == 1 && _value != 2)    //只有电子围栏处于开启或者报警状态解析下面的参数才有意义
		{		
			setLongitude(bcdToInt(_content, 8, 5));
			setLatitude(bcdToInt(_content, 13, 4));
			
			String timeStr = bcdToStr(_content, 17, 6);
			timeStr = bcdTimeToTimeStr(timeStr);
			setStatusTime(timeStr);
		}
	}

	public int getType()
	{
		return _type;
	}

	public void setType(int type)
	{
		_type = type;
	}

	public int getValue()
	{
		return _value;
	}

	public void setValue(int value)
	{
		_value = value;
	}

	public int getDevId()
	{
		return _devId;
	}

	public void setDevId(int devId)
	{
		_devId = devId;
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

	public void setStatusTime(String time)
	{
		_statusTime = time;
	}

}
