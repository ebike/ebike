package com.jcsoft.emsystem.protocol;

import java.io.UnsupportedEncodingException;

public class LoginResp extends JCProtocol
{
	private int _seqNo = 0;
	private int _result = 0;
	private String _authCode = null;
	
	public LoginResp()
	{
		super(EnumDataType.DATA_LoginResp);
	}

	@Override
	public void decodeContent()
	{
		_seqNo = byteArrayToInt(_content, 0, 2);
		_result = _content[2];
		try
		{
			byte[] authCodeArray = new byte[_content.length - 3];
			System.arraycopy(_content, 3, authCodeArray, 0, _content.length - 3);
			_authCode = new String(authCodeArray, "GBK");
		} 
		catch (UnsupportedEncodingException e)
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

	public int getResult()
	{
		return _result;
	}

	public void setResult(int result)
	{
		_result = result;
	}

	public String getAuthCode()
	{
		return _authCode;
	}

	public void setAuthCode(String authCode)
	{
		_authCode = authCode;
	}

}
