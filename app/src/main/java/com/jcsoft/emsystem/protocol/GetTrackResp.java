package com.jcsoft.emsystem.protocol;

import com.jcsoft.emsystem.client.JCLocation;

import java.util.ArrayList;
import java.util.List;

public class GetTrackResp extends JCProtocol
{
	private int _seqNo = 0;
	private int _devId = 0;
	private int _pointsCount = 0;
	private List<JCLocation> _points = new ArrayList<JCLocation>();

	public GetTrackResp()
	{
		super(EnumDataType.DATA_GetTrackResp);
	}

	@Override
	public void decodeContent()
	{
		if (_content == null)
		{
			return;
		}
		
		try
		{
			_seqNo = byteArrayToInt(_content, 0, 2);
			_devId = byteArrayToInt(_content, 2, 4);
			_pointsCount = _content[6];
			
			for(int i=0; i<_pointsCount; i++)
			{
				JCLocation location = new JCLocation();
				location.setLon(bcdToInt(_content, i*20 + 7, 5));
				location.setLat(bcdToInt(_content, i*20 + 12, 4));
				location.setDirection(bcdToInt(_content, i*20 + 16, 2));
				location.setSpeed(bcdToInt(_content, i*20 + 18, 2));
				location.setLastLocationTime(bcdTimeToTimeStr(bcdToStr(_content, i*20 + 20, 6)));
				location.setStatus(_content[i*20 + 26]);
				_points.add(location);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public int getSeqNo()
	{
		return _seqNo;
	}

	public void setSeqNo(int seqNo)
	{
		_seqNo = seqNo;
	}

	public int getDevId()
	{
		return _devId;
	}

	public void setDevId(int devId)
	{
		_devId = devId;
	}

	public int getPointsCount()
	{
		return _pointsCount;
	}

	public void setPointsCount(int pointsCount)
	{
		_pointsCount = pointsCount;
	}

	public List<JCLocation> getPoints()
	{
		return _points;
	}

	public void setPoints(List<JCLocation> points)
	{
		_points = points;
	}

}
