package com.example.meansuresteps.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.meansuresteps.helpers.RecordGPSPositions;
import com.example.meansuresteps.helpers.onLocationChangedInterface;

public class LocationUpdateService extends Service implements LocationListener {

	private final String TAG = "LocationUpdateService";

	private Context mAppContext;
	private LocationManager mLocationManager;
	private onLocationChangedInterface mActivity;
	private RecordGPSPositions recordGPS;

	/**
	 * This constructor intiate following variables: mActivity = Instance of
	 * MeasureStepsActivity with onLocationChangedInterface interface
	 * mAppContext = Application context mLocationManager = Android'd GPS
	 * location service manager recordGPS = Instance of RecordGPSPositions which
	 * calculates distance between 2 point using latitude and longitude
	 */
	public LocationUpdateService(Activity activity) {
		if (activity instanceof onLocationChangedInterface)
			mActivity = (onLocationChangedInterface) activity;

		mAppContext = activity.getApplicationContext();
		mLocationManager = (LocationManager) mAppContext
				.getSystemService(Context.LOCATION_SERVICE);
		recordGPS = new RecordGPSPositions();
	}

	/**
	 * This function starts GPS Location updates request
	 */
	public void startLocationUpdates() {
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 0, this);
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000, 0, this);
	}

	/**
	 * This function stops GPS Location updates request
	 */
	public void stopLocationUpdates() {
		mLocationManager.removeUpdates(this);
	}

	/**
	 * This function gets called when GPS location is changed It checks
	 * accuracy(30 meters) of the location and call calculateDistanceDiff to
	 * calculate distance between previous and this point. If distance is
	 * greater than 0 then it updates MeasureStepsActivity labels for distance
	 * and steps
	 */
	@Override
	public void onLocationChanged(Location location) {
		Log.i(TAG,
				"GPS: " + location.getLatitude() + " "
						+ location.getLongitude() + " "
						+ location.getAccuracy());
		if (location != null && location.getAccuracy() < 30) {
			recordGPS.updateCurrentGPSLocation(location.getLatitude(),
					location.getLongitude());
			double miles = recordGPS.calculateDistanceDiff();
			if (miles > 0) {
				Log.i(TAG, "Distance in Miles: " + miles);
				int meters = (int) (miles * 1609.34); // Convert miles to meters
				mActivity.updateDistance(miles);
				// convert meters to no of steps (1 meter = 2 steps)
				mActivity.updateNoOfSteps(meters * 2);
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		stopLocationUpdates();
	}

	@Override
	public void onProviderEnabled(String provider) {
		startLocationUpdates();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}