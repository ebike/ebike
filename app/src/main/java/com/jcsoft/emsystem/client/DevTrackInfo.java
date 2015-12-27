package com.jcsoft.emsystem.client;

import java.util.List;

public class DevTrackInfo
{
	private int _devId = -1;
	private List<JCLocation> _tracks = null;
	
	public DevTrackInfo()
	{
		
	}
	
	public DevTrackInfo(int devId, List<JCLocation> points)
	{
		_devId = devId;
		_tracks = points;
	}
	
	public int getDevId()
	{
		return _devId;
	}
	
	public void setDevId(int devId)
	{
		_devId = devId;
	}
	
	public List<JCLocation> getTracks()
	{
		return _tracks;
	}
	
	public void setTracks(List<JCLocation> tracks)
	{
		_tracks = tracks;
	}
}
