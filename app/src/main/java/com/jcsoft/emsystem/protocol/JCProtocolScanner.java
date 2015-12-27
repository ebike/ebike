package com.jcsoft.emsystem.protocol;

import com.jcsoft.emsystem.client.JCLog;
import com.jcsoft.emsystem.tcp.IJCProtocolScanner;

public class JCProtocolScanner implements IJCProtocolScanner
{
	//状态机：NewProtocol ReadingHeader PartialBody
	private enum EnumParseState
	{
		NewProtocol,          //这是一条新消息
		ReadingHeader,        //正在读取消息头
		PartialBody           //解析出来部分消息体
	}
	
	private final String TAG = "JCSoft";
	private EnumParseState _state = EnumParseState.NewProtocol;
	private final byte PROTOCOL_MARK = 0x7E;
	private final int HEADER_LENGTH = 11;
	private final int FOOTER_LENGTH = 2;
	private int _lastContentLength = 0;
	
	@Override
	public ScanResult scanBuffer(byte[] buffer, int offset, int length)
	{
		try
		{
			JCLog.d(TAG, "scanBuffer: offset = " + String.valueOf(offset) + "! length = " + String.valueOf(length));
			if (buffer == null || offset < 0 || buffer.length <= offset || buffer.length < length + offset || length <= 0)
			{
				return new ScanResult(EnumScanResult.RESULT_Error, 0);
			}
			
			switch(_state)
			{
			case NewProtocol:
				//解析消息起始标识位
				if (buffer[offset] != PROTOCOL_MARK)
				{
					return new ScanResult(EnumScanResult.RESULT_Error, 0);
				}
				_state = EnumParseState.ReadingHeader;			
				//注意此处没有break，将顺序往下执行
				
			case ReadingHeader:			
				//解析消息头
				if (length < HEADER_LENGTH)
				{
					//消息头没有接受完，不再解析，接着接收数据
					_state = EnumParseState.ReadingHeader;
					int unprocessedPos = offset + length;
					return new ScanResult(EnumScanResult.RESULT_NeedNextBuffer, unprocessedPos);
				}
				
				//能够解析出来完整的消息头，则求消息体长度 
				_lastContentLength = getLength(buffer, offset, length);
				if (_lastContentLength > length - HEADER_LENGTH - FOOTER_LENGTH)
				{
					//没有接收完
					_state = EnumParseState.PartialBody;
					int unprocessedPos = offset + length;
					return new ScanResult(EnumScanResult.RESULT_NeedNextBuffer, unprocessedPos);
				}
				
				//收到的数据中有一个完整的协议包，需要解析出来
				_state = EnumParseState.NewProtocol;   //保证下一次进来的时候还是正确的数据
				return new ScanResult(EnumScanResult.RESULT_End
						, offset + HEADER_LENGTH + _lastContentLength + FOOTER_LENGTH);
				
			case PartialBody:
				//收到了第二个包，可能还没有收完
				if (_lastContentLength > length - HEADER_LENGTH - FOOTER_LENGTH)
				{
					//还是没有接收完
					_state = EnumParseState.PartialBody;
					return new ScanResult(EnumScanResult.RESULT_NeedNextBuffer, 0);
				}		
				else
				{
					_state = EnumParseState.NewProtocol;
					return new ScanResult(EnumScanResult.RESULT_End
							, offset + HEADER_LENGTH + _lastContentLength + FOOTER_LENGTH);
				}	
				default:
					break;
			}
			ScanResult result = new ScanResult(EnumScanResult.RESULT_Error, 0);
			return result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ScanResult(EnumScanResult.RESULT_Error, 0);
		}
	}

	private int getLength(byte[] buffer, int offset, int length)
	{
		//起始标志位(1)  消息ID(2) 消息体属性(2)  消息流水号(2)  消息包封装项(4)
		//消息体(n) 校验和(2) 结束标志位(1)
		//其中消息体属性中二进制表示（0到15）的第0到第10个表示长度，第11到15用作其它
		int high = (buffer[offset + 3] & 0x07 & 0xFF) * 256;
		int low = buffer[offset + 4] & 0xFF;
		int contentLength = high + low;
		JCLog.d(TAG, "Parsed length: " + String.valueOf(contentLength));
		return contentLength;
	}

}
