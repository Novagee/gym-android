package com.novagee.aidong.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.novagee.aidong.im.controller.IMManager.GetChatCallback;
import com.novagee.aidong.im.model.Chat;
import com.novagee.aidong.im.model.Message;
import com.novagee.aidong.im.model.Topic;
import com.novagee.aidong.im.model.TopicMember;
import com.novagee.aidong.imageloader.ImageLoader;
import com.novagee.aidong.model.Friend;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.DBug;
import com.novagee.aidong.utils.Utils;
import com.novagee.aidong.view.BadgeView;
import com.novagee.aidong.view.ListViewLoader;
import com.novagee.aidong.view.UserListItem;

import com.novagee.aidong.R;

import android.content.Context;
import android.os.Handler;
import android.provider.CalendarContract.CalendarCache;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ChatListAdapter extends BaseAdapter {
	private List<Chat> chatList;
	private List<Chat> filteredChatList;
	private Map<String,User> userMap;
	private Context ct;
	private Handler handler;
	private ListViewLoader m_loader;
	
	public ChatListAdapter(Context ct, ListView listView){
		this.ct = ct;
		chatList = new ArrayList<Chat>();
		filteredChatList = new ArrayList<Chat>();
		userMap = new HashMap<String,User>();
		handler = new Handler();
		this.m_loader = new ListViewLoader(ct, listView);
		this.m_loader.setErrorButtonClickListener(mErrorClickListener);
	}
	
	public Map<String,User> getUserMap(){
		return userMap;
	}
	
	public void removeItem(int position){
		filteredChatList.remove(position);
		notifyDataSetChanged();
		if(filteredChatList.size() == 0){
			m_loader.showEmpty();
		}
	}
	
	public void applyData(List<Chat> chats){
		chatList.clear();
		chatList.addAll(chats);
		filter(null);
		
		refreshUseMap();
		notifyDataSetChanged();
		if(filteredChatList.size() == 0){
			m_loader.showEmpty();
		}
	}
	
	public void fillLocalData(){
		m_loader.showLoading();
		IMManager.getInstance(ct).getAllMyChat(new GetChatCallback(){
			@Override
			public void onFinish(List<Chat> data) {
				applyData(data);
			}
		});
	}
	
	private void refreshUseMap(){
		final List<Chat> chats = new ArrayList<Chat>();
		chats.addAll(chatList);
		new Thread(new Runnable(){
			@Override
			public void run() {
				for(Chat chat : chats){
					if(chat.topic==null && chat.targetClientId!=null && !userMap.containsKey(chat.targetClientId)){
						User user = UserManager.getInstance(ct).getUserByClientId(chat.targetClientId);
						userMap.put(chat.targetClientId, user);
					}
				}
				handler.post(new Runnable(){
					@Override
					public void run() {
						notifyDataSetChanged();
					}
				});
				
			}
		}).start();
	}
	
	public void filter(String charText) {
		filteredChatList.clear();
		if (charText==null || charText.length() == 0) {
			filteredChatList.addAll(chatList);
		} else {
			for (Chat chat : chatList) {
				if(chat.topic!=null && chat.topic.topicName.contains(charText)){
					filteredChatList.add(chat);
				}else if(chat.targetClientId!=null){
					if(userMap.containsKey(chat.targetClientId) && userMap.get(chat.targetClientId).userName.contains(charText)){
						filteredChatList.add(chat);
					}
				}
			}
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filteredChatList.size();
	}

	@Override
	public Chat getItem(int position) {
		// TODO Auto-generated method stub
		return filteredChatList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatListItem view = (ChatListItem) convertView;
		if (convertView == null) {
			view = new ChatListItem(parent.getContext());
		}
		
		view.setData(filteredChatList.get(position));
		
		return view;
	}
	
	private View.OnClickListener mErrorClickListener = new OnClickListener() {			
		@Override
		public void onClick(View v) {
			fillLocalData();			
		}
	};
	
	public class ChatListItem extends RelativeLayout{
		private TextView textName,textMsg,textTime;
		private ImageView imgIcon;
		private BadgeView badge;
		
		public ChatListItem(Context ct) {
			super(ct);
	        inflate(getContext(), R.layout.view_chat_list_item, this);
	        this.textName = (TextView)findViewById(R.id.chat_list_item_name);
	        this.textMsg = (TextView)findViewById(R.id.chat_list_item_msg);
	        this.textTime = (TextView)findViewById(R.id.chat_list_item_timestamp);
	        this.imgIcon = (ImageView)findViewById(R.id.chat_list_item_icon);
	        this.badge = (BadgeView)findViewById(R.id.chat_list_item_badge);
	        badge.setTextSize(TypedValue.COMPLEX_UNIT_DIP , 14);
	        badge.setTextColor(ct.getResources().getColor(R.color.no5));
	        badge.setBadgeColor(ct.getResources().getColor(R.color.no1));
		}
		
		public void setData(Chat chat){
			if(chat.topic!=null){
				textName.setText(chat.topic.topicName+"("+chat.topic.members().size()+")");
				imgIcon.setImageResource(R.drawable.friend_group);
				
			}else if(chat.targetClientId!=null){
				if(userMap.containsKey(chat.targetClientId)){
					textName.setText(userMap.get(chat.targetClientId).userName);
					ImageLoader.getInstance(ct).DisplayImage(userMap.get(chat.targetClientId).userPhotoUrl, imgIcon, R.drawable.friend_default, true);
				}
			}
			
			Message lastMsg = chat.lastMessage();
			if(lastMsg!=null){
				if(lastMsg.type.equals(Message.TYPE_TEXT)){
					textMsg.setText(lastMsg.message);
					
				}else if(lastMsg.type.equals(Message.TYPE_IMAGE)){
					if(lastMsg.fromClient.equals(IMManager.getInstance(ct).getCurrentClientId())){
						textMsg.setText(R.string.chat_send_image);
					}else{
						String text = getContext().getString(R.string.chat_received_image);
						text = text.replace("#", lastMsg.fromUsername);
						textMsg.setText(text);
					}
					
				}else if(lastMsg.type.equals(Message.TYPE_RECORD)){
					if(lastMsg.fromClient.equals(IMManager.getInstance(ct).getCurrentClientId())){
						textMsg.setText(R.string.chat_send_record);
					}else{
						String text = getContext().getString(R.string.chat_received_record);
						text = text.replace("#", lastMsg.fromUsername);
						textMsg.setText(text);
					}
				}
				
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(lastMsg.timestamp);
				textTime.setText(new SimpleDateFormat("yyyy-MM-dd kk:mm").format(c.getTime()));
				
				badge.setBadgeCount(chat.unReadedMessages().size());
			}
		}
	}
}
