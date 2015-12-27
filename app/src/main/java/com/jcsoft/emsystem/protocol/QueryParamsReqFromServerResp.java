package com.jcsoft.emsystem.protocol;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class QueryParamsReqFromServerResp extends JCProtocol
{
	private int _seqNo = 0;  //应答流水号
	private HashMap<String, String> _params = new HashMap<String, String>();
	
	public QueryParamsReqFromServerResp(int seqNo, HashMap<String, String> params)
	{
		super(EnumDataType.DATA_QueryParamsReqFromServerResp);
		_seqNo = seqNo;
		_params = params;
	}

	@Override
	public void encodeContent()
	{		
		String kv = "";
		Iterator<Entry<String, String>> it = _params.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<String, String> entry = it.next();
			kv += String.format("%s:%s;", entry.getKey(), entry.getValue());
		}
		
		try
		{
			byte[] paramsArray = kv.getBytes("GBK");
			byte[] content = new byte[2 + paramsArray.length];
			System.arraycopy(intTo2Bytes(_seqNo), 0, content, 0, 2);
			System.arraycopy(paramsArray, 0, content, 2, paramsArray.length);
			setContent(content);
		} 
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

}
