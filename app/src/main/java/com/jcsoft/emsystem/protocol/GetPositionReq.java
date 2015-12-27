package com.jcsoft.emsystem.protocol;


public class GetPositionReq extends JCProtocol
{
	private int _devId = -1;
	
	public GetPositionReq(int devId)
	{
		super(EnumDataType.DATA_GetPositionReq);
		setDevId(devId);
	}

	@Override
	public void encodeContent() 
	{
		if (getDevId() == -1)
		{
			return;
		}
		
		setContent(intTo4Bytes(Integer.valueOf(getDevId())));
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
