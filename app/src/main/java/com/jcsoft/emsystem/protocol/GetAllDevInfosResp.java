package com.jcsoft.emsystem.protocol;

import com.jcsoft.emsystem.client.DevInfo;
import com.jcsoft.emsystem.client.JCLocation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GetAllDevInfosResp extends JCProtocol
{
	private List<DevInfo> _devInfos = new ArrayList<DevInfo>();
	private static final String TAG_ID = "?ID$:";
	private static final String TAG_TEL = "?TEL$:";
	private static final String TAG_TYPE = "?TYPE$:";
	private static final String TAG_STATUS = "?STATUS$:";
	private static final String TAG_LON = "?LON$:";
	private static final String TAG_LAT = "?LAT$:";
	private static final String TAG_SPEED = "?SPEED$:";
	private static final String TAG_TIME = "?TIME$:";
	private static final String TAG_NAME = "?NAME$:";
	private static final int INVALID_INT = -999;
	
	public GetAllDevInfosResp()
	{
		super(EnumDataType.DATA_GetAllDevInfosResp);
	}

	@Override
	public void decodeContent()
	{
		if (_content == null)
		{
			return;
		}
		
		try
		{
			//格式为：<ID:id1;TEL:tel1;TYPE:v1;STATUS:0;LON:lon1;LAT:lat1;
			//TIME:time1;><ID:id2;TEL:tel2;TYPE:v2;STATUS:1;LON:lon2;LAT:lat2;TIME:time2;>
			String str = new String(_content, "GBK");
			String[] devInfos = str.split("><");
			
			for(String devInfo : devInfos)
			{
				devInfo = devInfo.replace("<", "").replace(">", "");
				String[] items = devInfo.split(";");
				DevInfo d = new DevInfo();
				JCLocation ds = new JCLocation();
				
				for(String item : items)
				{
					String s = getStrByTag(item, TAG_ID);
					if (s != null)
					{
						d.setDevId(Integer.valueOf(s));
						continue;
					}
					
					s = getStrByTag(item, TAG_TEL);
					if (s != null)
					{
						d.setTel(s);
						continue;
					}					

					s = getStrByTag(item, TAG_TYPE);
					if (s != null)
					{
						d.setType(Integer.valueOf(s));
						continue;
					}
					
					s = getStrByTag(item, TAG_STATUS);
					if (s != null)
					{
						d.setStatus(s);
						continue;
					}
					
					int i = getIntByTag(item, TAG_LON);
					if (i != INVALID_INT)
					{
						ds.setLon(i);
						continue;
					}
					
					i = getIntByTag(item, TAG_LAT);
					if (i != INVALID_INT)
					{
						ds.setLat(i);
						continue;
					}
					
					i = getIntByTag(item, TAG_SPEED);
					if (i != INVALID_INT)
					{
						ds.setSpeed(i);
						continue;
					}
					
					s = getStrByTag(item, TAG_TIME);
					if (s != null)
					{
						ds.setLastLocationTime(s);
						continue;
					}
					
					s = getStrByTag(item, TAG_NAME);
					if (s != null)
					{
						d.setName(s);
						continue;
					}
				}
				d.setCurrentStatus(ds);				
				_devInfos.add(d);
			}
		} 
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	//如果返回Null,表示解析失败
	private String getStrByTag(String item, String tag)
	{
		int pos = item.indexOf(tag);
		if (pos != -1)
		{
			return item.substring(tag.length());
		}		
		return null;
	}
	
	//如果返回-999表示解析失败
	private int getIntByTag(String item, String tag)
	{
		String s = getStrByTag(item, tag);
		if (s == null)
		{
			return INVALID_INT;
		}
		
		return Integer.valueOf(s);
	}

	public List<DevInfo> getDevInfos()
	{
		return _devInfos;
	}

	public void setDevInfos(List<DevInfo> devInfos)
	{
		_devInfos = devInfos;
	}

}
