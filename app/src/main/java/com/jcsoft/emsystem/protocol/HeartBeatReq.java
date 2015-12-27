package com.jcsoft.emsystem.protocol;

public class HeartBeatReq extends JCProtocol
{

	public HeartBeatReq()
	{
		super(EnumDataType.DATA_HeartBeatReq);
	}

	@Override
	public void encodeContent()
	{
		//消息体为空
	}

}
