package com.jcsoft.emsystem.client;

import android.content.Context;
import android.location.Location;

import com.jcsoft.emsystem.processor.ProcessorManager;
import com.jcsoft.emsystem.protocol.CommonResp;
import com.jcsoft.emsystem.protocol.EnumDataType;
import com.jcsoft.emsystem.protocol.EnumSubDataType;
import com.jcsoft.emsystem.protocol.GetAllDevInfosReq;
import com.jcsoft.emsystem.protocol.GetPositionReq;
import com.jcsoft.emsystem.protocol.GetTrackReq;
import com.jcsoft.emsystem.protocol.HeartBeatReq;
import com.jcsoft.emsystem.protocol.JCProtocol;
import com.jcsoft.emsystem.protocol.JCProtocolScanner;
import com.jcsoft.emsystem.protocol.LoginReq;
import com.jcsoft.emsystem.protocol.LoginResp;
import com.jcsoft.emsystem.protocol.LogoutReq;
import com.jcsoft.emsystem.protocol.ModifyPasswordReq;
import com.jcsoft.emsystem.protocol.QueryParamsReqFromServerResp;
import com.jcsoft.emsystem.protocol.RealLocateReq;
import com.jcsoft.emsystem.protocol.RemoteCtrlReq;
import com.jcsoft.emsystem.protocol.UploadLocationReq;
import com.jcsoft.emsystem.tcp.ITCPConnectionListener;
import com.jcsoft.emsystem.tcp.SendDataModel;
import com.jcsoft.emsystem.tcp.TCPConnection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

public class JCClient implements ITCPConnectionListener
{
	private final String TAG = "JCSoft";
	private String _userId = "";
	private String _password = "";
	private int _timeout = 30000;  //如果在_timeout毫秒之内没有收到服务器的响应，则认为出错
	private IJCClientListener _listener = null;
	private static JCClient _instance = null;
	private Timer _sendTimer = null;
	private HashMap<TimerTask, SendDataModel> _sendTimerTasks = new HashMap<TimerTask, SendDataModel>();
	private Object _tasksLock = new Object();
	private boolean _isLogined = false;
	private boolean _isLogining = false; //过滤掉重复的登录消息
	
	
	public static JCClient instance()
	{
		if (_instance == null)
		{
			_instance = new JCClient();
		}
		return _instance;
	}
	
	private JCClient()
	{
	}
	
	public void init(Context context, String site, int port)
	{
		TCPConnection.instance().setListener(this);
		TCPConnection.instance().init(context, site, port);	
		TCPConnection.instance().setScanner(new JCProtocolScanner());
		//五分钟发送一个心跳包，如果超过5分钟手机软件没有发送任何信息到服务器
		//服务器会认为手机软件已经掉线，会主动断开连接
	}
	
	public void uninit()
	{
		TCPConnection.instance().uninit();
	}	
		
	//此处假定上层每隔一段时间进行调用，而不是连续调用；如果是连续调用，则只对最后一次调用进行判断
	private void sendProtocol(JCProtocol protocol)
	{
		JCLog.d(TAG, "Send:  " + protocol.getDataType().toString() + "! _isLogined = " + String.valueOf(_isLogined));
		if (!_isLogined && protocol.getDataType() == EnumDataType.DATA_LogoutReq)
		{
			return;
		}
		
		SendDataModel dataModel = new SendDataModel(protocol);
		if (protocol.getDataType() != EnumDataType.DATA_LoginReq)
		{
			if (!isLogined())
			{
				if (protocol.getDataType() != EnumDataType.DATA_LogoutReq)
				{
					//没有登录成功时，过滤掉不必要的网络访问，但触发一次登录操作，去连接一次服务器
					onSendFailed(dataModel);
					login(_userId, _password);
					return;
				}
			}
		}
		
		TCPConnection.instance().send(dataModel);
		
		//控制心跳
		if (protocol != null && protocol.getDataType() != EnumDataType.DATA_HeartBeatReq)
		{
			HeartBeatManager.instance().reset();
		}

		synchronized(_tasksLock)
		{
			if (_sendTimer == null)
			{
				_sendTimer = new Timer();
			}
			
			TimerTask task = new TimerTask()
				{
					@Override
					public void run()
					{
						synchronized(_tasksLock)
						{
							SendDataModel dataModel = _sendTimerTasks.get(this);
							if (dataModel == null)
							{
								return;
							}
							dataModel.setTimeout(true);
							onSendFailed(dataModel);
						}
					}
				};
				
			_sendTimerTasks.put(task, dataModel);
			//设置定时器
			_sendTimer.schedule(task, _timeout);
		}
	}

