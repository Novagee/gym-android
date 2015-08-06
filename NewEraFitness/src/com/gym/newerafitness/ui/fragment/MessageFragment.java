package com.gym.newerafitness.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.gym.newerafitness.MainActivity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gym.newerafitness.R;
import com.gym.newerafitness.adapter.MessageAdapter;
import com.gym.newerafitness.bean.MessageBean;
import com.gym.newerafitness.constant.Constant;


public class MessageFragment extends BaseFragment {

	private static final String TAG = "MessageFragment";
	private MainActivity mMainActivity ;
	private ListView mListView;
	private MessageAdapter mMsgAdapter;
	private List<MessageBean> mMsgBean = new ArrayList<MessageBean>();
	
	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView contactsTextView, talkTextView, historyTextView;// 选项名称
	private List<Fragment> fragments;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int selectedColor, unSelectedColor;
	/** 页卡总数 **/
	private static final int pageSize = 3;
	private View messageLayout ;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*
		View messageLayout = inflater.inflate(R.layout.contacts_layout,
				container, false);*/
		Log.d(TAG, "onCreateView---->");
		mMainActivity = (MainActivity) getActivity();
		mFragmentManager = getActivity().getSupportFragmentManager();
		
		messageLayout = inflater.inflate(R.layout.message_tabwidget,
				container, false);
		;
	
		initView();
	
		/*
		mListView = (ListView)messageLayout.findViewById(R.id.listview_message);
		mMsgAdapter = new MessageAdapter(mMsgBean, mMainActivity);
		mListView.setAdapter(mMsgAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(mMainActivity, mMsgBean.get(position).toString(),
						Toast.LENGTH_SHORT).show();
			}

		});
		
		*/
		return messageLayout;
	}
	
	private void initView() {
		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);

		InitImageView();
		InitTextView();
		InitViewPager();
	}
	

	/**
	 * 初始化Viewpager页
	 */
	private void InitViewPager() {
		viewPager = (ViewPager) messageLayout.findViewById(R.id.vPager);
		fragments = new ArrayList<Fragment>();
		fragments.add(new MessageContactsFragment());
		fragments.add(new MessageTalkFragment());
		fragments.add(new MessageHistoryFragment());
		viewPager.setAdapter(new myPagerAdapter(this.getChildFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	

	/**
	 * 初始化头标
	 * 
	 */
	private void InitTextView() {
		contactsTextView = (TextView) messageLayout.findViewById(R.id.tab_1);
		talkTextView = (TextView) messageLayout.findViewById(R.id.tab_2);
		historyTextView = (TextView) messageLayout.findViewById(R.id.tab_3);

		contactsTextView.setTextColor(selectedColor);
		talkTextView.setTextColor(unSelectedColor);
		historyTextView.setTextColor(unSelectedColor);

		contactsTextView.setText("联系人");
		talkTextView.setText("讨论圈");
		historyTextView.setText("历史记录");

		contactsTextView.setOnClickListener(new MyOnClickListener(0));
		talkTextView.setOnClickListener(new MyOnClickListener(1));
		historyTextView.setOnClickListener(new MyOnClickListener(2));
	}
	

	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */

	private void InitImageView() {
		imageView = (ImageView) messageLayout.findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
													// = 偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}



	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.e(TAG, "onAttach-----");

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate------");
		mMsgBean.add(new MessageBean(R.drawable.ic_photo_1, "1", "11", "111"));
		mMsgBean.add(new MessageBean(R.drawable.ic_photo_2, "����", "����", "����"));
		mMsgBean.add(new MessageBean(R.drawable.ic_photo_3, "С��", "�Է�û?", "����"));
		mMsgBean.add(new MessageBean(R.drawable.ic_photo_4, "����", "�Է�û?", "����"));
		mMsgBean.add(new MessageBean(R.drawable.ic_photo_5, "Jack", "�Է�û?", "����"));
		mMsgBean.add(new MessageBean(R.drawable.ic_photo_6, "Jone", "�Է�û?", "����"));
		mMsgBean.add(new MessageBean(R.drawable.ic_photo_7, "Jone", "�Է�û?", "����"));
		mMsgBean.add(new MessageBean(R.drawable.ic_photo_8, "Jone", "�Է�û?", "����"));
		mMsgBean.add(new MessageBean(R.drawable.ic_photo_9, "Jone", "�Է�û?", "����"));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated-------");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Log.e(TAG, "onStart----->");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(TAG, "onresume---->");
		MainActivity.currFragTag = Constant.FRAGMENT_FLAG_MESSAGE;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e(TAG, "onpause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e(TAG, "onStop");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.e(TAG, "ondestoryView");
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e(TAG, "ondestory");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d(TAG, "onDetach------");

	}


	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {

			switch (index) {
			case 0:
				contactsTextView.setTextColor(selectedColor);
				talkTextView.setTextColor(unSelectedColor);
				historyTextView.setTextColor(unSelectedColor);
				break;
			case 1:
				talkTextView.setTextColor(selectedColor);
				contactsTextView.setTextColor(unSelectedColor);
				historyTextView.setTextColor(unSelectedColor);
				break;
			case 2:
				historyTextView.setTextColor(selectedColor);
				talkTextView.setTextColor(unSelectedColor);
				contactsTextView.setTextColor(unSelectedColor);
				break;
			}
			viewPager.setCurrentItem(index);
		}

	}

	/**
	 * 为选项卡绑定监听器
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int index) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			Animation animation = new TranslateAnimation(one * currIndex, one
					* index, 0, 0);// 显然这个比较简洁，只有一行代码。
			currIndex = index;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);

			switch (index) {
			case 0:
				contactsTextView.setTextColor(selectedColor);
				talkTextView.setTextColor(unSelectedColor);
				historyTextView.setTextColor(unSelectedColor);
				break;
			case 1:
				talkTextView.setTextColor(selectedColor);
				contactsTextView.setTextColor(unSelectedColor);
				historyTextView.setTextColor(unSelectedColor);
				break;
			case 2:
				historyTextView.setTextColor(selectedColor);
				talkTextView.setTextColor(unSelectedColor);
				contactsTextView.setTextColor(unSelectedColor);
				break;
			}
		}
	}
	
	
	/**
	 * 定义适配器
	 */
	class myPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragmentList;

		public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
			super(fm);
			this.fragmentList = fragmentList;
		}

		/**
		 * 得到每个页面
		 */
		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		/**
		 * 每个页面的title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		/**
		 * 页面的总个数
		 */
		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}

	

}
