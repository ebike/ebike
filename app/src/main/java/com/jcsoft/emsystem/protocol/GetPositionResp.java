package com.jcsoft.emsystem.protocol;

import com.jcsoft.emsystem.client.DevInfo;
import com.jcsoft.emsystem.client.JCLocation;

public class GetPositionResp extends JCProtocol
{
	private int _devId = 0;
	//卫星个数
	private int _satelliteCount = 0;
	//状态位：第0位 保留；第1位 0表示未定位，1表示已定位；第2位
	private byte _status = 0;	
	//经度
	private int _longitude = 0;
	//纬度
	private int _latitude = 0;
	//高程
	private int _height = 0;
	//速度
	private int _speed = 0;
	//方向
	private int _direction = 0;
	//最后一次定位时间
	private String _time = "";
	
	private DevInfo _devInfo = null;
	
	public GetPositionResp()
	{
		super(EnumDataType.DATA_GetPositionResp);
	}

	@Override
	public void decodeContent()
	{
		if (_content == null)
		{
			return;
		}
		
		setDevId(byteArrayToInt(_content, 0, 4));
		setSatelliteCount(_content[4]);
		setStatus(_content[5]);
		
		setLongitude(bcdToInt(_content, 6, 5));
		setLatitude(bcdToInt(_content, 11, 4));
		setHeight(bcdToInt(_content, 15, 2));
		setSpeed(bcdToInt(_content, 17, 2));
		setDirection(bcdToInt(_content, 19, 2));
		
		String timeStr = bcdToStr(_content, 21, 6);
		timeStr = bcdTimeToTimeStr(timeStr);

		setTime(timeStr);
	}

	public int getSatelliteCount()
	{
		return _satelliteCount;
	}

	public void setSatelliteCount(int satelliteCount)
	{
		_satelliteCount = satelliteCount;
	}

	public byte getStatus()
	{
		return _status;
	}

	public void setStatus(byte status)
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

	public int getHeight()
	{
		return _height;
	}

	public void setHeight(int height)
	{
		_height = height;
	}

	public int getSpeed()
	{
		return _speed;
	}

	public void setSpeed(int speed)
	{
		_speed = speed;
	}

	public int getDirection()
	{
		return _direction;
	}

	public void setDirection(int direction)
	{
		_direction = direction;
	}

	public String getTime()
	{
		return _time;
	}

	public void setTime(String time)
	{
		_time = time;
	}

	public DevInfo getDevInfo()
	{
		if (_devInfo == null)
		{
			_devInfo = new DevInfo();
			_devInfo.setDevId(getDevId());
			JCLocation currentStatus = new JCLocation();
			currentStatus.setDirection(_direction);
			currentStatus.setHeight(_height);
			currentStatus.setLastLocationTime(_time);
			currentStatus.setLat(_latitude);
			currentStatus.setLon(_longitude);
			currentStatus.setSpeed(_speed);
			currentStatus.setStatus(_status);
			_devInfo.setCurrentStatus(currentStatus);			
		}
		return _devInfo;
	}

	public void setDevInfo(DevInfo devInfo)
	{
		_devInfo = devInfo;
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
