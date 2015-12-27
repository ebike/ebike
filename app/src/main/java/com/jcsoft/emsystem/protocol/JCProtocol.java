package com.jcsoft.emsystem.protocol;


import com.jcsoft.emsystem.client.JCLog;
import com.jcsoft.emsystem.tcp.IJCProtocol;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

public class JCProtocol implements IJCProtocol
{
	public enum EnumEncryptType
	{
		ENCRYPT_NONE,
		ENCRYPT_SHA
	}

	private static final String TAG = "JCSoft";
	
	private EnumDataType _dataType = EnumDataType.DATA_UNKNOWN;
	private EnumSubDataType _subDataType = EnumSubDataType.SUBDATA_UNKNOWN;
	private final byte PROTOCOL_MARK = 0x7E;
	private final int PROTOCOL_HEADER_FOOTER_COUNT = 13;
	private int _protocolId = 0;                    //消息ID
	private boolean _isSubcontracted = false;       //是否分包
	private EnumEncryptType _encryptType = EnumEncryptType.ENCRYPT_NONE;   //加密方式，默认为不加密
	private int _contentLength = 0;                 //消息包长度
	private int _protocolSeq = 0;                   //消息序号
	private int _protocolTotalCount = 0;            //消息包总个数
	private int _protocolCurrentNum = 0;            //消息包包序号
	protected byte[] _content = null;                 //消息体
	private byte _checkSum = 0;                     //校验和
	
	private static final HashMap<EnumDataType, Integer> DATA_TYPE_MAP = new HashMap<EnumDataType, Integer>();
	
	public JCProtocol(EnumDataType dataType)
	{
		_dataType = dataType;
		setProtocolId();
	}
	
	public JCProtocol()
	{
		_dataType = EnumDataType.DATA_UNKNOWN;
	}

	public EnumDataType getDataType() 
	{
		return _dataType;
	}

	public void setDataType(EnumDataType dataType) 
	{
		_dataType = dataType;
	}
	
	//如果解析编码失败，返回Null
	@Override
	public byte[] encode()
	{
		try
		{
			encodeContent();
		}
		catch(Exception ex)
		{
			JCLog.e(TAG, ex.getMessage());
		}
		
		_contentLength = 0;
		if (_content != null)
		{
			_contentLength = _content.length;
		}
		
		//标志位 消息头 消息体 校验和 消息头
		byte[] content = new byte[PROTOCOL_HEADER_FOOTER_COUNT + _contentLength];
		try
		{
			content[0] = PROTOCOL_MARK;
			System.arraycopy(intTo2Bytes(_protocolId), 0, content, 1, 2);
			System.arraycopy(getAttrBytes(), 0, content, 3, 2);
			System.arraycopy(intTo2Bytes(_protocolSeq), 0, content, 5, 2);
			System.arraycopy(intTo2Bytes(_protocolTotalCount), 0, content, 7, 2);
			System.arraycopy(intTo2Bytes(_protocolCurrentNum), 0, content, 9, 2);
			
			if (_content != null)
			{
				System.arraycopy(_content, 0, content, 11, _content.length);
			}
			content[content.length - 2] = calculateCheckSum(content, 1, content.length - 3);
			content[content.length - 1] = PROTOCOL_MARK;
		}
		catch(Exception e)
		{
			JCLog.e(TAG, e.getMessage());
		}
		
		return content;
	}
	
