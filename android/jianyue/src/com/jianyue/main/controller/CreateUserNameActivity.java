package com.jianyue.main.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.jianyue.utils.AlertDialogUtility;
import com.jianyue.utils.LocationService;
import com.jianyue.utils.SessionManager;
import com.jianyue.utils.StaticMethodsUtility;

public class CreateUserNameActivity extends Activity{

	EditText etUserName;
	ImageView ivOk;
	public static boolean redirect_flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("UUID",  " " + System.currentTimeMillis());
		if(!SessionManager.getUUID(CreateUserNameActivity.this).equalsIgnoreCase(""))
		{
			finish();
			Intent i = new Intent(CreateUserNameActivity.this , MainActivityNew.class);
			startActivity(i);
			return;
		}
		else
		{
			setContentView(R.layout.create_username);
			ShowLocationDialog();
		}
		etUserName = (EditText)findViewById(R.id.etUserName);
		ivOk = (ImageView)findViewById(R.id.ivOk);
		
		ivOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(etUserName.getText().toString().trim().length() > 0)
				{
					finish();
					Intent i = new Intent(CreateUserNameActivity.this , GenderSelectionActivity.class);
					i.putExtra("Nickname", etUserName.getText().toString().trim());
					startActivity(i);
				}
				else
				{
					StaticMethodsUtility.showNegativeToast(CreateUserNameActivity.this, "请输入您的昵称");
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (redirect_flag) {
			redirect_flag = false;
			LocationManager mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Intent i = new Intent(CreateUserNameActivity.this, LocationService.class);
			if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
					|| mLocManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				stopService(i);
				startService(i);
			} else {
				stopService(i);
			}
		}
	}
	
	public void ShowLocationDialog() {
		AlertDialogUtility.showLocationAlert(CreateUserNameActivity.this,
				getString(R.string.location_string), onOkClick,
				onDontAllowClick);
	}

	private DialogInterface.OnClickListener onOkClick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent i = new Intent(CreateUserNameActivity.this, LocationService.class);
			startService(i);
		}
	};

	private DialogInterface.OnClickListener onDontAllowClick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {

		}
	};

}
