package com.example.meansuresteps.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.meansuresteps.R;

public class GetSensorsReportActivity extends Activity implements
		SensorEventListener {

	
	private SensorManager mSensorManager;
	private AudioManager mAudioManager;
	private BroadcastReceiver receiver;
	private int steps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_sensors_report);
		receiver = getBroadcastReceiver();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
	}

	/**
	 * Broadcast receivers for Audio and Battery Sensors
	 */
	public BroadcastReceiver getBroadcastReceiver() {
		return new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// Received ringer mode changed intent
				if (intent.getAction().equals(
						AudioManager.RINGER_MODE_CHANGED_ACTION)) {
					String value = "NA";
					switch (mAudioManager.getRingerMode()) {
					case AudioManager.RINGER_MODE_SILENT:
						value = "Ringer Mode Silent";
						break;
					case AudioManager.RINGER_MODE_VIBRATE:
						value = "Ringer Mode Vibrate";
						break;
					case AudioManager.RINGER_MODE_NORMAL:
						value = "Ringer Mode Normal";
						break;
					}
					TextView modeLabel = (TextView) findViewById(R.id.modeValue);
					modeLabel.setText(value);
				} else if (intent.getAction().equals(
						Intent.ACTION_BATTERY_CHANGED)) {
					// Received battery level changed intent
					TextView batteryLabel = (TextView) findViewById(R.id.batteryValue);
					int currentLevel = intent.getIntExtra(
							BatteryManager.EXTRA_LEVEL, -1);
					int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,
							-1);
					int level = -1;
					if (currentLevel >= 0 && scale > 0) {
						level = (currentLevel * 100) / scale;
					}
					batteryLabel.setText(level + "%");
				}
			}
		};
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	/**
	 * This function gets triggered when sensor event is changed It supplied
	 * values of new state of the sensor in SensorEvent
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		TextView label = null;
		Float value = event.values[0];
		switch (sensorType) {
		case Sensor.TYPE_GRAVITY: {
			label = (TextView) findViewById(R.id.gravityValue);
			break;
		}
		case Sensor.TYPE_AMBIENT_TEMPERATURE: {
			label = (TextView) findViewById(R.id.temperatureValue);
			break;
		}
		case Sensor.TYPE_GYROSCOPE: {
			label = (TextView) findViewById(R.id.gyroscopeValue);
			break;
		}
		case Sensor.TYPE_LIGHT: {
			label = (TextView) findViewById(R.id.brightnessValue);
			break;
		}
		case Sensor.TYPE_PRESSURE: {
			label = (TextView) findViewById(R.id.pressureValue);
			break;
		}
		case Sensor.TYPE_PROXIMITY: {
			label = (TextView) findViewById(R.id.proximityValue);
			break;
		}
		case Sensor.TYPE_STEP_DETECTOR: {
			if (event.values[0] == 1) {
				label = (TextView) findViewById(R.id.stepsValue);
				++steps;
			}
			value = (float) steps;
			break;
		}
		case Sensor.TYPE_RELATIVE_HUMIDITY: {
			label = (TextView) findViewById(R.id.humidityValue);
			break;
		}
		case Sensor.TYPE_LINEAR_ACCELERATION: {
			label = (TextView) findViewById(R.id.accelnValue);
			break;
		}
		}

		if (label != null)
			label.setText(Math.round(value) + "");
	}

	/**
	 * register sensor listeners onResume
	 */
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(
				AudioManager.RINGER_MODE_CHANGED_ACTION));
		registerReceiver(receiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_ALL)) {
			switch (sensor.getType()) {
			case Sensor.TYPE_GRAVITY:
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
			case Sensor.TYPE_GYROSCOPE:
			case Sensor.TYPE_LIGHT:
			case Sensor.TYPE_PRESSURE:
			case Sensor.TYPE_PROXIMITY:
			case Sensor.TYPE_STEP_DETECTOR:
			case Sensor.TYPE_RELATIVE_HUMIDITY:
			case Sensor.TYPE_LINEAR_ACCELERATION:
				mSensorManager.registerListener(this, sensor, 1000);
				break;
			}
		}
	}

	/**
	 * Unregister listeners onPause to save battery
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		unregisterReceiver(receiver);
	}
}