	//如果解析成功，返回0，否则返回其他值
	@Override
	public int decode(byte[] data, int length)
	{
		try
		{
			if (length < PROTOCOL_HEADER_FOOTER_COUNT)
			{
				return -1;
			}
			
			//解析校验和
			byte checkSum = data[length - 2];
			byte rightCheckSum = calculateCheckSum(data, 1, length - 3);
			if (checkSum != rightCheckSum)
			{
				//校验和不对，认为是错的消息
				return -2;
			}
			
			//解析消息头
			//解析消息类型
			_protocolId = byteArrayToInt(data, 1, 2);
			_dataType = getDataTypeById(_protocolId);
			
			//解析消息属性
			if ((data[3] & 0x20) == 0x20)
			{
				_isSubcontracted = true; //这是分包
			}
			
			_encryptType = EnumEncryptType.ENCRYPT_NONE;
			if ((data[3] & 0x08) == 0x08)
			{
				_encryptType = EnumEncryptType.ENCRYPT_SHA;
			}		
			_contentLength = ((data[3] & 0x07) & 0xff) * 256 + (data[4] & 0xff);		
			
			//解析消息流水号
			_protocolSeq = byteArrayToInt(data, 5, 2);
			
			//解析消息总包数
			_protocolTotalCount = byteArrayToInt(data, 7, 2);
			
			//解析消息当前编号
			_protocolCurrentNum = byteArrayToInt(data, 9, 2);
			
			//解析内容
			if (_contentLength > 0)
			{
				_content = new byte[_contentLength];
				System.arraycopy(data, 11, _content, 0, _contentLength);
			}
			
			if (_content != null)
			{
				decodeContent();
			}		
			return 0;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return -1;
	}
	
	public static int byteArrayToInt(byte[] b, int offset, int length)
	{
		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = offset; i < offset+length; i++)
		{
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}
	
	public static byte calculateCheckSum(byte content[], int offset, int length)
	{
		byte checkSum = 0;
		for(int i=offset; i<offset+length; i++)
		{
			checkSum ^= content[i];
		}
		return checkSum;
	}
	
	private byte[] getAttrBytes()
	{
		byte[] b = intTo2Bytes(_contentLength);
		if (_isSubcontracted)   //如果分包，需要设置第13位为1
		{
			b[0] |= 0x20;
		}
		
		switch (_encryptType)
		{
		case ENCRYPT_NONE:
			break;
		case ENCRYPT_SHA:
			b[0] |= 0x08;   //当第11位为1，表示消息体经过RSA算法加密
			break;
			default:
				break;
		}
		
		return b;
	}
	
	//将一个int值转换为两个字节，并保存在数组中
	public static byte[] intTo2Bytes(int val)
	{
		byte[] b = new byte[2];
		for (int i=b.length-1; i>-1; i--)
		{		
			b[i] = Integer.valueOf(val & 0xff).byteValue();
			//将最高位保存在最低位		
			val = val >> 8;       
			//向右移8位	
		}
		return b;
	}
	
	public static byte[] intTo4Bytes(int val)
	{
		byte[] b = new byte[4];
		for (int i=b.length-1; i>-1; i--)
		{		
			b[i] = Integer.valueOf(val & 0xff).byteValue();
			//将最高位保存在最低位		
			val = val >> 8;       
			//向右移8位	
		}
		return b;
	}
	
	//将格式为"2014-02-05 15:44"的时间转换为0x14 0x02 0x05 0x15 0x44 0x00 存于byte数组中
	//在本程序中不检查时间格式是否正确
	protected byte[] timeToBCD(String time)
	{
		if (time == null || time.length() == 0)
		{
			//如果设置的值不对，返回null
			return null;
		}

		String t = time.substring(2).replace("-", "").replace(" ", "").replace(":", "");
		byte[] b = new byte[6];
		byte[] p = strToBcd(t);
		System.arraycopy(p, 0, b, 0, p.length);
		return b;
	}

	public int getProtocolId()
	{
		return _protocolId;
	}

	public void setProtocolId(int protocolId)
	{
		_protocolId = protocolId;
	}

	public boolean isSubcontracted()
	{
		return _isSubcontracted;
	}

	public void setSubcontracted(boolean isSubcontracted)
	{
		_isSubcontracted = isSubcontracted;
	}

	public EnumEncryptType getEncryptType()
	{
		return _encryptType;
	}

	public void setEncryptType(EnumEncryptType encryptType)
	{
		_encryptType = encryptType;
	}

	public int getContentLength()
	{
		return _contentLength;
	}

	public void setContentLength(int contentLength)
	{
		_contentLength = contentLength;
	}

	public int getProtocolSeq()
	{
		return _protocolSeq;
	}

	public void setProtocolSeq(int protocolSeq)
	{
		_protocolSeq = protocolSeq;
	}

	public int getProtocolTotalCount()
	{
		return _protocolTotalCount;
	}

	public void setProtocolTotalCount(int protocolTotalCount)
	{
		_protocolTotalCount = protocolTotalCount;
	}

	public int getProtocolCurrentNum()
	{
		return _protocolCurrentNum;
	}

	public void setProtocolCurrentNum(int protocolCurrentNum)
	{
		_protocolCurrentNum = protocolCurrentNum;
	}

	public byte[] getContent()
	{
		return _content;
	}

	public void setContent(byte[] content)
	{
		_content = content;
		printByteArray(this.getDataType().toString(), _content);
	}

	public byte getCheckSum()
	{
		return _checkSum;
	}
	
	private void setProtocolId()
	{
		_protocolId = getIdByDataType(_dataType);
	}
	
	//对内容进行编码
	public void encodeContent()
	{
	}
	
	//各个子类对内容进行解码
	public void decodeContent()
	{		
	}

	public EnumSubDataType getSubDataType()
	{
		return _subDataType;
	}

	public void setSubDataType(EnumSubDataType subDataType)
	{
		_subDataType = subDataType;
	}
	
	public static int bcdToInt(byte[] bytes, int offset, int length)
	{
		try
		{
			String str = bcdToStr(bytes, offset, length);
			return Integer.valueOf(str);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return -1;
	}

	/**
	 * @功能: BCD码转为10进制串(阿拉伯数据)
	 * @参数: BCD码
	 * @结果: 10进制串
	 */
	public static String bcdToStr(byte[] bytes, int offset, int length)
	{
		StringBuffer temp = new StringBuffer(offset * 2);
		for (int i = offset; i < offset + length; i++)
		{
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString();
	}

	/**
	 * @功能: 10进制串转为BCD码
	 * @参数: 10进制串
	 * @结果: BCD码
	 */
	public static byte[] strToBcd(String asc)
	{
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0)
		{
			asc = "0" + asc;
			len = asc.length();
		}
		
		byte abt[] = new byte[len];
		if (len >= 2)
		{
			len = len / 2;
		}
		
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++)
		{
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9'))
			{
				j = abt[2 * p] - '0';
			} 
			else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z'))
			{
				j = abt[2 * p] - 'a' + 0x0a;
			} 
			else
			{
				j = abt[2 * p] - 'A' + 0x0a;
			}
			
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9'))
			{
				k = abt[2 * p + 1] - '0';
			} 
			else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z'))
			{
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} 
			else
			{
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	//将整数值转换为长度为Length的bcd
	public static byte[] changeIntToBcd(int val, int length)
	{
		String sVal = String.format(Locale.getDefault(), "%1$0" + String.valueOf(length * 2) + "d", val);
		return strToBcd(sVal);		
	}
	
	public static void printByteArray(String prefix, byte[] data)
	{
		if (data == null)
		{
			return;
		}
		
		String str = prefix + ":";
		for (int i = 0; i < data.length; i++)
		{
			String hex = String.format("0x%02X, ", data[i] & 0xFF);
			str += hex;
		}
		
		if (str.lastIndexOf(", ") != -1)
		{
			str = str.substring(0, str.length() - 2);
		}
		JCLog.d(TAG, str);
	}
	
	public static JCProtocol makeNewJCProtocol(byte[] data, int length)
	{
		EnumDataType dataType = getDataTypeById(byteArrayToInt(data, 1, 2));
		JCProtocol protocol = null;
		switch(dataType)
		{
		case DATA_UNKNOWN:
			break;
		case DATA_LoginResp:
			protocol = new LoginResp();
			break;
		case DATA_GetAllDevInfosResp:
			protocol = new GetAllDevInfosResp();
			break;
		case DATA_GetPositionResp:
			protocol = new GetPositionResp();
			break;
		case DATA_GetTrackResp:
			protocol = new GetTrackResp();
			break;
		case DATA_RemoteCtrlResp:
			protocol = new RemoteCtrlResp();
			break;
		case DATA_NotifyReqFromServer:
			protocol = new NotifyReqFromServer();
			break;
		case DATA_QueryLocationReqFromServer:
			protocol = new QueryLocationReqFromServer();
			break;
		case DATA_QueryParamsReqFromServer:
			protocol = new QueryParamsReqFromServer();
			break;
		case DATA_SetParamsReqFromServer:
			protocol = new SetParamsReqFromServer();
			break;
		case DATA_TextMessageReqFromServer:
			protocol = new TextMessageReqFromServer();
			break;
		case DATA_KickUserReqFromServer:
			protocol = new KickUserReqFromServer();
			break;
		case DATA_CommonRespFromServer:
			protocol = new CommonRespFromServer();
			break;
			default:
				break;
		}
		
		if (protocol == null)
		{
			return null;
		}
		int ret = protocol.decode(data, length);
		if (ret != 0)
		{
			//收到的数据有错
			JCLog.e(TAG, "Recv Wrong Data!");
			return null;
		}
		return protocol;
	}


	public static String bcdTimeToTimeStr(String timeStr)
	{
		try
		{
			Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);
			
			String yearStr = String.valueOf(currentYear).substring(0, 2) +  timeStr.substring(0, 2);
			String monthStr = timeStr.substring(2, 4);
			String dayStr = timeStr.substring(4, 6);
			String hourStr = timeStr.substring(6, 8);
			String minuteStr = timeStr.substring(8, 10);
			String secondStr = timeStr.substring(10, 12);
			
			timeStr = String.format("%s-%s-%s %s:%s:%s", yearStr, monthStr, dayStr, hourStr, minuteStr, secondStr);
			return timeStr;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return "";
		}
	}
	
	public static EnumDataType getDataTypeById(int id)
	{
		initDataTypeMap();
		if (DATA_TYPE_MAP.containsValue(id))
		{
			Iterator<Entry<EnumDataType, Integer>> iter = DATA_TYPE_MAP.entrySet().iterator();
			while (iter.hasNext()) 
			{
				Entry<EnumDataType, Integer> entry = iter.next();
				EnumDataType key = entry.getKey();
				int val = entry.getValue();
				if (id == val)
				{
					return key;
				}
		    }
		}
		return EnumDataType.DATA_UNKNOWN;
	}
	
	public static int getIdByDataType(EnumDataType type)
	{
		initDataTypeMap();
		return DATA_TYPE_MAP.get(type);
	}
	
	private static void initDataTypeMap()
	{
		if (DATA_TYPE_MAP.isEmpty())
		{
			DATA_TYPE_MAP.put(EnumDataType.DATA_UNKNOWN, 0xFFFF);  
			DATA_TYPE_MAP.put(EnumDataType.DATA_LoginReq, 0x1100);
			DATA_TYPE_MAP.put(EnumDataType.DATA_LogoutReq, 0x1101);
			DATA_TYPE_MAP.put(EnumDataType.DATA_GetAllDevInfosReq, 0x1103);
			DATA_TYPE_MAP.put(EnumDataType.DATA_GetPositionReq, 0x1102);
			DATA_TYPE_MAP.put(EnumDataType.DATA_GetTrackReq, 0x1104);
			DATA_TYPE_MAP.put(EnumDataType.DATA_RemoteCtrlReq, 0x1105);
			DATA_TYPE_MAP.put(EnumDataType.DATA_RealLocateReq, 0x1106);
			DATA_TYPE_MAP.put(EnumDataType.DATA_UploadLocationReq, 0x1107);
			DATA_TYPE_MAP.put(EnumDataType.DATA_HeartBeatReq, 0x1002);
			DATA_TYPE_MAP.put(EnumDataType.DATA_CommonResp, 0x1001);
			DATA_TYPE_MAP.put(EnumDataType.DATA_GetAllDevInfosResp, 0x9103);
			DATA_TYPE_MAP.put(EnumDataType.DATA_GetPositionResp, 0x9102);
			DATA_TYPE_MAP.put(EnumDataType.DATA_GetTrackResp, 0x9104);
			DATA_TYPE_MAP.put(EnumDataType.DATA_LoginResp, 0x9100);
			DATA_TYPE_MAP.put(EnumDataType.DATA_RemoteCtrlResp, 0x9105);
			DATA_TYPE_MAP.put(EnumDataType.DATA_NotifyReqFromServer, 0x9904);
			DATA_TYPE_MAP.put(EnumDataType.DATA_QueryLocationReqFromServer, 0x9903);
			DATA_TYPE_MAP.put(EnumDataType.DATA_QueryParamsReqFromServer, 0x9901);
			DATA_TYPE_MAP.put(EnumDataType.DATA_SetParamsReqFromServer, 0x9900);
			DATA_TYPE_MAP.put(EnumDataType.DATA_TextMessageReqFromServer, 0x9902);
			DATA_TYPE_MAP.put(EnumDataType.DATA_QueryLocationReqFromServerResp, 0x1903);
			DATA_TYPE_MAP.put(EnumDataType.DATA_QueryParamsReqFromServerResp, 0x1901);
			DATA_TYPE_MAP.put(EnumDataType.DATA_CommonRespFromServer, 0x9001);
			DATA_TYPE_MAP.put(EnumDataType.DATA_ModifyPasswordReq, 0x1109);
			DATA_TYPE_MAP.put(EnumDataType.DATA_KickUserReqFromServer, 0x9905);
		}	
	}	
}
