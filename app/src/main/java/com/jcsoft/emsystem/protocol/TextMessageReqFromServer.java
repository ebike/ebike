package com.jcsoft.emsystem.protocol;

import java.io.UnsupportedEncodingException;

public class TextMessageReqFromServer extends JCProtocol
{
	private int _devId = 0;        //设备ID
	private int _flag = 0;         //标志位
	private String _text = null;
	
	public TextMessageReqFromServer()
	{
		super(EnumDataType.DATA_TextMessageReqFromServer);
	}

	@Override
	public void decodeContent()
	{
		setDevId(byteArrayToInt(_content, 0, 4));
		setFlag(_content[0]);
		try
		{
			_text = new String(_content, 5, _content.length - 5, "GBK");
		} 
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	public String getText()
	{
		return _text;
	}

	public void setText(String text)
	{
		_text = text;
	}

	public int getFlag()
	{
		return _flag;
	}

	public void setFlag(int flag)
	{
		_flag = flag;
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
