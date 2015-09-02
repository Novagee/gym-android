package com.novagee.aidong.fragment;

import java.util.Observable;
import java.util.Observer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.novagee.aidong.R;
import com.novagee.aidong.activity.CreateTopicActivity;
import com.novagee.aidong.activity.SearchUserActivity;
import com.novagee.aidong.adapter.FriendListNewAdapter;
import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.DBug;

public class FriendListFragmentNew extends BaseFragment implements Observer{
	private GridView mGridView;
	private FriendListNewAdapter mFriendListAdapter;
	private Context ct;
	private AlertDialog mActionDialog;
	private Handler handler;
	
	public FriendListFragmentNew(String title) {
		super(title);
		handler = new Handler();
	}
	
	@Override
	public void onViewShown(){
		initData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend_new, container, false);
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
		mGridView = (GridView) getActivity().findViewById(R.id.friend_gridView);
       
		mFriendListAdapter = new FriendListNewAdapter(ct,mGridView);
		mGridView.setAdapter(mFriendListAdapter);
        
       
        
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
        mFriendListAdapter.fillRemoteData(true);

		//mFriendListAdapter.fillRemoteData(true);
        DBug.e("inirData",mFriendListAdapter.getCount()+"");
    //    int badgeCount = UserManager.getInstance(ct).getLocalPendingFriendRequestCount();
    //    friendRequestBadge.setBadgeCount(badgeCount);
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
					//	mFriendListAdapter.fillLocalData();
					}else if(data instanceof IMManager.UpdateType && ((IMManager.UpdateType)data).equals(IMManager.UpdateType.FriendRequest)){
					//	updateFriendRequestBadge();
					//	mFriendListAdapter.fillLocalData();
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
