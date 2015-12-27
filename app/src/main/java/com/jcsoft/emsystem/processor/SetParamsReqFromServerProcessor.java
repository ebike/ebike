package com.jcsoft.emsystem.processor;

import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.client.JCClient;
import com.jcsoft.emsystem.database.ConfigService;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.JCProtocol;
import com.jcsoft.emsystem.protocol.SetParamsReqFromServer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class SetParamsReqFromServerProcessor implements IProcessor
{
	private static SetParamsReqFromServerProcessor _instance = null;
	
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(SetParamsReqFromServerProcessor.instance());
		return true;
	}
			
	public static SetParamsReqFromServerProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new SetParamsReqFromServerProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		if (protocol.getDataType() != EnumDataType.DATA_SetParamsReqFromServer)
		{
			return -1;
		}		
		
		SetParamsReqFromServer req = (SetParamsReqFromServer)protocol;
		//更新参数，如果没有则插入
		HashMap<String, String> params = req.getParams();
		if (params != null && !params.isEmpty())
		{
			Iterator<Entry<String, String>> it = params.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String, String> entry = it.next();
				ConfigService.instance().updateOrInsert(entry.getKey(), entry.getValue());
			}
		}
		
		JCClient.instance().sendCommonResponse(protocol.getProtocolSeq(), protocol.getProtocolId(), (byte)0);
		return 0;
	}

}
