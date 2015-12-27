package com.jcsoft.emsystem.processor;

import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.JCProtocol;
import com.jcsoft.emsystem.protocol.RemoteCtrlResp;

public class RemoteCtrlRespProcessor implements IProcessor
{
	private static RemoteCtrlRespProcessor _instance = null;
	
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(RemoteCtrlRespProcessor.instance());
		return true;
	}
			
	public static RemoteCtrlRespProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new RemoteCtrlRespProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		if (protocol.getDataType() != EnumDataType.DATA_RemoteCtrlResp)
		{
			return -1;
		}		
		
		//上层提示发送成功，针对远程控制设备的命令来说，需要根据后续的状态变化通知来控制设备
		RemoteCtrlResp resp = (RemoteCtrlResp)protocol;
		int result = resp.getResult();
		switch(resp.getSubDataType())
		{
		case SUBDATA_openVF:
			listener.onOpenVF(resp.getDevId(), result);
			break;
		case SUBDATA_closeVF:
			listener.onCloseVF(resp.getDevId(), result);
			break;
		case SUBDATA_lockCar:
			listener.onLockCar(resp.getDevId(), result);
			break;
		case SUBDATA_unlockCar:
			listener.onUnlockCar(resp.getDevId(), result);
			break;
			default:
				break;
		}
		return 0;
	}

}
