package com.jcsoft.emsystem.protocol;

import java.io.UnsupportedEncodingException;

public class LoginReq extends JCProtocol
{
	private String _userId = "";
	private String _password = "";
	
	public LoginReq(String userId, String password)
	{
		super(EnumDataType.DATA_LoginReq);
		_userId = userId;
		_password = password;	
	}
	
	@Override
	public void encodeContent() 
	{		
		String str = _userId + ":" + _password;
		try
		{
			setContent(str.getBytes("GBK"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	
}
