package com.jcsoft.emsystem.processor;

import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.GetAllDevInfosResp;
import com.jcsoft.emsystem.protocol.JCProtocol;

public class GetAllDevInfosRespProcessor implements IProcessor
{
	private static GetAllDevInfosRespProcessor _instance = null;
	
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(GetAllDevInfosRespProcessor.instance());
		return true;
	}
			
	public static GetAllDevInfosRespProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new GetAllDevInfosRespProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		if (protocol.getDataType() != EnumDataType.DATA_GetAllDevInfosResp)
		{
			return -1;
		}	
		
		//通知界面获取到的设备信息
		GetAllDevInfosResp resp = (GetAllDevInfosResp)protocol;
		listener.onGetAllDevInfos(resp.getDevInfos());
		return 0;
	}

}
