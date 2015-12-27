package com.jcsoft.emsystem.processor;

import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.JCProtocol;
import com.jcsoft.emsystem.protocol.KickUserReqFromServer;

public class KickUserReqFromServerProcessor implements IProcessor
{
	private static KickUserReqFromServerProcessor _instance = null;
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(CommonRespFromServerProcessor.instance());
		return true;
	}
	
	public static KickUserReqFromServerProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new KickUserReqFromServerProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		if (protocol.getDataType() != EnumDataType.DATA_KickUserReqFromServer)
		{
			return -1;
		}	
		
		KickUserReqFromServer req = (KickUserReqFromServer)protocol;
		listener.onKicked(req.getReason());
		return 0;
	}

}
