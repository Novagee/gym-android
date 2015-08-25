package com.novagee.aidong.fragment;

import java.util.Observable;
import java.util.Observer;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.novagee.aidong.R;

import com.arrownock.im.callback.AnIMBinaryCallbackData;
import com.arrownock.im.callback.AnIMCreateTopicCallbackData;
import com.arrownock.im.callback.AnIMGetTopicInfoCallbackData;
import com.arrownock.im.callback.AnIMGetTopicListCallbackData;
import com.arrownock.social.IAnSocialCallback;
import com.novagee.aidong.activity.ChatActivity;
import com.novagee.aidong.activity.CreateTopicActivity;
import com.novagee.aidong.activity.FriendRequestActivity;
import com.novagee.aidong.activity.SearchUserActivity;
import com.novagee.aidong.activity.UserDetailActivity;
import com.novagee.aidong.adapter.FriendListAdapter;
import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.im.model.Chat;
import com.novagee.aidong.im.model.Topic;
import com.novagee.aidong.im.model.TopicMember;
import com.novagee.aidong.im.view.ChatView;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.DBug;
import com.novagee.aidong.utils.Utils;
import com.novagee.aidong.view.BadgeView;
import com.novagee.aidong.view.UserListItem;

public class FriendListFragment extends BaseFragment implements Observer{
	private ListView mListView;
	private ImageView imgNewBtn;
	private FriendListAdapter mFriendListAdapter;
	private Context ct;
	private BadgeView friendRequestBadge ;
	private AlertDialog mActionDialog;
	private Handler handler;
	
	public FriendListFragment(String title) {
		super(title);
		handler = new Handler();
	}
	
	@Override
	public void onViewShown(){
		initData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend, container, false);
        ct = getActivity();
        
