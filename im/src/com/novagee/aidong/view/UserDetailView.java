package com.novagee.aidong.view;

import com.novagee.aidong.imageloader.ImageLoader;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.Utils;

import com.novagee.aidong.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserDetailView extends RelativeLayout {
	private ImageView imgUserIcon;
	private TextView textUserName,textBtn;
	
	public UserDetailView(Context context) {
		super(context);
		init();
	}
	public UserDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public UserDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init(){
        inflate(getContext(), R.layout.view_user_detail, this);
        imgUserIcon = (ImageView) findViewById(R.id.user_detail_img);
        textUserName = (TextView) findViewById(R.id.user_detail_text_name);
        textBtn = (TextView) findViewById(R.id.user_detail_text_btn);
        
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getContext().getResources().getColor(R.color.no1));
        float corner = Utils.px2Dp(getContext(), 2);
        gd.setCornerRadii(new float[]{corner,corner,corner,corner,corner,corner,corner,corner});
        textBtn.setBackgroundDrawable(gd);
	}
	
	public ImageView getImageView(){
		return imgUserIcon;
	}
	
	public TextView getTextButton(){
		return textBtn;
	}
	
	public void setButton(String text,OnClickListener lsr){
		textBtn.setText(text);
		textBtn.setOnClickListener(lsr);
	}
	
	public void setUserInfo(User user){
		ImageLoader.getInstance(getContext()).DisplayImage(user.userPhotoUrl, imgUserIcon, R.drawable.friend_default, true);
		textUserName.setText(user.userName);
	}
}
