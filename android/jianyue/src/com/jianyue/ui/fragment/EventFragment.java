package com.jianyue.ui.fragment;

import com.jianyue.main.controller.MainActivityNew;

import com.jianyue.main.controller.R;
import com.jianyue.utils.Constant;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View newsLayout = inflater.inflate(R.layout.event_layout, container,
				false);
		return newsLayout;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		MainActivityNew.currFragTag = Constant.FRAGMENT_FLAG_EVENT;
	}
	

}