        return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		IMManager.getInstance(ct).addObserver(this);
		UserManager.getInstance(ct).addObserver(this);
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
//		IMManager.getInstance(ct).deleteObserver(this);
//		UserManager.getInstance(ct).deleteObserver(this);
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);

        initView(ct);
		//initData();
	}
	
	private void initView(final Context ct){
        mListView = (ListView) getActivity().findViewById(R.id.friend_listView);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        
        UserListItem friendRequestBtn = new UserListItem(ct);
        friendRequestBtn.setLayoutParams(new AbsListView.LayoutParams(-1,Utils.px2Dp(ct, 72)));
        friendRequestBtn.setName(ct.getString(R.string.friend_friend_request));
        friendRequestBtn.setIcon(R.drawable.friend_request);
        friendRequestBadge = new BadgeView(ct);
        friendRequestBadge.setTextSize(TypedValue.COMPLEX_UNIT_DIP , 14);
        friendRequestBadge.setTextColor(ct.getResources().getColor(R.color.no5));
        friendRequestBadge.setBadgeColor(ct.getResources().getColor(R.color.no1));
        RelativeLayout.LayoutParams rlpBadge = new RelativeLayout.LayoutParams(-2,-2);
        rlpBadge.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlpBadge.addRule(RelativeLayout.CENTER_VERTICAL);
        rlpBadge.rightMargin = Utils.px2Dp(ct, 20);
        friendRequestBtn.addView(friendRequestBadge,rlpBadge);
        //Remark by seeyet,2015/08/25,已经不需要添加好友功能了
    //   mListView.addHeaderView(friendRequestBtn);
        friendRequestBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(),FriendRequestActivity.class));
				getActivity().overridePendingTransition(R.anim.slide_in_right,android.R.anim.fade_out);
			}
        });
        
        mFriendListAdapter = new FriendListAdapter(ct);
        mListView.setAdapter(mFriendListAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				position -= mListView.getHeaderViewsCount();
				if(mFriendListAdapter.getItem(position) instanceof Topic){
					Topic topic = (Topic) mFriendListAdapter.getItem(position);
					Chat chat = IMManager.getInstance(ct).addChat(topic);
					IMManager.getInstance(ct).notifyChatUpdated();
					Intent i = new Intent(ct,ChatActivity.class);
					Bundle b = new Bundle();
					b.putSerializable(Constant.INTENT_EXTRA_KEY_CHAT, chat);
					i.putExtras(b);
					ct.startActivity(i);
				}else if(mFriendListAdapter.getItem(position) instanceof User){
					User user = (User) mFriendListAdapter.getItem(position);
					Intent i = new Intent(view.getContext(),UserDetailActivity.class);
					i.putExtra(Constant.INTENT_EXTRA_KEY_CLIENT,user.clientId);
					view.getContext().startActivity(i);
					((Activity) view.getContext()).overridePendingTransition(R.anim.push_up_in,android.R.anim.fade_out);
				}
			}
        });
        
        imgNewBtn = (ImageView) getActivity().findViewById(R.id.img_new_btn);
        imgNewBtn.setVisibility(View.GONE);//modified by seeyet,2015/08/23，隐藏添加好友按钮
        imgNewBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Modified by seeyet,2015/08/22直接跳转搜索好友
				Intent i = new Intent(ct,SearchUserActivity.class);
		    	ct.startActivity(i);
				//mActionDialog.show();
			}
        });
        
		AlertDialog.Builder dialogBuiler = new AlertDialog.Builder(ct);
		View view = getActivity().getLayoutInflater().inflate( R.layout.view_friend_alert, null);
		view.findViewById(R.id.action_dialog_friend_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ct,SearchUserActivity.class);
		    	ct.startActivity(i);
				getActivity().overridePendingTransition(R.anim.slide_in_right,android.R.anim.fade_out);
		    	mActionDialog.dismiss();
			}
		});
		view.findViewById(R.id.action_dialog_topic_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ct,CreateTopicActivity.class);
				Bundle b = new Bundle();
				b.putString(Constant.INTENT_EXTRA_KEY_TOPIC_EDIT_TYPE, CreateTopicActivity.TYPE_CREATE);
				i.putExtras(b);
		    	ct.startActivity(i);
				getActivity().overridePendingTransition(R.anim.slide_in_right,android.R.anim.fade_out);
		    	mActionDialog.dismiss();
			}
		});
		dialogBuiler.setView(view);
		mActionDialog = dialogBuiler.create();
	}

	public void initData(){
        mFriendListAdapter.fillLocalData();
		//mFriendListAdapter.fillRemoteData(true);
        DBug.e("inirData",mFriendListAdapter.getCount()+"");
        int badgeCount = UserManager.getInstance(ct).getLocalPendingFriendRequestCount();
        friendRequestBadge.setBadgeCount(badgeCount);
	}
	
	private void updateFriendRequestBadge(){
		UserManager.getInstance(ct).fetchFriendRequest(new IAnSocialCallback(){
			@Override
			public void onFailure(JSONObject arg0) {
				DBug.e("fetchFriendRequest.onFailure",arg0.toString());
			}
			@Override
			public void onSuccess(JSONObject arg0) {
				DBug.e("fetchFriendRequest",arg0.toString());
				handler.post(new Runnable(){
					@Override
					public void run() {
						friendRequestBadge.setBadgeCount(UserManager.getInstance(ct).getLocalPendingFriendRequestCount());
					}
				});
			}
		});
	}
	
	public void filterList(String text){
		mFriendListAdapter.filter(text);
	}
	
	@Override
	public void update(final Observable observable, final Object data) {
		handler.post(new Runnable(){
			@Override
			public void run() {
				if(observable instanceof IMManager){
					if(data instanceof IMManager.UpdateType && ((IMManager.UpdateType)data).equals(IMManager.UpdateType.Topic)){
						mFriendListAdapter.fillLocalData();
					}else if(data instanceof IMManager.UpdateType && ((IMManager.UpdateType)data).equals(IMManager.UpdateType.FriendRequest)){
						updateFriendRequestBadge();
						mFriendListAdapter.fillLocalData();
					}
				}else if(observable instanceof UserManager){
					DBug.e("UserManager", "update()");
					if(data instanceof UserManager.UpdateType){
						mFriendListAdapter.fillLocalData();
						if(((UserManager.UpdateType)data).equals(UserManager.UpdateType.Friend)){
							
						}
					}
				}
			}
		});
	};
}
