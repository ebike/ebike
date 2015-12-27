package com.jcsoft.emsystem.protocol;


public class GetAllDevInfosReq extends JCProtocol
{
	public GetAllDevInfosReq()
	{
		super(EnumDataType.DATA_GetAllDevInfosReq);
	}

	@Override
	public void encodeContent() 
	{
		//消息体为空
	}

}
