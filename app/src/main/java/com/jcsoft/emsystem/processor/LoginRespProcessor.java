package com.jcsoft.emsystem.processor;

import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.JCProtocol;
import com.jcsoft.emsystem.protocol.LoginResp;

public class LoginRespProcessor implements IProcessor
{
	private static LoginRespProcessor _instance = null;
	
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(LoginRespProcessor.instance());
		return true;
	}
			
	public static LoginRespProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new LoginRespProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		//判断是不是自己需要处理的，如果不是则返回-1，是则返回0
		if (protocol.getDataType() != EnumDataType.DATA_LoginResp)
		{
			//解析内容
			return -1;
		}
		
		LoginResp resp = (LoginResp)protocol;
		listener.onLoginResult(resp.getResult());		
		return 0;
	}

}
