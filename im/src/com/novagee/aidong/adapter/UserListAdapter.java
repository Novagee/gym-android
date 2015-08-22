package com.novagee.aidong.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.arrownock.exception.ArrownockException;
import com.arrownock.social.IAnSocialCallback;
import com.novagee.aidong.activity.SearchUserActivity;
import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.controller.UserManager.FetchFriendCallback;
import com.novagee.aidong.controller.UserManager.FetchUserCallback;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.im.controller.IMManager.FetchLocalTopicCallback;
import com.novagee.aidong.im.model.Topic;
import com.novagee.aidong.im.model.TopicMember;
import com.novagee.aidong.model.Friend;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.DBug;
import com.novagee.aidong.utils.Utils;
import com.novagee.aidong.view.UserListItem;

import com.novagee.aidong.R;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class UserListAdapter extends BaseAdapter {
	private List<User> userList;
	private Map<String,Boolean> friendStatus;
	private Context ct;
	
	public UserListAdapter(Context ct){
		this.ct = ct;
		userList = new ArrayList<User>();
		friendStatus = new HashMap<String,Boolean>();
	}
	
	public void applyData(List<User> users){
		userList.clear();
		userList.addAll(users);
		
		refreshFriendStatus();
		
		notifyDataSetChanged();
		
	}
	
	private void refreshFriendStatus(){
		UserManager.getInstance(ct).getMyLocalFriends(new FetchFriendCallback(){
			@Override
			public void onFinish(List<Friend> friends) {
				friendStatus.clear();
				for(Friend f :friends){
					friendStatus.put(f.targetClientId, f.isMutual);
				}
				notifyDataSetChanged();
			}
		});
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();
	}

	@Override
	public User getItem(int position) {
		// TODO Auto-generated method stub
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewFriendListItem view = (NewFriendListItem) convertView;
		if (convertView == null) {
			view = new NewFriendListItem(parent.getContext());
		}
		
		view.setData(userList.get(position));
		
		return view;
	}
	
	public class NewFriendListItem extends UserListItem{
		private TextView textFriendStatus;
		public NewFriendListItem(Context ct) {
			super(ct);

			setLayoutParams(new AbsListView.LayoutParams(-1,Utils.px2Dp(ct, 56)));
			textFriendStatus = new TextView(ct);
			textFriendStatus.setGravity(Gravity.CENTER_VERTICAL);
			textFriendStatus.setPadding(Utils.px2Dp(ct, 24), 0, Utils.px2Dp(ct, 24), 0);
			RelativeLayout.LayoutParams rlpTextFS = new RelativeLayout.LayoutParams(-2,Utils.px2Dp(ct, 36));
			rlpTextFS.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rlpTextFS.addRule(RelativeLayout.CENTER_VERTICAL);
			rlpTextFS.rightMargin = Utils.px2Dp(ct,16);
			addView(textFriendStatus,rlpTextFS);
		}
		
		public void setData(final User user){
			setIcon(user.userPhotoUrl, R.drawable.friend_default);
			setName(user.userName);

			float corner = Utils.px2Dp(ct, 2);
			GradientDrawable bgBtn = new GradientDrawable();
			bgBtn.setCornerRadii(new float[]{corner,corner,corner,corner,corner,corner,corner,corner});
			
			if(friendStatus.containsKey(user.clientId)){
				if(friendStatus.get(user.clientId)){
					textFriendStatus.setTextColor(ct.getResources().getColor(R.color.no9));
					bgBtn.setColor(getResources().getColor(R.color.no6));
					textFriendStatus.setBackgroundDrawable(bgBtn);
					textFriendStatus.setText(R.string.friend_request_status_isfriend);
				}else{
					textFriendStatus.setTextColor(ct.getResources().getColor(R.color.no5));
					textFriendStatus.setBackgroundColor(ct.getResources().getColor(R.color.no1));
					textFriendStatus.setText(R.string.friend_request_status_sent);
				}
				textFriendStatus.setOnClickListener(null);
			}else{
				textFriendStatus.setTextColor(ct.getResources().getColor(R.color.no5));
				textFriendStatus.setBackgroundColor(ct.getResources().getColor(R.color.no3));
				textFriendStatus.setText(R.string.friend_request_status_add);
				textFriendStatus.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						DBug.e(user.userName, user.clientId);
						UserManager.getInstance(ct).sendFriendRequest(user,new IAnSocialCallback(){
							@Override
							public void onFailure(JSONObject arg0) {
								
							}

							@Override
							public void onSuccess(JSONObject arg0) {
								refreshFriendStatus();
							}
							
						});
					}
				});
			}
		}
	}
}
