package com.gym.newerafitness.ui.components;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.gym.newerafitness.R;
import com.gym.newerafitness.constant.Constant;

public class BottomControlPanel extends RelativeLayout implements View.OnClickListener {
	private Context mContext;
	private ImageText mHomeBtn = null;
	private ImageText mContactsBtn = null;
	private ImageText mBookBtn = null;
	private ImageText mNearByBtn = null;
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
		mHomeBtn = (ImageText)findViewById(R.id.btn_home);
		mContactsBtn = (ImageText)findViewById(R.id.btn_contacts);
		mBookBtn = (ImageText)findViewById(R.id.btn_book);
		mNearByBtn = (ImageText)findViewById(R.id.btn_nearby);
		setBackgroundColor(DEFALUT_BACKGROUND_COLOR);
		viewList.add(mHomeBtn);
		viewList.add(mContactsBtn);
		viewList.add(mBookBtn);
		viewList.add(mNearByBtn);

	}
	public void initBottomPanel(){
		if(mHomeBtn != null){
			mHomeBtn.setImage(R.drawable.ico_home_unselected);
			mHomeBtn.setText("首页");
		}
		if(mContactsBtn != null){
			mContactsBtn.setImage(R.drawable.ico_contacts_unselected);
			mContactsBtn.setText("会员社交");
		}
		if(mBookBtn != null){
			mBookBtn.setImage(R.drawable.ico_book_unselected);
			mBookBtn.setText("网上预约");
		}
		if(mNearByBtn != null){
			mNearByBtn.setImage(R.drawable.ico_nearby_unselected);
			mNearByBtn.setText("附近商家");
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
		case R.id.btn_home:
			index = Constant.BTN_FLAG_HOME;
			mHomeBtn.setChecked(Constant.BTN_FLAG_HOME);
			break;
		case R.id.btn_contacts:
			index = Constant.BTN_FLAG_MESSAGE;
			mContactsBtn.setChecked(Constant.BTN_FLAG_MESSAGE);
			break;
		case R.id.btn_book:
			index = Constant.BTN_FLAG_BOOK;
			mBookBtn.setChecked(Constant.BTN_FLAG_BOOK);
			break;
		case R.id.btn_nearby:
			index = Constant.BTN_FLAG_NEARBY;
			mNearByBtn.setChecked(Constant.BTN_FLAG_NEARBY);
			break;
		default:break;
		}
		if(mBottomCallback != null){
			mBottomCallback.onBottomPanelClick(index);
		}
	}
	public void defaultBtnChecked(){
		if(mHomeBtn != null){
			mHomeBtn.setChecked(Constant.BTN_FLAG_HOME);
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
