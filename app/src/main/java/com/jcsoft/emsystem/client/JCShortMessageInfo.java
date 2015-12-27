package com.jcsoft.emsystem.client;

public class JCShortMessageInfo 
{
	private int _devId = 0;
	private String _content;
	
	public JCShortMessageInfo()
	{
		
	}
	
	public JCShortMessageInfo(int devId, String content)
	{
		_devId = devId;
		_content = content;
	}

	public String getContent()
	{
		return _content;
	}

	public void setContent(String content)
	{
		_content = content;
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
