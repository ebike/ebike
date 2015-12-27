package com.jcsoft.emsystem.client;

import android.util.Log;

public class JCLog
{
	public static void d(String tag, String msg)
	{
		Log.d(tag, msg);
	}
	
	public static void e(String tag, String msg)
	{
		Log.e(tag, msg);
	}
	
	public static void e(String tag, String msg, Throwable tr)
	{
		Log.e(tag, msg, tr);
	}
}
