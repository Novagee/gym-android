package com.jianyue.ui.fragment.components;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianyue.main.controller.R;
import com.jianyue.utils.Constant;

public class BottomControlPanel extends RelativeLayout implements View.OnClickListener {
	private Context mContext;
	private ImageText mChatBtn = null;
	private ImageText mFriendBtn = null;
	private ImageText mEventBtn = null;
	private ImageText mSettingBtn = null;
	private int DEFALUT_BACKGROUND_COLOR = Color.rgb(243, 243, 243); //Color.rgb(192, 192, 192)
	private BottomPanelCallback mBottomCallback = null;
	private List<ImageText> viewList = new ArrayList<ImageText>();

	public interface BottomPanelCallback{
		public void onBottomPanelClick(int itemId);
	}
	public BottomControlPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		mChatBtn = (ImageText)findViewById(R.id.btn_chat);
		mFriendBtn = (ImageText)findViewById(R.id.btn_friend);
		mEventBtn = (ImageText)findViewById(R.id.btn_event);
		mSettingBtn = (ImageText)findViewById(R.id.btn_setting);
		setBackgroundColor(DEFALUT_BACKGROUND_COLOR);
		viewList.add(mChatBtn);
		viewList.add(mFriendBtn);
		viewList.add(mEventBtn);
		viewList.add(mSettingBtn);

	}
	public void initBottomPanel(){
		if(mChatBtn != null){
			mChatBtn.setImage(R.drawable.ico_home_unselected);
			mChatBtn.setText("聊天");
		}
		if(mFriendBtn != null){
			mFriendBtn.setImage(R.drawable.ico_contacts_unselected);
			mFriendBtn.setText("朋友圈");
		}
		if(mEventBtn != null){
			mEventBtn.setImage(R.drawable.ico_book_unselected);
			mEventBtn.setText("活动");
		}
		if(mSettingBtn != null){
			mSettingBtn.setImage(R.drawable.ico_nearby_unselected);
			mSettingBtn.setText("设置");
		}
		setBtnListener();

	}
	private void setBtnListener(){
		int num = this.getChildCount();
		for(int i = 0; i < num; i++){
			View v = getChildAt(i);
			if(v != null){
				v.setOnClickListener(this);
			}
		}
	}
	public void setBottomCallback(BottomPanelCallback bottomCallback){
		mBottomCallback = bottomCallback;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		initBottomPanel();
		int index = -1;
		switch(v.getId()){
		case R.id.btn_chat:
			index = Constant.BTN_FLAG_CHAT;
			mChatBtn.setChecked(Constant.BTN_FLAG_CHAT);
			break;
		case R.id.btn_friend:
			index = Constant.BTN_FLAG_FRIEND;
			mFriendBtn.setChecked(Constant.BTN_FLAG_FRIEND);
			break;
		case R.id.btn_event:
			index = Constant.BTN_FLAG_EVENT;
			mEventBtn.setChecked(Constant.BTN_FLAG_EVENT);
			break;
		case R.id.btn_setting:
			index = Constant.BTN_FLAG_SETTING;
			mSettingBtn.setChecked(Constant.BTN_FLAG_SETTING);
			break;
		default:break;
		}
		if(mBottomCallback != null){
			mBottomCallback.onBottomPanelClick(index);
		}
	}
	public void defaultBtnChecked(){
		if(mChatBtn != null){
			mChatBtn.setChecked(Constant.BTN_FLAG_CHAT);
		}
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		layoutItems(left, top, right, bottom);
	}
	/**最左边和最右边的view由母布局的padding进行控制位置。这里需对第2、3个view的位置重新设置
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	private void layoutItems(int left, int top, int right, int bottom){
		int n = getChildCount();
		if(n == 0){
			return;
		}
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		Log.i("yanguoqi", "paddingLeft = " + paddingLeft + " paddingRight = " + paddingRight);
		int width = right - left;
		int height = bottom - top;
		Log.i("yanguoqi", "width = " + width + " height = " + height);
		int allViewWidth = 0;
		for(int i = 0; i< n; i++){
			View v = getChildAt(i);
			Log.i("yanguoqi", "v.getWidth() = " + v.getWidth());
			allViewWidth += v.getWidth();
		}
		int blankWidth = (width - allViewWidth - paddingLeft - paddingRight) / (n - 1);
		Log.i("yanguoqi", "blankV = " + blankWidth );

		LayoutParams params1 = (LayoutParams) viewList.get(1).getLayoutParams();
		params1.leftMargin = blankWidth;
		viewList.get(1).setLayoutParams(params1);

		LayoutParams params2 = (LayoutParams) viewList.get(2).getLayoutParams();
		params2.leftMargin = blankWidth;
		viewList.get(2).setLayoutParams(params2);
	}



}
