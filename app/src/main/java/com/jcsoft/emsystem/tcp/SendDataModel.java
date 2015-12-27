package com.jcsoft.emsystem.tcp;


public class SendDataModel 
{
	private int _seq = 0;
	private IJCProtocol _protocol = null;
	private boolean _isExit = false;
	private boolean _isTimeout = false;
	private static int _seed = 0;
	private final static int MAX_SEQ = 40000000;
	private boolean _isDisconnected = false;
	
	public SendDataModel(IJCProtocol protocol)
	{
		setProtocol(protocol);
		_seq = getNextSeq();
	}
	
	public SendDataModel(boolean isExit)
	{
		_isExit = isExit;
	}

	private static int getNextSeq() 
	{
		if (_seed >= MAX_SEQ)
		{
			_seed = 0;
		}
		_seed++;
		return _seed;
	}

	public int getSeq() 
	{
		return _seq;
	}

	public void setSeq(int seq) 
	{
		_seq = seq;
	}

	public IJCProtocol getProtocol()
	{
		return _protocol;
	}

	public void setProtocol(IJCProtocol protocol)
	{
		_protocol = protocol;
	}

	public boolean isExit()
	{
		return _isExit;
	}

	public void setExit(boolean isExit)
	{
		_isExit = isExit;
	}

	public boolean isTimeout()
	{
		return _isTimeout;
	}

	public void setTimeout(boolean isTimeout)
	{
		_isTimeout = isTimeout;
	}

	public boolean isDisconnected()
	{
		return _isDisconnected;
	}

	public void setDisconnected(boolean isDisconnected)
	{
		_isDisconnected = isDisconnected;
	}
	
}
