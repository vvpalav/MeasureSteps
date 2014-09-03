package com.example.meansuresteps.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meansuresteps.R;
import com.example.meansuresteps.helpers.onLocationChangedInterface;
import com.example.meansuresteps.services.LocationUpdateService;

public class MeasureStepsActivity extends Activity implements onLocationChangedInterface {

	private int totalStepsWalked;
	private double totalDistInMiles;
	private LocationUpdateService locationUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure_steps);

		locationUpdate = new LocationUpdateService(this);
		Button stopButton = (Button) findViewById(R.id.startStop);
		final Button startButton = (Button) findViewById(R.id.start);

		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startButton.setEnabled(true);
				// Start GPS Location request service
				locationUpdate.stopLocationUpdates(); 
				Toast.makeText(MeasureStepsActivity.this,
						"GPS Location Request Stopped", Toast.LENGTH_SHORT)
						.show();
			}
		});

		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startButton.setEnabled(false);
				// Stop GPS Location request service
				locationUpdate.startLocationUpdates(); 
				Toast.makeText(MeasureStepsActivity.this,
						"GPS Location Request Started", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/**
	 * This function allow other classes to access MeasureStepsActivity via
	 * interface to update distance on the screen
	 */
	@Override
	public void updateDistance(double miles) {
		this.totalDistInMiles += miles;
		TextView distanceInMiles = (TextView) findViewById(R.id.distanceInMiles);
		distanceInMiles.setText(String.valueOf(this.totalDistInMiles));
	}

	/**
	 * This function allow other classes to access MeasureStepsActivity via
	 * interface to update no of steps walked on the screen
	 */
	@Override
	public void updateNoOfSteps(int steps) {
		this.totalStepsWalked += steps;
		TextView noOfSteps = (TextView) findViewById(R.id.noOfSteps);
		noOfSteps.setText(String.valueOf(this.totalStepsWalked));
	}

	/**
	 * Stop the GPS location request service on back pressed
	 */
	@Override
	public void onBackPressed() {
		locationUpdate.stopLocationUpdates();
		super.onBackPressed();
	}
}
