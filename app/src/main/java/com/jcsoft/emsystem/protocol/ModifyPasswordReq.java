package com.jcsoft.emsystem.protocol;

import java.io.UnsupportedEncodingException;

public class ModifyPasswordReq extends JCProtocol
{
	private String _userId = "";
	private String _oldPassword = "";
	private String _newPassword = "";
	
	public ModifyPasswordReq(String userId, String oldPassword, String newPassword)
	{
		super(EnumDataType.DATA_ModifyPasswordReq);
		_newPassword = newPassword;
		_userId = userId;
		_oldPassword = oldPassword;
	}

	@Override
	public void encodeContent()
	{		
		String str = _userId + ":" + _oldPassword + ":" + _newPassword;
		try
		{
			setContent(str.getBytes("GBK"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	public String getNewPassword()
	{
		return _newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		_newPassword = newPassword;
	}

}
