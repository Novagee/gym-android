package com.gym.newerafitness.ui.fragment;

import com.gym.newerafitness.MainActivity;

import com.gym.newerafitness.R;
import com.gym.newerafitness.constant.Constant;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contactsLayout = inflater.inflate(R.layout.home_layout,
				container, false);
		return contactsLayout;
	}

	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		MainActivity.currFragTag = Constant.FRAGMENT_FLAG_HOME;
	}
	
	
	
}
