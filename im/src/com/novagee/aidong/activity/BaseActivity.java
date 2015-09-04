package com.novagee.aidong.activity;

import com.novagee.aidong.IMppApp;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;

public class BaseActivity extends ActionBarActivity {
	protected boolean isActive = false;
	private ProgressDialog mLoadingDialog;
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
	
	public void showLoading(String message) {
        if (mLoadingDialog == null) {
        	mLoadingDialog = new ProgressDialog(this);
        	mLoadingDialog.setCancelable(false);
        	mLoadingDialog.setCanceledOnTouchOutside(false);
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.show();
    }
	
	public void dismissLoading() {
        if (mLoadingDialog != null) {
        	mLoadingDialog.dismiss();
        }
    }
}
