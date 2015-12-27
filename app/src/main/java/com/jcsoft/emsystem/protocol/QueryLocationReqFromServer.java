package com.jcsoft.emsystem.protocol;

public class QueryLocationReqFromServer extends JCProtocol
{

	public QueryLocationReqFromServer()
	{
		super(EnumDataType.DATA_QueryLocationReqFromServer);
	}

	@Override
	public void decodeContent()
	{
		//消息体为空
	}

}
