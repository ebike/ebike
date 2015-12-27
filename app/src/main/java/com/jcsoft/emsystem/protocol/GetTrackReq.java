package com.jcsoft.emsystem.protocol;


public class GetTrackReq extends JCProtocol
{
	private int _devId = -1;      //实际为 整数
	private String _beginTime = "";  //格式为"2014-02-05 15:44"
	private String _endTime = "";
	
	public GetTrackReq(int devId, String beginTime, String endTime)
	{
		super(EnumDataType.DATA_GetTrackReq);
		_devId = devId;
		_beginTime = beginTime;
		_endTime = endTime;
	}
	

	@Override
	public void encodeContent() 
	{		
		byte[] content = new byte[16];
		//拷贝设备ID
		System.arraycopy(intTo4Bytes(_devId), 0, content, 0, 4);
		
		//放置开始时间
		System.arraycopy(timeToBCD(_beginTime), 0, content, 4, 6);
		
		//放置结束时间
		System.arraycopy(timeToBCD(_endTime), 0, content, 10, 6);
		setContent(content);
	}

}
