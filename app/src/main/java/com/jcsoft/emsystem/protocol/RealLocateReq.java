package com.jcsoft.emsystem.protocol;


public class RealLocateReq extends JCProtocol
{
	private String _devId = "";
	private int _interval = 0;
	private int _validTime = 0;
	
	public RealLocateReq(String devId, int interval, int validTime)
	{
		super(EnumDataType.DATA_RealLocateReq);
		_devId = devId;
		_interval = interval;
		_validTime = validTime;
	}

	@Override
	public void encodeContent() 
	{
		byte[] content = new byte[8];
		//拷贝设备ID
		System.arraycopy(intTo4Bytes(Integer.valueOf(_devId)), 0, content, 0, 4);
		
		System.arraycopy(intTo2Bytes(_interval), 0, content, 4, 2);
		System.arraycopy(intTo2Bytes(_validTime), 0, content, 6, 2);
	}

}
