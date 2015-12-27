package com.jcsoft.emsystem.protocol;

import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadLocationReq extends JCProtocol
{
	private Location _location = null;
	private int _currentSatelliteCount = 0;
	
	public UploadLocationReq(Location location, int currentSatelliteCount)
	{
		super(EnumDataType.DATA_UploadLocationReq);
		_location = location;
		_currentSatelliteCount = currentSatelliteCount;
		encodeContent();
	}

	@Override
	public void encodeContent()
	{
		if (_location == null)
		{
			return;
		}
		
		//卫星个数
		int satelliteCount = _currentSatelliteCount;
		//状态位
		int status = 0;
		//经度
		int lon = (int)(_location.getLongitude() * 1000000);
		//纬度
		int lat = (int)(_location.getLatitude() * 1000000);		
		//高程
		int height = (int)_location.getAltitude();
		//速度
		int speed = (int)_location.getSpeed();
		//方向
		int direction = (int)_location.getBearing();
		//时间,由于取得的时间不对，所以采用直接取系统时间
		SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Date dt = cal.getTime();
		String time = sdf.format(dt);
		
		byte[] content = new byte[23];
		content[0] = (byte) satelliteCount;
		content[1] = (byte) status;
		
		System.arraycopy(changeIntToBcd(lon, 5), 0, content, 2, 5);
		System.arraycopy(changeIntToBcd(lat, 4), 0, content, 7, 4);
		System.arraycopy(changeIntToBcd(height, 2), 0, content, 11, 2);
		System.arraycopy(changeIntToBcd(speed, 2), 0, content, 13, 2);
		System.arraycopy(changeIntToBcd(direction, 2), 0, content, 15, 2);
		System.arraycopy(timeToBCD(time), 0, content, 17, 6);
		
		setContent(content);
	}

}
