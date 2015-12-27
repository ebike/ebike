package com.jcsoft.emsystem.protocol;

public enum EnumDataType 
{
	DATA_UNKNOWN,                            //未定义
	DATA_LoginReq,
	DATA_LogoutReq,
	DATA_GetAllDevInfosReq,
	DATA_GetPositionReq,
	DATA_GetTrackReq,
	DATA_RemoteCtrlReq,
	DATA_RealLocateReq,
	DATA_UploadLocationReq,
	DATA_HeartBeatReq,
	DATA_CommonResp,
	DATA_GetAllDevInfosResp,
	DATA_GetPositionResp,
	DATA_GetTrackResp,
	DATA_LoginResp,
	DATA_RemoteCtrlResp,
	DATA_NotifyReqFromServer,
	DATA_QueryLocationReqFromServer,
	DATA_QueryParamsReqFromServer,
	DATA_SetParamsReqFromServer,
	DATA_TextMessageReqFromServer,
	DATA_QueryLocationReqFromServerResp,
	DATA_QueryParamsReqFromServerResp,
	DATA_CommonRespFromServer,
	DATA_ModifyPasswordReq,
	DATA_KickUserReqFromServer
}