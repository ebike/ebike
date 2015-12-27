package com.jcsoft.emsystem.processor;

import com.jcsoft.emsystem.client.CarLockStatus;
import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.client.JCClient;
import com.jcsoft.emsystem.client.VFStatus;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.JCProtocol;
import com.jcsoft.emsystem.protocol.NotifyReqFromServer;

public class NotifyReqFromServerProcessor implements IProcessor
{
	private static NotifyReqFromServerProcessor _instance = null;
	
	public boolean init()
	{
		ProcessorManager.instance().registerProcessor(NotifyReqFromServerProcessor.instance());
		return true;
	}
			
	public static NotifyReqFromServerProcessor instance()
	{
		if (_instance == null)
		{
			_instance = new NotifyReqFromServerProcessor();
		}
		return _instance;
	}
	
	@Override
	public int process(JCProtocol protocol, IJCClientListener listener)
	{
		if (protocol.getDataType() != EnumDataType.DATA_NotifyReqFromServer)
		{
			return -1;
		}		
		
		NotifyReqFromServer notify = (NotifyReqFromServer)protocol;
		//根据notify中的状态类型更新界面

		//状态类型定义
		//0x01	电子围栏
		//0x02	车辆锁定状态
		
		//状态值定义
//		0x01	状态类型为0x01时使用，表示电子围栏打开
//		0x02	状态类型为0x01时使用，表示电子围栏关闭
//		0x03	状态类型为0x02时使用，表示车辆已锁定
//		0x04	状态类型为0x02时使用，表示车辆已解锁
//		0x05	状态类型为0x01、0x02时都可使用，操作失败
//		其他值	保留

		switch(notify.getType())
		{
		case 0x01:
			notifyVFS(listener, notify);
			break;
		case 0x02:
			notifyCLS(listener, notify);
			break;
			default:
				break;
		}

		JCClient.instance().sendCommonResponse(protocol.getProtocolSeq(), protocol.getProtocolId(), (byte)0);
		return 0;
	}

	private void notifyVFS(IJCClientListener listener, NotifyReqFromServer notify)
	{
		VFStatus.EnumVFS status = VFStatus.EnumVFS.VFS_UNKNOWN;
		switch (notify.getValue())
		{
		case 0x01:
			status = VFStatus.EnumVFS.VFS_Opened;
			break;
		case 0x02:
			status = VFStatus.EnumVFS.VFS_Closed;
			break;
		case 0x05:
			status = VFStatus.EnumVFS.VFS_Unusable;
			break;
		case 0x06:
			status = VFStatus.EnumVFS.VFS_Alarm;
			break;
			default:
				break;
		}
		VFStatus vfs = new VFStatus(notify.getDevId(), status, notify.getLongitude(), notify.getLatitude()
				, notify.getStatusTime());
		listener.onVFStatusChanged(vfs);
	}
	
	private void notifyCLS(IJCClientListener listener, NotifyReqFromServer notify)
	{
		CarLockStatus.EnumCLS status = CarLockStatus.EnumCLS.CLS_UNKNOWN;
		switch (notify.getValue())
		{
		case 0x03:
			status = CarLockStatus.EnumCLS.CLS_Locked;
			break;
		case 0x04:
			status = CarLockStatus.EnumCLS.CLS_Unlocked;
			break;
		case 0x05:
			status = CarLockStatus.EnumCLS.CLS_Unusable;
			break;
			default:
				break;
		}
		CarLockStatus cls = new CarLockStatus(notify.getDevId(), status);
		listener.onCarLockStatusChanged(cls);
	}

}
