package com.novagee.aidong.activity;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import com.novagee.aidong.controller.ApplyEventDataTask;
import com.novagee.aidong.imageloader.ImageLoader;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.InternetChecker;
import com.novagee.aidong.utils.webservices.APIResponse;
import com.novagee.aidong.utils.webservices.WebElements;
import com.novagee.aidong.view.CustomDialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.novagee.aidong.R;

public class ApplyEventActivity extends Activity {

	private String eventId;
	private String mobile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_event);
		Intent intent = getIntent();
        eventId = intent.getStringExtra("id");
        TextView tvEventTitle = (TextView)findViewById(R.id.event_title);
		tvEventTitle.setText(intent.getStringExtra("title"));
		ImageView imageView = (ImageView) findViewById(R.id.event_pic);
		String pic = intent.getStringExtra("pic");
		if(!(pic == null || "".equals(pic))){
			System.out.println("event pic:"+pic);
			ImageLoader.getInstance(this).DisplayImage(pic, imageView, R.drawable.friend_default, false);	
		}
		TextView tvEventDescription = (TextView)findViewById(R.id.event_description);
		tvEventDescription.setText(intent.getStringExtra("description"));
		ImageView backImageView = (ImageView) findViewById(R.id.event_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		final Context context = this;
		TextView tvApplyEvent = (TextView)findViewById(R.id.apply_event_button);
		tvApplyEvent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				try{
					TelephonyManager phoneMgr=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
					mobile = phoneMgr.getLine1Number();
				}catch(Exception e){
					e.printStackTrace();
				}
				//如果无法获取手机号码，则让用户自己输入
				if(mobile == null || "".equals(mobile)){
					final EditText inputServer = new EditText(ApplyEventActivity.this);
					inputServer.setBackgroundColor(0xFFFFFFFF);
					inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
					inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)}); 
					new CustomDialog.Builder(ApplyEventActivity.this).setTitle(context.getString(R.string.event_input_mobile)).setContentView(inputServer)
						     .setPositiveButton(context.getString(R.string.dialog_ok),
						    		 new DialogInterface.OnClickListener() {
						             @Override
						             public void onClick(DialogInterface dialog, int which) {
						            	 mobile = inputServer.getText().toString();
						            	 dialog.dismiss();
						            	 if(mobile == null || "".equals(mobile)){
						            		 Toast.makeText(context, context.getString(R.string.event_mobile_cannot_null),Toast.LENGTH_LONG).show();
						            	 }else{
						            		 doApply();
						            	 }
						           }
						        })
						     .setNegativeButton(context.getString(R.string.dialog_cancel), 
						    		 new DialogInterface.OnClickListener() {
					             @Override
					             public void onClick(DialogInterface dialog, int which) {
					            	 dialog.dismiss();
					           }
					        }).create().show();
				}else{
					doApply();
				}
				
				
				
		
			}
		});
        System.out.println("eventId:"+eventId);
	}
	
	private void doApply() {
		final APIResponse apiResponse = new APIResponse();
		final Context context = this;
		String append_url = "r/event/"+eventId+"/apply";
		ApplyEventDataTask task = new ApplyEventDataTask(ApplyEventActivity.this,
				apiResponse , append_url ) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (result.equals(Constant.FAIL)) {
					if (!InternetChecker
							.checkInternetConnection(ApplyEventActivity.this)) {
						Toast.makeText(context, context.getString(R.string.event_no_internet),Toast.LENGTH_LONG).show();
					}
					return;
				}
				
				if(apiResponse.ack.equalsIgnoreCase("Success"))
				{
					Toast.makeText(context,context.getString(R.string.event_apply_successful),Toast.LENGTH_LONG).show();
					finish();
				//	overridePendingTransition(R.anim.slide_in_right,android.R.anim.fade_out);
				}
				
			}
		};
		
		if (InternetChecker.checkInternetConnection(ApplyEventActivity.this)) {
			try {

				MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					/*
					reqEntity.addPart(WebElements.FETCH_OR_APPLY_EVENT.UUID,
							new StringBody(SessionManager.getUUID(ApplyEventActivity.this)));*/
					reqEntity.addPart(WebElements.FETCH_OR_APPLY_EVENT.PHONE_NUMBER,
							new StringBody(mobile));
					reqEntity.addPart(WebElements.FETCH_OR_APPLY_EVENT.TIMESTAMP,
							new StringBody(String.valueOf(System.currentTimeMillis())));
					task.execute(reqEntity);
				} catch (Exception e) {
					e.printStackTrace();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context, context.getString(R.string.event_no_internet),Toast.LENGTH_LONG).show();
		}


	}
	

}