	public void login(String userId, String password)
	{
		if (_isLogining)
		{
			return;
		}

		_userId = userId;
		_password = password;
		_isLogining = true;
		JCProtocol protocol = new LoginReq(_userId, _password);
		sendProtocol(protocol);
	}
	
	public void updateStorePassword(String newPassword)
	{
		_password = newPassword;
	}
	
	public void logout()
	{
		JCProtocol protocol = new LogoutReq(_userId);
		sendProtocol(protocol);
	}
	
	//获取所有的设备信息
	public void getAllDevInfos()
	{
		JCProtocol protocol = new GetAllDevInfosReq();
		sendProtocol(protocol);
	}
	
	//获取某个设备的具体位置信息
	public void getPosition(int devId)
	{
		JCProtocol protocol = new GetPositionReq(devId);
		sendProtocol(protocol);
	}
	
	//获取某个设备某段时间内的轨迹信息
	public void getTrack(int devId, String beginTime, String endTime)
	{
		JCProtocol protocol = new GetTrackReq(devId, beginTime, endTime);
		sendProtocol(protocol);
	}
	
	//打开电子围栏
	public void openVF(int devId, int range)
	{
		JCProtocol protocol = new RemoteCtrlReq(devId, EnumSubDataType.SUBDATA_openVF, range);
		sendProtocol(protocol);
	}
	
	//关闭电子围栏
	public void closeVF(int devId)
	{
		JCProtocol protocol = new RemoteCtrlReq(devId, EnumSubDataType.SUBDATA_closeVF, 0);
		sendProtocol(protocol);
	}
	
	//远程锁车
	public void lockCar(int devId)
	{
		JCProtocol protocol = new RemoteCtrlReq(devId, EnumSubDataType.SUBDATA_lockCar, 0);
		sendProtocol(protocol);
	}
	
	//取消远程锁车
	public void unlockCar(int devId)
	{
		JCProtocol protocol = new RemoteCtrlReq(devId, EnumSubDataType.SUBDATA_unlockCar, 0);
		sendProtocol(protocol);
	}
	
	//实时跟踪，跟踪有效期为validTime秒，每隔interval上传更新一次数据
	public void realLocate(String devId, int interval, int validTime)
	{
		JCProtocol protocol = new RealLocateReq(devId, interval, validTime);
		sendProtocol(protocol);
	}
	
	//上传自己的位置信息
	public void uploadLocation(Location location, int currentSatelliteCount)
	{		
		JCProtocol protocol = new UploadLocationReq(location, currentSatelliteCount);
		sendProtocol(protocol);
	}
	
	public void sendHeartBeat()
	{
		JCProtocol protocol = new HeartBeatReq();
		sendProtocol(protocol);
	}
	
	public void sendParams(int seqNo, HashMap<String, String> params)
	{
		JCProtocol protocol = new QueryParamsReqFromServerResp(seqNo, params);
		sendProtocol(protocol);
	}

	public void sendCommonResponse(int protocolSeq, int protocolId, byte result)
	{
		JCProtocol protocol = new CommonResp(protocolSeq, protocolId, result);
		sendProtocol(protocol);
	}

	public void modifyPassword(String str)
	{
		//修改密码
		JCProtocol protocol = new ModifyPasswordReq(_userId, _password, str);
		sendProtocol(protocol);		
	}
	
	public IJCClientListener getListener() 
	{
		return _listener;
	}

	public void setListener(IJCClientListener listener)
	{
		_listener = listener;
	}
	
