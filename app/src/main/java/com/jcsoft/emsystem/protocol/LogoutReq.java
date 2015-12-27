package com.jcsoft.emsystem.protocol;

import java.io.UnsupportedEncodingException;


public class LogoutReq extends JCProtocol
{
	private String _userId = "";
	
	public LogoutReq(String userId)
	{
		super(EnumDataType.DATA_LogoutReq);
		_userId = userId;
	}


	@Override
	public void encodeContent() 
	{
		try
		{
			setContent(_userId.getBytes("GBK"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
}
