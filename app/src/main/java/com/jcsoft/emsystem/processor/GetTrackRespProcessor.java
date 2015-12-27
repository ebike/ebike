package com.jcsoft.emsystem.processor;


import com.jcsoft.emsystem.client.DevTrackInfo;
import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.client.JCLocation;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.GetTrackResp;
import com.jcsoft.emsystem.protocol.JCProtocol;

import java.util.ArrayList;
import java.util.List;

public class GetTrackRespProcessor implements IProcessor
{
	private static GetTrackRespProcessor _instance = null;
	private List<JCLocation> _locations = null;
	
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(GetTrackRespProcessor.instance());
		return true;
	}
			
	public static GetTrackRespProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new GetTrackRespProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		if (protocol.getDataType() != EnumDataType.DATA_GetTrackResp)
		{
			return -1;
		}		
		
		GetTrackResp resp = (GetTrackResp)protocol;
		//看看是否是分包，如果是分包，则等待接收完成
		if (resp.getProtocolTotalCount() == 0)
		{
			listener.onGetTrack(new DevTrackInfo(resp.getDevId(), resp.getPoints()));
		}
		else
		{
			int currentNum = resp.getProtocolCurrentNum();
			if (currentNum == 1)
			{
				_locations = new ArrayList<JCLocation>();
				_locations.addAll(resp.getPoints());
			}
			else if(currentNum == resp.getProtocolTotalCount())
			{
				_locations.addAll(resp.getPoints());
				listener.onGetTrack(new DevTrackInfo(resp.getDevId(), _locations));
			}
			else
			{
				_locations.addAll(resp.getPoints());
			}
		}
		return 0;
	}

}
