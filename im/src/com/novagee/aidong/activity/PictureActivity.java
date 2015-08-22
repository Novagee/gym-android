package com.novagee.aidong.activity;

import com.novagee.aidong.imageloader.ImageLoader;
import com.novagee.aidong.utils.Utils;
import com.novagee.aidong.view.AppBar;
import com.novagee.aidong.view.TouchImageView;

import com.novagee.aidong.R;
import com.novagee.aidong.R.anim;
import com.novagee.aidong.R.drawable;
import com.novagee.aidong.R.id;
import com.novagee.aidong.R.layout;
import com.novagee.aidong.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class PictureActivity extends BaseActivity {
	private AppBar mAppbar;
	private TouchImageView mTouchImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		mAppbar = (AppBar)findViewById(R.id.picture_app_bar);
		mAppbar.getLogoView().setImageResource(R.drawable.menu_back);
		mAppbar.getLogoView().setLayoutParams(new RelativeLayout.LayoutParams(Utils.px2Dp(this, 56),Utils.px2Dp(this, 56)));
		mAppbar.getLogoView().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mAppbar.getTextView().setVisibility(View.VISIBLE);
		mAppbar.getTextView().setText(R.string.chat_view_picture);
			
		mTouchImageView = (TouchImageView)findViewById(R.id.picture_touchImageView);
		String url = getIntent().getStringExtra("url");
		ImageLoader.getInstance(this).DisplayImage(url, mTouchImageView, null, false);
	}
	
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,R.anim.push_up_out);
	}
	
}
