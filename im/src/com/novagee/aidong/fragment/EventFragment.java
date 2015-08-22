package com.novagee.aidong.fragment;

import java.util.ArrayList;

import com.novagee.aidong.activity.MainActivity;
import com.novagee.aidong.adapter.ChatListAdapter;
import com.novagee.aidong.adapter.EventListAdapter;
import com.novagee.aidong.controller.FetchEventListDataTask;
import com.novagee.aidong.model.Event;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.InternetChecker;
import com.novagee.aidong.utils.webservices.APIResponse;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.novagee.aidong.R;

public class EventFragment extends BaseFragment {
	private ListView mListView;
	private ArrayList<Event> mDataList = new ArrayList<Event>();
	private EventListAdapter mEventListAdapter;
	private Context ct;
	public EventFragment(String title) {
		super(title);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event, container,
				false);
		this.ct = getActivity();
		mListView = (ListView) rootView.findViewById(R.id.listView);
		mEventListAdapter = new EventListAdapter(ct);
		mListView.setAdapter(mEventListAdapter);
		return rootView;
	}
	
	
	@Override
	public void onViewShown(){
		mEventListAdapter.fillData();
	}
	
	@Override  
	public void setUserVisibleHint(boolean isVisibleToUser) {  
	       super.setUserVisibleHint(isVisibleToUser);  
	       if (isVisibleToUser) {  
	           //相当于Fragment的onResume  
	       } else {  
	           //相当于Fragment的onPause  
	       }  
	}  

}
