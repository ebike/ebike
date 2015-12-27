package com.jcsoft.emsystem.processor;

import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.client.JCClient;
import com.jcsoft.emsystem.constants.JCConstValues;
import com.jcsoft.emsystem.database.ConfigService;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.JCProtocol;

import java.util.HashMap;

public class QueryParamsReqFromServerProcessor implements IProcessor
{
	private static QueryParamsReqFromServerProcessor _instance = null;
	
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(QueryParamsReqFromServerProcessor.instance());
		return true;
	}
			
	public static QueryParamsReqFromServerProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new QueryParamsReqFromServerProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		if (protocol.getDataType() != EnumDataType.DATA_QueryParamsReqFromServer)
		{
			return -1;
		}		
		
		//从数据库获取相应参数并返回给服务器，此处可以直接操作		
		HashMap<String, String> kvs = new HashMap<String, String>();
		kvs.put(JCConstValues.S_VFRange
				, ConfigService.instance().getConfigValue(JCConstValues.S_VFRange));
		kvs.put(JCConstValues.S_UploadLocationInterval
				, ConfigService.instance().getConfigValue(JCConstValues.S_UploadLocationInterval));
		kvs.put(JCConstValues.S_LoactionProvider
				, ConfigService.instance().getConfigValue(JCConstValues.S_LoactionProvider));
		
		JCClient.instance().sendParams(protocol.getProtocolSeq(), kvs);
		return 0;
	}

}
