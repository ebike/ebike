package com.jcsoft.emsystem.processor;

import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.protocol.JCProtocol;

public interface IProcessor
{
	//如果成功处理，返回0，否则返回其它值
	int process(JCProtocol protocol, IJCClientListener listener);
}
