package com.jcsoft.emsystem.tcp;

public interface IJCProtocol
{
	//如果解析编码失败，返回Null
	public byte[] encode();
	
	//如果解析成功，返回0，否则返回其他值
	public int decode(byte[] data, int length);
}
