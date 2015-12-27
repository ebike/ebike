package com.jcsoft.emsystem.protocol;

public class CommonResp extends JCProtocol
{
	private int _respSeq = 0;
	private int _respId = 0;
	private byte _result = 0x00;
	
	public CommonResp(int respSeq, int respId, byte result)
	{
		super(EnumDataType.DATA_CommonResp);
		_respSeq = respSeq;
		_respId = respId;
		_result = result;
	}

	@Override
	public void encodeContent()
	{
		byte[] content = new byte[5];
		System.arraycopy(intTo2Bytes(_respSeq), 0, content, 0, 2);
		System.arraycopy(intTo2Bytes(_respId), 0, content, 2, 2);
		content[4] = _result;
		setContent(content);
	}

}
