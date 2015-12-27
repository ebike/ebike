package com.jcsoft.emsystem.tcp;

public interface IJCProtocolScanner
{
	public enum EnumScanResult
	{
		RESULT_End,                    //此包包含一个完整的协议包，有可能还有其它数据
		RESULT_NeedNextBuffer,         //此包还需要后续的数据才能组成一个完整的协议包
		RESULT_Error                   //这个数据包有错
	}
	
	public class ScanResult
	{
		private EnumScanResult _enumScanResult = EnumScanResult.RESULT_Error;
		private int _unprocessedLocation = 0;
		
		public ScanResult()
		{			
		}
		
		public ScanResult(EnumScanResult enumScanResult, int unprocessedLocation)
		{
			_enumScanResult = enumScanResult;
			_unprocessedLocation = unprocessedLocation;
		}

		public EnumScanResult getEnumScanResult()
		{
			return _enumScanResult;
		}

		public void setEnumScanResult(EnumScanResult enumScanResult)
		{
			_enumScanResult = enumScanResult;
		}

		public int getUnprocessedLocation()
		{
			return _unprocessedLocation;
		}

		public void setUnprocessedLocation(int unprocessedLocation)
		{
			_unprocessedLocation = unprocessedLocation;
		}		
	}
	
	//从buffer的offset处开始解析（在解析过程中，每次都是协议包的第一个字符），解析的最大长度为length
	ScanResult scanBuffer(byte[] buffer, int offset, int length);
}
