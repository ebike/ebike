package com.jcsoft.emsystem.protocol;

public class RemoteCtrlResp extends JCProtocol
{
	private int _seqNo = 0;
	private int _ctrlType = 0;
	private int _devId = 0;
	private int _result = 0;
	
	public RemoteCtrlResp()
	{
		super(EnumDataType.DATA_RemoteCtrlResp);
	}


	@Override
	public void decodeContent()
	{
		_seqNo = byteArrayToInt(_content, 0, 2);
		_ctrlType = _content[2];
		EnumSubDataType subDataType = EnumSubDataType.SUBDATA_UNKNOWN;
		switch(_ctrlType)
		{
		case 0x01:
			subDataType = EnumSubDataType.SUBDATA_openVF;
			break;
		case 0x02:
			subDataType = EnumSubDataType.SUBDATA_closeVF;
			break;
		case 0x03:
			subDataType = EnumSubDataType.SUBDATA_lockCar;
			break;
		case 0x04:
			subDataType = EnumSubDataType.SUBDATA_unlockCar;
			break;
			default:
				break;
		}
		this.setSubDataType(subDataType);
		_devId = byteArrayToInt(_content, 3, 4);
		_result = _content[7];
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


	public int getCtrlType()
	{
		return _ctrlType;
	}


	public void setCtrlType(int ctrlType)
	{
		_ctrlType = ctrlType;
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
