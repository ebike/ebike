package com.jcsoft.emsystem.client;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.jcsoft.emsystem.constants.JCConstValues;
import com.jcsoft.emsystem.database.ConfigService;

import java.util.Iterator;

public class JCLocationManager implements LocationListener
{
	private static final String TAG = "JCSoft";
	private static  JCLocationManager _instance = null;	
	private Context _context = null;
	private LocationManager _locationManager = null;
	private String _currentProvider = null;
	private int _currentSatelliteCount = 0;	
	
	private JCLocationManager()
	{
		
	}
	
	public static JCLocationManager instance()
	{
		if (_instance == null)
		{
			_instance = new JCLocationManager();			
		}
		return _instance;
	}
	
	public static void destroy()
	{
		_instance = null;
	}
	
	public void init(Context context)
	{
		_context = context;
	}
	
	public boolean start()
	{
		if (_context != null && _locationManager == null) //启动定位
		{
			_locationManager = (LocationManager)_context.getSystemService(Context.LOCATION_SERVICE);
			if (_locationManager == null)
			{
				return false;
			}		
			uploadLastKnownLocation();	

			int interval = getUploadInterval();	
			String provider = ConfigService.instance().getConfigValue(JCConstValues.S_LoactionProvider);
			if (provider == null || provider.length() == 0)
			{
				ConfigService.instance().insertConfigValue(JCConstValues.S_LoactionProvider, LocationManager.NETWORK_PROVIDER);
				provider = LocationManager.NETWORK_PROVIDER;
				startNetwork(interval);
			}
			else if (provider == LocationManager.NETWORK_PROVIDER)
			{
				startNetwork(interval);
			}
			else
			{
				startGps(interval);
			}
		}
		return true;
	}
	
	public void startGps(int interval)
	{	
		_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, 5, this);
		_locationManager.addGpsStatusListener(new Listener(){

			@Override
			public void onGpsStatusChanged(int event)
			{
				GpsStatus status = _locationManager.getGpsStatus(null);
				if (status != null)
				{					
					int maxCount = status.getMaxSatellites();
					Iterator<GpsSatellite> it = status.getSatellites().iterator();
					int count = 0;
					while(it.hasNext() && count < maxCount)
					{
						it.next();
						count ++;
					}
					_currentSatelliteCount = count;
				}
				
			}});
	}

	public void startNetwork(int interval)
	{
		try
		{
			_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval, 5, this);
		}
		catch(Exception ex)
		{
			JCLog.e(TAG, ex.getMessage());
		}
	}
	
	private int getUploadInterval()
	{
		int interval = 5000;  //默认五分钟查询一次
		String sInterval = ConfigService.instance().getConfigValue(JCConstValues.S_UploadLocationInterval);
		if (sInterval == null || sInterval.length() == 0)
		{
			ConfigService.instance().insertConfigValue(JCConstValues.S_UploadLocationInterval, String.valueOf(interval));
		}
		else
		{
			//interval = Integer.valueOf(sInterval);
			if (interval <= 2000)  //必须大于2秒
			{
				interval = 2000;
			}
		}
		return interval;
	}
	
	public Location getCurrentLocation()
	{
		if (_locationManager == null)
		{
			return null;
		}
		Location location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		return location;
	}
	
	public void uploadLastKnownLocation()
	{
		if (_locationManager == null)
		{
			return;
		}
		
		if(_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{		
			Location location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null)
			{
				location = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
			
			if (location != null)
			{
				//上报位置信息
				uploadLocation(location);
			}
		}
	}

	public void stop()
	{
		if (_locationManager != null)
		{
			_locationManager.removeUpdates(this);
		}
	}
	
	private void uploadLocation(Location location)
	{
		JCClient.instance().uploadLocation(location, _currentSatelliteCount);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		JCLog.d(TAG, "onLocationChanged");
		uploadLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider)
	{
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		JCLog.d(TAG, "onProviderEnabled");
		if (_currentProvider == null)
		{
			_currentProvider = provider;
			return;
		}
		
		if (_currentProvider.equalsIgnoreCase(LocationManager.GPS_PROVIDER))
		{
			return;
		}
		
		//肯定是在网络定位状态并且此时用户打开了GPS定位
		_currentProvider = provider;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{		
	}
		
}
