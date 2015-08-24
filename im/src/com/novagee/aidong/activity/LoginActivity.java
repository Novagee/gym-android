package com.novagee.aidong.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.novagee.aidong.R;

import com.arrownock.exception.ArrownockException;
import com.arrownock.im.AnIM;
import com.arrownock.im.callback.AnIMAddClientsCallbackData;
import com.arrownock.im.callback.AnIMBinaryCallbackData;
import com.arrownock.im.callback.AnIMCallbackAdapter;
import com.arrownock.im.callback.AnIMCreateTopicCallbackData;
import com.arrownock.im.callback.AnIMGetClientIdCallbackData;
import com.arrownock.im.callback.AnIMGetClientsStatusCallbackData;
import com.arrownock.im.callback.AnIMGetTopicListCallbackData;
import com.arrownock.im.callback.AnIMMessageCallbackData;
import com.arrownock.im.callback.AnIMStatusUpdateCallbackData;
import com.arrownock.im.callback.AnIMTopicBinaryCallbackData;
import com.arrownock.im.callback.AnIMTopicMessageCallbackData;
import com.arrownock.social.AnSocial;
import com.arrownock.social.AnSocialMethod;
import com.arrownock.social.IAnSocialCallback;
import com.novagee.aidong.IMppApp;
import com.novagee.aidong.controller.SocialManager;
import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.DBug;
import com.novagee.aidong.utils.SpfHelper;
import com.novagee.aidong.utils.Utils;
import com.novagee.aidong.view.MaterialEditText;

