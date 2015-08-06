package com.jianyue.utils;

import com.jianyue.main.controller.CreateUserNameActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class LocationHandler {

	public static String ERROR_PROVIDER_NOT_FOUND = "erro_provider_not_found";

	private LocationManager mLocManager;
	private Context mContext;

	private String provider = LocationManager.NETWORK_PROVIDER;

	private LocationUpdate mLocationUpdate;

	public interface LocationUpdate
	{
		public void onLocationFindSucess(Location location);

		public void onLocationFindError(String msg);
	}

	public LocationHandler(Context context) {
		mContext = context;
	}

	public LocationHandler(Context context, LocationUpdate locationUpdate) {
		mContext = context;
		mLocationUpdate = locationUpdate;
	}

	public void setLocationUpdateListerner(LocationUpdate locationUpdate) {
		mLocationUpdate = locationUpdate;
	}

	public void requestForLocation() 
	{
		mLocManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		setAutoError();

		if (!mLocManager.isProviderEnabled(provider)) 
		{
			removeAutoError();
			if(!mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				CreateUserNameActivity.redirect_flag = true;
				mContext.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			}
			else
			{
				mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, mLocListener);
			}
		}
		else 
		{
			mLocManager.requestLocationUpdates(provider, 0, 100, mLocListener);
		}

	}

	/*public void removeUpdates() {
		mLocManager.removeUpdates(mLocListener);
	}*/

	private LocationListener mLocListener = new LocationListener() {

		// Required method since class implements LocationListener interface
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d("log_tag", "onStatusChanged");
			// getLocationInfo();
			// mLocationUpdate.onLocationFindError("");
		}

		// Required method since class implements LocationListener interface
		public void onProviderEnabled(String provider) {
			// Called when the user enables the location provider
			Log.d("log_tag", "onProviderEnabled");
			// mLocManager.requestLocationUpdates(provider, GPSupdateInterval,
			// GPSmoveInterval, this);

		}

		// Required method since class implements LocationListener interface
		public void onProviderDisabled(String provider) {
			Log.d("log_tag", "onProviderDisabled");

		}

		public void onLocationChanged(Location location) {
			// Called when location has changed
			Log.d("log_tag", "onLocationChanged");
			//removeUpdates();
			// getLocationInfo();
			removeAutoError();
			if (location == null) {
				mLocationUpdate.onLocationFindError("");
			} else {
				mLocationUpdate.onLocationFindSucess(location);
			}

		}
	};

	private Handler handlerTimer = new Handler();

	private Runnable runTimer = new Runnable() {

		@Override
		public void run()
		{
			//removeUpdates();
			mLocationUpdate.onLocationFindError("take_so_much_time");
		}
	};

	public void setAutoError() {
		handlerTimer.postDelayed(runTimer, 30000);
	}

	private void removeAutoError() {
		handlerTimer.removeCallbacks(runTimer);
	}

	public Location requestLastSavedLocation() {
		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		Location lastKnownLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (lastKnownLocation == null) {
			lastKnownLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		if (lastKnownLocation == null) {
			lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);
			lastKnownLocation.setLatitude(0);
			lastKnownLocation.setLongitude(0);
		}
		return lastKnownLocation;
	}
}
