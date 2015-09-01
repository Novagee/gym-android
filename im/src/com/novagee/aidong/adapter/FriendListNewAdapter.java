package com.novagee.aidong.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arrownock.social.IAnSocialCallback;
import com.novagee.aidong.R;
import com.novagee.aidong.activity.UserDetailActivity;
import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.controller.UserManager.FetchUserCallback;
import com.novagee.aidong.imageloader.ImageLoader;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.DBug;
import com.novagee.aidong.view.ListViewLoader;

public class FriendListNewAdapter extends BaseAdapter {
	private List<User> userData;
	private List<User> filteredUserData;
	private List<Object> data;
	private Context ct;
	
	private long lastTimeFetch = 0l;
	private final static long REFRESH_RATE = 5000l;
	private ListViewLoader m_loader;
	
	public FriendListNewAdapter(Context ct,GridView gridView){
		this.ct = ct;
		userData = new ArrayList<User>();
		data = new ArrayList<Object>();
		this.m_loader = new ListViewLoader(ct, gridView);
		this.m_loader.setErrorButtonClickListener(mErrorClickListener);
	}
	
	public void fillRemoteData(boolean fillLocalDataFirst){
		Calendar c = Calendar.getInstance();
		if(c.getTimeInMillis() - lastTimeFetch < REFRESH_RATE){
			fillLocalData();
			return;
		}
		
		if(fillLocalDataFirst){
			fillLocalData();
		}
		UserManager.getInstance(ct).fetchMyRemoteFriend( new IAnSocialCallback(){
			@Override
			public void onFailure(JSONObject arg0) {
			}
			@Override
			public void onSuccess(JSONObject arg0) {
				Calendar c = Calendar.getInstance();
				lastTimeFetch = c.getTimeInMillis();
				fillLocalData();
			}
		});
	}
	
	public void fillLocalData(){
		DBug.e("FriendListAda", "fillLocalData");
		m_loader.showLoading();
		UserManager.getInstance(ct).getMyLocalFriends(new FetchUserCallback(){
			@Override
			public void onFinish(List<User> users) {
				userData.clear();
				userData.addAll(users);
				filter(null);
			}
		});
	}
	
	private void mergeData(){
		data.clear();
		
		if(filteredUserData.size()!=0 && filteredUserData!=null){
	//		data.add(ct.getString(R.string.friend_friend_list));
			data.addAll(filteredUserData);
		}
		//Add by seeyet,2015/08/27,好友列表里面去掉自己
		for(int i =0;i<data.size();i++){
			if(data.get(i) instanceof User){
				User user = (User)data.get(i);
			    if(user.userId.equals(UserManager.getInstance(ct).getCurrentUser().userId)){
			        System.out.println("remove user:"+user.userId);
				   data.remove(i);
				   break;
			    }
			}
		}

		notifyDataSetChanged();
		
	}
	
	public void filter(String charText) {
		filteredUserData = new ArrayList<User>();
		if (charText==null || charText.trim().length() == 0) {
			//filteredUserData.addAll(userData);
			for (User user : userData) {
				if (!(user.userPhotoUrl== null ||"".equals(user.userPhotoUrl))) {
					filteredUserData.add(user);
				}
			}
		} else {
			for (User user : userData) {
				if (user.userName.contains(charText) && !(user.userPhotoUrl== null ||"".equals(user.userPhotoUrl))) {
					filteredUserData.add(user);
				}
			}
		}
		
		mergeData();
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendListItem view = (FriendListItem) convertView;
		if (convertView == null) {
			view = new FriendListItem(parent.getContext());
		}
		
		view.setData(data.get(position));
		
		return view;
	}
	
	
	public class FriendListItem extends FrameLayout{
		private ImageView imgIcon;
		private TextView textName;
		private Context ct;
		private View btnChat;
		public FriendListItem(Context context) {
			super(context);
			this.ct = context;
	        inflate(getContext(), R.layout.view_user_list_item_new, this);
	        this.textName = (TextView)findViewById(R.id.user_list_item_name);
	        this.imgIcon = (ImageView)findViewById(R.id.user_list_item_icon);
	        this.btnChat = (View) findViewById(R.id.btn_chat);
		}
		
		public void setData(Object data){
			if(data instanceof User){
				User user = (User) data;
				ImageLoader.getInstance(ct).DisplayImage(user.userPhotoUrl, imgIcon, R.drawable.friend_default,true);
				textName.setText(user.userName);
				btnChat.setOnClickListener(getOnClickListener(user));
			}
			
		}
		
		private OnClickListener getOnClickListener(final User user){
			return new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(ct,UserDetailActivity.class);
					i.putExtra(Constant.INTENT_EXTRA_KEY_CLIENT,user.clientId);
					ct.startActivity(i);
					((Activity) ct).overridePendingTransition(R.anim.push_up_in,android.R.anim.fade_out);
				}
			};
			
		}
	}
	
	private View.OnClickListener mErrorClickListener = new OnClickListener() {			
		@Override
		public void onClick(View v) {
			fillLocalData();			
		}
	};
	

}