	@Override
	public void onSendFailed(SendDataModel data)
	{				
		_isLogining = false;
		synchronized(_tasksLock)
		{
			Iterator<Entry<TimerTask, SendDataModel>> it = _sendTimerTasks.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<TimerTask, SendDataModel> entry = it.next();
				TimerTask task = entry.getKey();
				SendDataModel dataModel = entry.getValue();
				if (dataModel == data)
				{
					_sendTimerTasks.remove(task);  //仅仅移除，不做其它处理
					break;
				}
			}
		}
		
		if (_listener == null)
		{
			return;
		}
		
		synchronized(_listener)
		{	
			JCProtocol protocol = (JCProtocol)data.getProtocol();
			switch(protocol.getDataType())
			{
			case DATA_UNKNOWN:
				break;
			case DATA_LoginReq:
				if (data.isTimeout())
				{
					_listener.onLoginResult(-1000);
				}
				else if (data.isDisconnected())
				{
					_listener.onLoginResult(-10000);
				}
				else
				{
					_listener.onLoginResult(-1);
				}
				break;
			case DATA_LogoutReq:
				break;
			case DATA_GetAllDevInfosReq:
				_listener.onGetAllDevInfos(null);
				break;
			case DATA_GetPositionReq:
				GetPositionReq gpr = (GetPositionReq)protocol;
				_listener.onGetPosition(gpr.getDevId(), null);
				break;
			case DATA_GetTrackReq:
				_listener.onGetTrack(null);
				break;
			case DATA_RemoteCtrlReq:
				RemoteCtrlReq req = (RemoteCtrlReq)protocol;
				switch(protocol.getSubDataType())
				{
				case SUBDATA_UNKNOWN:
					break;
				case SUBDATA_openVF:
					_listener.onOpenVF(req.getDevId(), -1);
					break;
				case SUBDATA_closeVF:
					_listener.onCloseVF(req.getDevId(), -1);
					break;
				case SUBDATA_lockCar:
					_listener.onLockCar(req.getDevId(), -1);
					break;
				case SUBDATA_unlockCar:
					_listener.onUnlockCar(req.getDevId(), -1);
					break;
				}
				break;			
			case DATA_RealLocateReq:
				_listener.onRealLocate(-1);
				break;
			case DATA_ModifyPasswordReq:
				_listener.onModifyPassword(-1);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void onRecvData(byte[] data, int dataLength)
	{
		_isLogining = false;
		JCLog.d(TAG, "Recv data length:" + String.valueOf(dataLength));
		if (_sendTimer != null)
		{
			synchronized(_tasksLock)
			{
				//如果收到数据则认为网络已经畅通，就假定所有的定时器已经不再需要了
				_sendTimerTasks.clear();
			}
		}
		
		//此处需要注意，针对获取轨迹、远程控制命令，如果超过指定的时间返回会如何处理？
		//例如发送了一个锁车命令，服务器会立即返回控制结果，该控制结果仅仅表明服务器
		//收到了该命令，但并不能说明操作已经成功，是否成功需要看后续收到的状态通知。
		
		//收到的一个完整协议包交由上层处理
		JCProtocol protocol = JCProtocol.makeNewJCProtocol(data, dataLength);
		if (protocol == null)
		{
			//解析失败，什么也不处理
			return;
		}
		JCLog.d(TAG, "Recv: " + protocol.getDataType().toString());
		
		if (protocol.getDataType() == EnumDataType.DATA_LoginResp)
		{
			LoginResp resp = (LoginResp)protocol;
			if (resp.getResult() == 0 || resp.getResult() == 4)
			{
				setLogined(true);
			}
		}
		
		ProcessorManager.instance().process(protocol, _listener);
	}

	@Override
	public void onConnectFailed()
	{
		_isLogining = false;
		_isLogined = false;
		if (_listener == null)
		{
			return;
		}
		
		JCLog.d(TAG, "onConnectFailed()");
		synchronized(_listener)
		{
			_listener.onConnectFailed();
		}
	}

	@Override
	public void onConnected()
	{
		//如果正在登录，则不向上通知了
		if (_isLogining)
		{
			return;
		}
		
		//自动登录
		synchronized(_listener)
		{
			if (_listener == null)
			{
				return;
			}
			_listener.onConnected();
		}
	}

	public boolean isLogined()
	{
		return _isLogined;
	}

	public void setLogined(boolean isLogined)
	{
		_isLogined = isLogined;
	}
}
