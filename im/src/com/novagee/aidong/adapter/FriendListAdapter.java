package com.novagee.aidong.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import com.arrownock.exception.ArrownockException;
import com.arrownock.social.IAnSocialCallback;
import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.controller.UserManager.FetchUserCallback;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.im.controller.IMManager.FetchLocalTopicCallback;
import com.novagee.aidong.im.model.Topic;
import com.novagee.aidong.im.model.TopicMember;
import com.novagee.aidong.model.Friend;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.DBug;
import com.novagee.aidong.utils.Utils;
import com.novagee.aidong.view.ListViewLoader;
import com.novagee.aidong.view.UserListItem;

import com.novagee.aidong.R;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class FriendListAdapter extends BaseAdapter {
	private List<User> userData;
	private List<User> filteredUserData;
	private List<Topic> topicData;
	private List<Topic> filteredTopicData;
	private List<Object> data;
	private Context ct;
	private ListViewLoader m_loader;
	
	private long lastTimeFetch = 0l;
	private final static long REFRESH_RATE = 5000l;
	
	public FriendListAdapter(Context ct, ListView listView){
		this.ct = ct;
		userData = new ArrayList<User>();
		topicData = new ArrayList<Topic>();
		data = new ArrayList<Object>();
		this.m_loader = new ListViewLoader(ct, listView);
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
				IMManager.getInstance(ct).fetchAllRemoteTopic();
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
		IMManager.getInstance(ct).getMyLocalTopic(new FetchLocalTopicCallback(){
			@Override
			public void onFinish(List<Topic> topics) {
//				DBug.e("FriendListAda.getMyLocalTopic", topics.size()+"");
//				for(Topic t :topics){
//					DBug.e("getAllLocalTopic", t.topicId);
//					for(TopicMember member :t.members()){
//						DBug.e("--- member", member.clientId);
//					}
//				}
				topicData.clear();
				topicData.addAll(topics);
				filter(null);
			}
		});
	}
	
	private void mergeData(){
		data.clear();
		if(filteredTopicData.size()!=0 && filteredTopicData!=null){
			data.add(ct.getString(R.string.friend_topic_list));
			data.addAll(filteredTopicData);
		}
		if(filteredUserData.size()!=0 && filteredUserData!=null){
			data.add(ct.getString(R.string.friend_friend_list));
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
		if(data.size() == 0){
			m_loader.showEmpty();
		}
	}
	
	public void filter(String charText) {
		filteredUserData = new ArrayList<User>();
		if (charText==null || charText.trim().length() == 0) {
			filteredUserData.addAll(userData);
		} else {
			for (User user : userData) {
				if (user.userName.contains(charText)) {
					filteredUserData.add(user);
				}
			}
		}
		
		filteredTopicData = new ArrayList<Topic>();
		if (charText==null || charText.length() == 0) {
			filteredTopicData.addAll(topicData);
		} else {
			for (Topic topic : topicData) {
				if (topic.topicName.contains(charText)) {
					filteredTopicData.add(topic);
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
	
	private View.OnClickListener mErrorClickListener = new OnClickListener() {			
		@Override
		public void onClick(View v) {
			fillLocalData();			
		}
	};

	public class FriendListItem extends FrameLayout{
		private UserListItem mUserListItem;
		private SectionHeader mSectionHeader;
		public FriendListItem(Context context) {
			super(context);
			mUserListItem = new UserListItem(context);
			addView(mUserListItem, new FrameLayout.LayoutParams(-1,Utils.px2Dp(context, 56)));
			mSectionHeader = new SectionHeader(context);
			addView(mSectionHeader, new FrameLayout.LayoutParams(-1,-2));
		}
		
		public void setData(Object data){
			mUserListItem.setVisibility(View.GONE);
			mSectionHeader.setVisibility(View.GONE);
			if(data instanceof User){
				mUserListItem.setVisibility(View.VISIBLE);
				User user = (User) data;
				mUserListItem.setIcon(user.userPhotoUrl,R.drawable.friend_default);
				if (user.userName != null) {
					mUserListItem.setName(user.userName);
				}
				
			}else if(data instanceof Topic){
				mUserListItem.setVisibility(View.VISIBLE);
				Topic topic = (Topic) data;
				mUserListItem.setIcon(R.drawable.friend_group);
				if (topic.topicName != null) {
					mUserListItem.setName(topic.topicName+"("+topic.members().size()+")");
				} else {
					if (topic.members() != null && topic.members().size() > 0) {
						String members = "";
						for (TopicMember topicMember : topic.members()) {
							String memberName = UserManager.getInstance(
									getContext()).getUserByClientId(
									topicMember.clientId).userName;
							members += memberName + ",";
						}
						members = members.substring(0, members.length() - 1);
						mUserListItem.setName(members);
					} else {
						mUserListItem.setName("");
					}
				}
			}else if(data instanceof String){
				mSectionHeader.setVisibility(View.VISIBLE);
				mSectionHeader.setTitle((String)data);
			}
		}
		
	}
	
	public class SectionHeader extends RelativeLayout{
		private TextView textTitle;
		public SectionHeader(Context ct) {
			super(ct);
			
			View viewLine = new View(ct);
			viewLine.setBackgroundColor(ct.getResources().getColor(R.color.no7));
			RelativeLayout.LayoutParams rlpLine = new RelativeLayout.LayoutParams(-1,Utils.px2Dp(ct, 1));
			rlpLine.topMargin = Utils.px2Dp(ct, 8);
			rlpLine.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			addView(viewLine,rlpLine);
			
			RelativeLayout.LayoutParams rlpTextTitle = new RelativeLayout.LayoutParams(-2,-2);
			rlpTextTitle.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rlpTextTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rlpTextTitle.setMargins(Utils.px2Dp(ct, 16), Utils.px2Dp(ct, 24), 0, Utils.px2Dp(ct, 8));
			textTitle = new TextView(ct);
			int padding = 0;
			textTitle.setPadding(padding, padding, padding, padding);
			textTitle.setTextColor(ct.getResources().getColor(R.color.no9));
			addView(textTitle,rlpTextTitle);
			
		}
		
		public void setTitle(String title){
			textTitle.setText(title);
		}
		
	}
}
