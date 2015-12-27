package com.jcsoft.emsystem.client;

import java.util.Timer;
import java.util.TimerTask;

public class HeartBeatManager
{
	//每次过一段时间，发送一条心跳信息到服务器
	private Timer _heartTimer = null;
	private static HeartBeatManager _instance = null;
	private boolean _isStarted = false;
	private long _period = 5*60 * 1000;
	
	public static HeartBeatManager instance()
	{
		if (_instance == null)
		{
			_instance = new HeartBeatManager();
		}
		return _instance;
	}
	
	public void destroy()
	{
		_instance = null;
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		stop();
		super.finalize();
	}
	
	public int start(int period)
	{
		if (_isStarted)
		{
			return -1;
		}
		_isStarted = true;
		_period = period;
		
		startTimer();
		return 0;
	}
	
	private void startTimer()
	{		
		_heartTimer = new Timer();
		_heartTimer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					//发送心跳消息到服务器
					JCClient.instance().sendHeartBeat();
				}
			}, _period, _period);
	}
	
	public int stop()
	{
		if (!_isStarted)
		{
			return -1;
		}
		
		if (_heartTimer != null)
		{
			_heartTimer.cancel();
			_heartTimer = null;
		}
		
		_isStarted = false;
		return 0;
	}
	
	//重置定时器，保证
	public int reset()
	{
		if (!_isStarted)
		{
			return -1;
		}
		
		if (_heartTimer != null)
		{
			_heartTimer.cancel();
			_heartTimer = null;
		}
		
		startTimer();
		return 0;
	}
}
