package com.novagee.aidong.activity;

import java.util.Set;

import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.im.model.Chat;
import com.novagee.aidong.im.model.Topic;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.Utils;
import com.novagee.aidong.view.AppBar;
import com.novagee.aidong.view.MaterialEditText;

import com.novagee.aidong.R;
import com.novagee.aidong.R.color;
import com.novagee.aidong.R.drawable;
import com.novagee.aidong.R.id;
import com.novagee.aidong.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

public class EditTopicActivity extends BaseActivity {
	private AppBar mAppBar;
	private MaterialEditText etTopicName;
	private Topic mTopic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_topic);
		
		mAppBar = (AppBar)findViewById(R.id.edit_topic_app_bar);
		mAppBar.getLogoView().setImageResource(R.drawable.menu_back);
		mAppBar.getLogoView().setLayoutParams(new RelativeLayout.LayoutParams(Utils.px2Dp(this, 56),Utils.px2Dp(this, 56)));
		mAppBar.getLogoView().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		mAppBar.getTextView().setVisibility(View.VISIBLE);
		mAppBar.getTextView().setText(R.string.chat_topic_edit);
		
		mAppBar.getMenuItemView1().setVisibility(View.VISIBLE);
		mAppBar.getMenuItemView1().setImageResource(R.drawable.menu_done);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(Utils.px2Dp(this, 56),Utils.px2Dp(this, 56));
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mAppBar.getMenuItemView1().setLayoutParams(rlp);
		
		etTopicName = (MaterialEditText)findViewById(R.id.edit_topic_materialEditText);
		etTopicName.setLineFocusedColor(getResources().getColor(R.color.no1));
		etTopicName.setLineUnFocusedColor(getResources().getColor(R.color.no1));
		etTopicName.setLineFocusedHeight(4);
		etTopicName.setLineUnFocusedHeight(1);
		etTopicName.getEditText().setTextColor(getResources().getColor(R.color.no1));
		etTopicName.getEditText().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		etTopicName.getEditText().setSingleLine();
		etTopicName.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		checkBundle();
	}
	
	private void checkBundle(){
		if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey(Constant.INTENT_EXTRA_KEY_TOPIC)){
			mTopic = (Topic) getIntent().getExtras().getSerializable(Constant.INTENT_EXTRA_KEY_TOPIC);
			
			mAppBar.getMenuItemView1().setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					IMManager.getInstance(v.getContext()).updateTopicName(etTopicName.getEditText().getText().toString(),mTopic);
					onBackPressed();
				}
			});
		}
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.slide_out_right);
	}
	
}
