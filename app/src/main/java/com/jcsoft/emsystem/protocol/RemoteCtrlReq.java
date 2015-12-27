package com.jcsoft.emsystem.protocol;


public class RemoteCtrlReq extends JCProtocol
{
	private int _devId = 0;
	private int _extraParam = 0;
	
	public RemoteCtrlReq(int devId, EnumSubDataType subDataType, int extraParam)
	{
		super(EnumDataType.DATA_RemoteCtrlReq);
		setSubDataType(subDataType);
		setDevId(devId);
		_extraParam = extraParam;
	}
	
	@Override
	public void encodeContent() 
	{
		byte[] content = new byte[9];
		switch(getSubDataType())
		{
		case SUBDATA_openVF:
			content[0] = 0x01;
			break;
		case SUBDATA_closeVF:
			content[0] = 0x02;
			break;
		case SUBDATA_lockCar:
			content[0] = 0x03;
			break;
		case SUBDATA_unlockCar:
			content[0] = 0x04;
			break;
			default:
				break;
		}
		
		//拷贝设备ID
		System.arraycopy(intTo4Bytes(Integer.valueOf(getDevId())), 0, content, 1, 4);
		
		System.arraycopy(intTo4Bytes(_extraParam), 0, content, 5, 4);
		
		setContent(content);
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
