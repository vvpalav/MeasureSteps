package com.example.meansuresteps.helpers;

import java.text.DecimalFormat;

import android.util.Log;

public class RecordGPSPositions {

	private double lastGPSLat = -1;
	private double lastGPSLong = -1;
	private double currentGPSLat = -1;
	private double currentGPSLong = -1;
	private boolean flag = false;

	/**
	 * This function update last and current latitude and longitude
	 */
	public void updateCurrentGPSLocation(double lat, double lon) {
		if (!flag && this.lastGPSLat == -1 && this.lastGPSLong == -1) {
			// first time location received
			this.lastGPSLat = lat;
			this.lastGPSLong = lon;
		} else {
			// make current GPS points as last GPS points
			flag = true;
			this.lastGPSLat = this.currentGPSLat;
			this.lastGPSLong = this.currentGPSLong;
		}
		this.currentGPSLat = lat;
		this.currentGPSLong = lon;
	}

	/**
	 * This function uses haversine formule to calculate distance based on
	 * latitude and longitude http://andrew.hedges.name/experiments/haversine/
	 * 
	 * @return: distance in miles between two points
	 */
	public double calculateDistanceDiff() {
		// Return if same location or only 1 location received
		if (!flag
				|| (this.currentGPSLat == this.lastGPSLat && 
				this.currentGPSLong == this.lastGPSLong))
			return 0;

		Log.i("RecordGPSPositionsLast", this.lastGPSLat + " "
				+ this.lastGPSLong);
		Log.i("RecordGPSPositionsCurrent", this.currentGPSLat + " "
				+ this.currentGPSLong);

		int RadiusOfEarth = 6371;
		double dlon = deg2rad(this.currentGPSLong - this.lastGPSLong);
		double dlat = deg2rad(this.currentGPSLat - this.lastGPSLat);
		double a = Math.pow(Math.sin(dlat / 2), 2)
				+ Math.cos(deg2rad(this.lastGPSLat))
				* Math.cos(deg2rad(this.currentGPSLat))
				* Math.pow(Math.sin(dlon / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		// Convert KM to miles and return the value
		return Double.valueOf(twoDForm.format(RadiusOfEarth * c * 0.621371));
	}

	/**
	 * This function convert value from degree to radians
	 * 
	 * @param deg : value in degree
	 * @return value in radians
	 */
	public double deg2rad(double deg) {
		return deg * (Math.PI / 180);
	}
}
