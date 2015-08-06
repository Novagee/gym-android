package com.jianyue.utils;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.jianyue.utils.LocationHandler.LocationUpdate;

public class LocationService extends Service
{
	public static final String ACTION_UPDATE = "com.custom.LocationService.UPDATE";
	

	private LocationHandler locationHandler = new LocationHandler(LocationService.this);
	String longitude = "0.0", latitude = "0.0";

	@Override
	public IBinder onBind(Intent intent)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		// For time consuming an long tasks you can launch a new thread here...
		//Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
		super.onStart(intent, startId);
		RequestLocation();
	}

	@Override
	public void onDestroy()
	{
		//Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

	private void RequestLocation()
	{
		locationHandler.requestForLocation();
		locationHandler.setLocationUpdateListerner(new LocationUpdate()
		{
			@Override
			public void onLocationFindSucess(Location location)
			{
				longitude = String.valueOf(location.getLongitude());
				latitude = String.valueOf(location.getLatitude());
				Log.d("locatoin", latitude);
				try
				{
					SessionManager.setLatLong(getApplicationContext(), latitude, longitude);
				}
				catch(Exception e)
				{
					
				}
				//RequestUpdatelocation();
			}

			@Override
			public void onLocationFindError(String msg)
			{
				Log.d("locatoin", "dsgd");
				if (msg.equals(LocationHandler.ERROR_PROVIDER_NOT_FOUND))
				{
					
				}
				return;
			}
		});
	}
}