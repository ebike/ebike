package com.jcsoft.emsystem.client;

import com.jcsoft.emsystem.activity.MainActivity;

import java.util.List;

public class JCClientListener implements IJCClientListener
{
	private MainActivity _mainActivity = null;
	
	public class KeyValue
	{	
		public String Key = null;
		public String Value = null;
		
		public KeyValue()
		{
		}
		
		public KeyValue(String key, String value)
		{
			Key = key;
			Value = value;
		}
	}
	
	public JCClientListener(MainActivity activity)
	{
		_mainActivity = activity;
	}
	
	@Override
	public void onLoginResult(int result)
	{
//		sendMsg(MainActivity.MSG_onLoginResult, result, 0, null);
	}

	@Override
	public void onGetAllDevInfos(List<DevInfo> infos)
	{
//		sendMsg(MainActivity.MSG_onGetAllDevInfos, 0, 0, infos);
	}

	@Override
	public void onGetPosition(int devId, DevInfo info)
	{		
//		sendMsg(MainActivity.MSG_onGetPosition, devId, 0, info);
	}

	@Override
	public void onGetTrack(DevTrackInfo info)
	{
//		sendMsg(MainActivity.MSG_onGetTrack, 0, 0, info);
	}

	@Override
	public void onOpenVF(int devId, int result)
	{
//		sendMsg(MainActivity.MSG_onOpenVF, devId, result, null);
	}

	@Override
	public void onCloseVF(int devId, int result)
	{
//		sendMsg(MainActivity.MSG_onCloseVF, devId, result, null);
	}

	@Override
	public void onLockCar(int devId, int result)
	{
//		sendMsg(MainActivity.MSG_onLockCar, devId, result, null);
	}

	@Override
	public void onUnlockCar(int devId, int result)
	{
//		sendMsg(MainActivity.MSG_onUnlockCar, devId, result, null);
	}

	@Override
	public void onGetSysParam(String key, String value)
	{
//		sendMsg(MainActivity.MSG_onGetSysParam, 0, 0, new KeyValue(key, value));
	}

	@Override
	public void onRealLocate(int result)
	{
//		sendMsg(MainActivity.MSG_onRealLocation, result, 0, null);
	}

	@Override
	public void onReceivedMessage(JCShortMessageInfo message)
	{
//		sendMsg(MainActivity.MSG_onReceivedMessage, 0, 0, message);
	}

	@Override
	public void onConnectFailed()
	{
//		sendMsg(MainActivity.MSG_onConnectFailed, 0, 0, null);
	}

	@Override
	public void onConnected()
	{
//		sendMsg(MainActivity.MSG_onConnected, 0, 0, null);
	}

	@Override
	public void onVFStatusChanged(VFStatus status)
	{
//		sendMsg(MainActivity.MSG_onVFStatusChanged, 0, 0, status);
	}

	@Override
	public void onCarLockStatusChanged(CarLockStatus status)
	{
//		sendMsg(MainActivity.MSG_onCarLockStatusChanged, 0, 0, status);
	}

	@Override
	public void onModifyPassword(int result)
	{
//		sendMsg(MainActivity.MSG_onModifyPassword, result, 0, null);
	}

	
	private void sendMsg(int what, int arg1, int arg2, Object obj)
	{
//		Message msg = Message.obtain(_mainActivity._handler, what, arg1, arg2, obj);
//		msg.sendToTarget();
	}

	@Override
	public void onKicked(int reason)
	{
//		sendMsg(MainActivity.MSG_onKicked, reason, 0, null);
	}
}
