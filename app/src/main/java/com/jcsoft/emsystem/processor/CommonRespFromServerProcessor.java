package com.jcsoft.emsystem.processor;

import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.protocol.CommonRespFromServer;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.JCProtocol;

public class CommonRespFromServerProcessor implements IProcessor
{
	private static CommonRespFromServerProcessor _instance = null;
	
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(CommonRespFromServerProcessor.instance());
		return true;
	}
			
	public static CommonRespFromServerProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new CommonRespFromServerProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		if (protocol.getDataType() != EnumDataType.DATA_CommonRespFromServer)
		{
			return -1;
		}
		
		CommonRespFromServer resp = (CommonRespFromServer)protocol;
		EnumDataType dataType = JCProtocol.getDataTypeById(resp.getRespId());
		if (dataType == null || dataType == EnumDataType.DATA_UNKNOWN)
		{
			//如果发现该响应对应的请求已经不存在了，则认为之前已经处理过，不再做任何处理
			return 0;
		}
		
		int result = resp.getResult();  //判断是哪一次控制失败了，然后反馈到界面上		
		
		switch(dataType)
		{
		case DATA_RealLocateReq:
			//临时位置跟踪失败
			listener.onRealLocate(result);
			break;
		case DATA_UploadLocationReq:
			break;
		case DATA_ModifyPasswordReq:
			listener.onModifyPassword(result);
			break;
			default:
				break;
		}
		return 0;
	}

}
