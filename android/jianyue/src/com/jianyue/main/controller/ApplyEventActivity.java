package com.jianyue.main.controller;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ApplyEventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.apply_event, menu);
		return true;
	}

}
