package com.novagee.aidong.activity;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arrownock.social.IAnSocialCallback;
import com.novagee.aidong.R;
import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.SpfHelper;
import com.novagee.aidong.utils.Utils;
import com.novagee.aidong.view.AppBar;

public class CreateUserActivity extends BaseActivity {

	private AppBar appbar;
	
	private EditText etUsername; 
	
	private EditText etPassword; 
	
	private String payload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkBundle();
		initView();
	}
	
	 private void checkBundle() {
	        if (getIntent().hasExtra(Constant.INTENT_EXTRA_KEY_PAYLOAD)) {
	            payload = getIntent().getStringExtra(Constant.INTENT_EXTRA_KEY_PAYLOAD);
	        }
	    }
	
	private void initView(){
		setContentView(R.layout.activity_create_user);
		
		etUsername = (EditText) findViewById(R.id.create_username);
		etPassword = (EditText) findViewById(R.id.create_password);
		etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		
		appbar = (AppBar) findViewById(R.id.create_user_app_bar);
		appbar.getLogoView().setImageResource(R.drawable.menu_back);
		appbar.getLogoView().setLayoutParams(new RelativeLayout.LayoutParams(Utils.px2Dp(this, 56),Utils.px2Dp(this, 56)));
		appbar.getLogoView().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED);
				onBackPressed();
			}
		});
		appbar.getTextView().setVisibility(View.VISIBLE);
		appbar.getTextView().setText(R.string.create_new_user_title);

		appbar.getMenuItemView().setVisibility(View.VISIBLE);
		appbar.getMenuItemView().setImageResource(R.drawable.menu_done);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(Utils.px2Dp(this, 56),Utils.px2Dp(this, 56));
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		appbar.getMenuItemView().setLayoutParams(rlp);
		appbar.getMenuItemView().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				createNewUser();
			}
		});
	}
	
	private void createNewUser(){
		 showLoading(this.getString(R.string.login_sign_up_message));
         UserManager.getInstance(CreateUserActivity.this).signUp(etUsername.getText().toString(),
        		 etPassword.getText().toString(), new IAnSocialCallback() {
                     @Override
                     public void onFailure(JSONObject arg0) {
                         try {
                             dismissLoading();
                             String errorMsg = arg0.getJSONObject("meta").getString("message");
                             Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
                         } catch (JSONException e) {
                             e.printStackTrace();
                             dismissLoading();
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
                             dismissLoading();
                         }
                     }
                 });
	}
	
	private void afterLogin(User user){
		if(!SpfHelper.getInstance(this).hasSignIn()){
	    	SpfHelper.getInstance(this).saveUserInfo(etUsername.getText().toString(), etPassword.getText().toString());
		}
    	IMManager.getInstance(this).connect(user.clientId);
    	UserManager.getInstance(this).setCurrentUser(user);
    	
		IMManager.getInstance(this).fetchAllRemoteTopic();
    	UserManager.getInstance(this).fetchMyRemoteFriend(null);
    	
    	IMManager.getInstance(this).bindAnPush();
    	
    	dismissLoading();//add by seeyet,2015/08/23,close the loader
    	
    	Intent i = new Intent(this,MainActivity.class);
    	if(payload!=null){
        	i.putExtra(Constant.INTENT_EXTRA_KEY_PAYLOAD, payload);
    	}
    	startActivity(i);
    	finish();
	}
	
	private void goToMainActivity() {
        Intent i = new Intent(CreateUserActivity.this, MainActivity.class);
        if (payload != null) {
            i.putExtra(Constant.INTENT_EXTRA_KEY_PAYLOAD, payload);
        }
        startActivity(i);
        dismissLoading();
        finish();
    }
}
