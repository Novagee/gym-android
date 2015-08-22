package com.novagee.aidong.activity;

import java.util.Observable;
import java.util.Observer;

import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.im.model.Chat;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.DBug;
import com.novagee.aidong.utils.Utils;
import com.novagee.aidong.view.AppBar;
import com.novagee.aidong.view.UserDetailView;

import com.novagee.aidong.R;
import com.novagee.aidong.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class UserDetailActivity extends BaseActivity implements Observer{
	private UserDetailView mUserDetailView;
	private AppBar mAppBar;
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkBundle();
		initView();
		
		UserManager.getInstance(this).addObserver(this);
	}
	
	private void checkBundle(){
		if(getIntent().hasExtra(Constant.INTENT_EXTRA_KEY_CLIENT)){
			String clientId = getIntent().getStringExtra(Constant.INTENT_EXTRA_KEY_CLIENT);
			UserManager.getInstance(this).fetchUserDataByClientId(clientId);
			user = UserManager.getInstance(this).getUserByClientId(clientId);
		}
	}
	
	private void initView(){
		setContentView(R.layout.activity_user_detail);
		
		mAppBar = (AppBar)findViewById(R.id.user_detail_app_bar);
		mAppBar.getLogoView().setImageResource(R.drawable.menu_back);
		mAppBar.getLogoView().setLayoutParams(new RelativeLayout.LayoutParams(Utils.px2Dp(this, 56),Utils.px2Dp(this, 56)));
		mAppBar.getLogoView().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		mUserDetailView = (UserDetailView) findViewById(R.id.userDetailAct_userDetailView);
		if(!user.clientId.equals(UserManager.getInstance(this).getCurrentUser().clientId)){
			mUserDetailView.getTextButton().setVisibility(View.VISIBLE);
			mUserDetailView.setButton(getString(R.string.setting_send_msg), new OnClickListener(){
				@Override
				public void onClick(View v) {
					Chat chat = IMManager.getInstance(UserDetailActivity.this).addChat(user.clientId);
					IMManager.getInstance(UserDetailActivity.this).notifyChatUpdated();
					Intent i = new Intent(UserDetailActivity.this,ChatActivity.class);
					Bundle b = new Bundle();
					b.putSerializable(Constant.INTENT_EXTRA_KEY_CHAT, chat);
					i.putExtras(b);
					startActivity(i);
					finish();
					overridePendingTransition(android.R.anim.fade_in,R.anim.push_up_out);
				}
			});
		}else{
			mUserDetailView.getTextButton().setVisibility(View.GONE);
		}
		mUserDetailView.setUserInfo(user);
	}
	
	private void refreshUser(){
		user = user.getFromTable();
		mUserDetailView.setUserInfo(user);
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,R.anim.push_up_out);
	}

	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof UserManager){
			if(data instanceof UserManager.UpdateType){
				if(((UserManager.UpdateType)data).equals(UserManager.UpdateType.User)){
					refreshUser();
				}
			}
		}
	}
}
