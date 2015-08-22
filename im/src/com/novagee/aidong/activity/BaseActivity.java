package com.novagee.aidong.activity;

import com.novagee.aidong.IMppApp;

import android.support.v7.app.ActionBarActivity;

public class BaseActivity extends ActionBarActivity {
	protected boolean isActive = false;
	
	@Override
	protected void onResume() {
		super.onResume();
		IMppApp mApp = (IMppApp) getApplicationContext();
		mApp.activityToForeground(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		IMppApp mApp = (IMppApp) getApplicationContext();
		mApp.activityToBackground(this);
	}
}
