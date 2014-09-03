package com.example.meansuresteps.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.meansuresteps.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button measureSteps = (Button) findViewById(R.id.measureSteps);
		measureSteps.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this,
						MeasureStepsActivity.class));
			}
		});

		Button getSensorsReport = (Button) findViewById(R.id.getMobileReport);
		getSensorsReport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this,
						GetSensorsReportActivity.class));
			}
		});
	}
}
