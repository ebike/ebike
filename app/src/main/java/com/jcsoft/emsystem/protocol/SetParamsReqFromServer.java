package com.jcsoft.emsystem.protocol;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class SetParamsReqFromServer extends JCProtocol
{
	private HashMap<String, String> _params = new HashMap<String, String>();
	
	public SetParamsReqFromServer()
	{
		super(EnumDataType.DATA_SetParamsReqFromServer);
	}

	@Override
	public void decodeContent()
	{
		try
		{
			String str = new String(_content, "GBK");
			String[] kvStrs = str.split(";");
			for(String kvStr : kvStrs)
			{
				String[] kvs = kvStr.split(":");
				getParams().put(kvs[0], kvs[1]);
			}
		} 
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getParams()
	{
		return _params;
	}

	public void setParams(HashMap<String, String> params)
	{
		_params = params;
	}

}
