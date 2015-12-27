package com.jcsoft.emsystem.tcp;

public interface ITCPConnectionListener
{
	//自动连接服务器成功
	void onConnected();
	
	//连接服务器失败或者断线通过该方法回调
	void onConnectFailed();
	
	//如果发送失败，则在此回调给上层，上层不能在此函数中直接操作界面,并不应有耗时特别长的处理
	void onSendFailed(SendDataModel data);
	
	//TCPConnection会对收到的数据进行解析分包，保证此处回调的数据都是一个协议包
	//上层不能再此函数中直接操作界面,并不应有耗时特别长的处理
	//，并且内存data在onRecvData返回后可能会立即被重写
	void onRecvData(byte[] data, int dataLength);
}