public class LoginActivity extends BaseActivity{
	private MaterialEditText etUsername,etPwd;
	private Button btnSignUp,btnSignIn;
	private String payload;
	private boolean doubleBackToExistPressedOnce = false;
	private ProgressDialog mDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkBundle();
		autoSignIn();
	}

	private void autoSignIn(){
		if(SpfHelper.getInstance(this).hasSignIn()){
			UserManager.getInstance(this).login(SpfHelper.getInstance(this).getMyUsername(),SpfHelper.getInstance(this).getMyPwd(),new IAnSocialCallback(){
				@Override
				public void onFailure(JSONObject arg0) {
					try {
						String errorMsg = arg0.getJSONObject("meta").getString("message");
						Toast.makeText(getBaseContext(), errorMsg,Toast.LENGTH_LONG).show();
						initView();
					} catch (JSONException e) {
						e.printStackTrace();
						initView();
					}
				}
				@Override
				public void onSuccess(final JSONObject arg0) {
					try {
						JSONObject userJson = arg0.getJSONObject("response").getJSONObject("user");
	                	User user = new User(userJson);
	                	afterLogin(user);
					} catch (JSONException e) {
						e.printStackTrace();
						initView();
					}
				}
			});
		}else{
			initView();
		}
	}
	
	private void checkBundle(){
		if(getIntent().hasExtra(Constant.INTENT_EXTRA_KEY_PAYLOAD)){
			payload = getIntent().getStringExtra(Constant.INTENT_EXTRA_KEY_PAYLOAD);
		}
	}
	
	private void initView(){
		setContentView(R.layout.activity_login);
		final Context context = this;
		mDialog = new ProgressDialog(this);//add by jiff,2015/08/23 loader
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		
		etUsername = (MaterialEditText)findViewById(R.id.et_username);
		etUsername.setLineFocusedColor(getResources().getColor(R.color.no5));
		etUsername.setLineUnFocusedColor(getResources().getColor(R.color.no5));
		etUsername.setLineFocusedHeight(4);
		etUsername.setLineUnFocusedHeight(1);
		etUsername.getEditText().setTextColor(getResources().getColor(R.color.no5));
		etUsername.getEditText().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		etUsername.getEditText().requestFocus();
		etUsername.getEditText().setHint(R.string.login_username);
		etUsername.getEditText().setHintTextColor(getResources().getColor(R.color.no7));
		etUsername.getEditText().setSingleLine();
		
		etPwd = (MaterialEditText)findViewById(R.id.et_pwd);
		etPwd.setLineFocusedColor(getResources().getColor(R.color.no5));
		etPwd.setLineUnFocusedColor(getResources().getColor(R.color.no5));
		etPwd.setLineFocusedHeight(4);
		etPwd.setLineUnFocusedHeight(1);
		etPwd.getEditText().setTextColor(getResources().getColor(R.color.no5));
		etPwd.getEditText().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		etPwd.getEditText().setHint(R.string.login_pwd);
		etPwd.getEditText().setHintTextColor(getResources().getColor(R.color.no7));
		etPwd.getEditText().setSingleLine();
		etPwd.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
		etPwd.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);

		float corner = Utils.px2Dp(this, 2);
		GradientDrawable bgBtnSignUp = new GradientDrawable();
		bgBtnSignUp.setColor(getResources().getColor(R.color.no3));
		bgBtnSignUp.setCornerRadii(new float[]{corner,corner,corner,corner,corner,corner,corner,corner});
		GradientDrawable bgBtnSignIn = new GradientDrawable();
		bgBtnSignIn.setColor(getResources().getColor(R.color.no2));
		bgBtnSignIn.setCornerRadii(new float[]{corner,corner,corner,corner,corner,corner,corner,corner});
		
		btnSignUp = (Button)findViewById(R.id.btn_sign_up);
		btnSignUp.setBackgroundDrawable(bgBtnSignUp);
		btnSignUp.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mDialog.setMessage(context.getString(R.string.login_sign_up_message));
				mDialog.show();
				UserManager.getInstance(LoginActivity.this).signUp(etUsername.getEditText().getText().toString(),etPwd.getEditText().getText().toString(),new IAnSocialCallback(){
					@Override
					public void onFailure(JSONObject arg0) {
						try {
							mDialog.dismiss();//add by seeyet,2015/08/23,close the loader
							String errorMsg = arg0.getJSONObject("meta").getString("message");
							Toast.makeText(getBaseContext(), errorMsg,Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					public void onSuccess(final JSONObject arg0) {
						try {
							JSONObject userJson = arg0.getJSONObject("response").getJSONObject("user");
		                	User user = new User(userJson);
		                	afterLogin(user);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		btnSignIn = (Button)findViewById(R.id.btn_sign_in);
		btnSignIn.setBackgroundDrawable(bgBtnSignIn);
		btnSignIn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mDialog.setMessage(context.getString(R.string.login_sign_in_message));
				mDialog.show();
				UserManager.getInstance(LoginActivity.this).login(etUsername.getEditText().getText().toString(),etPwd.getEditText().getText().toString(),new IAnSocialCallback(){
					@Override
					public void onFailure(JSONObject arg0) {
						try {
							mDialog.dismiss();//add by seeyet,2015/08/23,close the loader
							String errorMsg = arg0.getJSONObject("meta").getString("message");
							Toast.makeText(getBaseContext(), errorMsg,Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					public void onSuccess(final JSONObject arg0) {
						try {
							JSONObject userJson = arg0.getJSONObject("response").getJSONObject("user");
		                	User user = new User(userJson);
		                	afterLogin(user);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		});
		
	}
	
	
	private void afterLogin(User user){
		if(!SpfHelper.getInstance(this).hasSignIn()){
	    	SpfHelper.getInstance(this).saveUserInfo(etUsername.getEditText().getText().toString(), etPwd.getEditText().getText().toString());
		}
    	IMManager.getInstance(this).connect(user.clientId);
    	UserManager.getInstance(this).setCurrentUser(user);
    	
		IMManager.getInstance(this).fetchAllRemoteTopic();
    	UserManager.getInstance(this).fetchMyRemoteFriend(null);
    	
    	IMManager.getInstance(this).bindAnPush();
    	
    	mDialog.dismiss();//add by seeyet,2015/08/23,close the loader
    	
    	Intent i = new Intent(this,MainActivity.class);
    	if(payload!=null){
        	i.putExtra(Constant.INTENT_EXTRA_KEY_PAYLOAD, payload);
    	}
    	startActivity(i);
    	finish();
	}
	
	
	@Override
	public void onBackPressed() {
		Handler h = new Handler();
		Runnable r =new Runnable(){
			@Override
			public void run() {
				doubleBackToExistPressedOnce = false;
			}
		};
		if(!doubleBackToExistPressedOnce){
			doubleBackToExistPressedOnce = true;
			Toast.makeText(this,getString(R.string.general_press_again_to_exit),Toast.LENGTH_SHORT).show();
			h.postDelayed(r, 2000);
		}else{
			h.removeCallbacks(r);
			super.onBackPressed();
		}
	}
}