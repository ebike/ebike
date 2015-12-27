package com.jcsoft.emsystem.protocol;

public class CommonRespFromServer extends JCProtocol
{
	private int _respSeq = 0;
	private int _respId = 0;
	private byte _result = 0x00;
	
	public CommonRespFromServer()
	{
		super(EnumDataType.DATA_CommonRespFromServer);
	}

	@Override
	public void decodeContent()
	{
		if (_content == null)
		{
			return;
		}
		
		setRespSeq(byteArrayToInt(_content, 0, 2));
		setRespId(byteArrayToInt(_content, 2, 2));
		setResult(_content[4]);
	}


	public int getRespSeq()
	{
		return _respSeq;
	}


	public void setRespSeq(int respSeq)
	{
		_respSeq = respSeq;
	}


	public int getRespId()
	{
		return _respId;
	}


	public void setRespId(int respId)
	{
		_respId = respId;
	}


	public byte getResult()
	{
		return _result;
	}


	public void setResult(byte result)
	{
		_result = result;
	}

}
