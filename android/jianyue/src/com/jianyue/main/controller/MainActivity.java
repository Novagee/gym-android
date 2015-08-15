package com.jianyue.main.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jianyue.DataTask.CommonDataTask;
import com.jianyue.DataTask.EditProfileDataTask;
import com.jianyue.DataTask.FetchChatDataTask;
import com.jianyue.DataTask.FetchEventListDataTask;
import com.jianyue.DataTask.FetchFriendDataTask;
import com.jianyue.DataTask.NearByPeopleDataTask;
import com.jianyue.main.controller.adapter.EventListAdapter;
import com.jianyue.utils.ClassAPIResponse;
import com.jianyue.utils.ClassChat;
import com.jianyue.utils.ClassEvent;
import com.jianyue.utils.ClassFriend;
import com.jianyue.utils.ClassUserDetail;
import com.jianyue.utils.GlobalData;
import com.jianyue.utils.Internet_Check;
import com.jianyue.utils.SessionManager;
import com.jianyue.utils.StaticMethodsUtility;
import com.jianyue.webservices.WebElements;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class MainActivity extends Activity {

	ScrollView scrollImages;
	PullToRefreshScrollView mPullRefreshScrollView;
	LinearLayout llImages;

	ScrollView scrollMessage;
	PullToRefreshScrollView mPullRefreshScrollViewMessage;
	LinearLayout llMessage;

	ScrollView scrollFriend;
	PullToRefreshScrollView mPullRefreshScrollViewFriend;
	LinearLayout llFriend;

	int postion = 0, width, height, img_id = 0, image_no, img_message_id = 1,
			img_friend_id = 1, postion_message = 0, postion_friend = 0;
	int page_no = 1, page_size = 5;
	int page_no_chat = 1, page_size_chat = 5;
	int page_no_friend = 1, page_size_friend = 5;

	boolean is_refresh = false;
	boolean is_refresh_chat = false;
	boolean is_reshesh_friend = false;
	boolean is_refresh_event = false;

	float y, after_scrool_y;
	ImageView ivCamera, ivComposeMessage, ivGender, ivHomeMessage,
			ivPrefCamera, ivPrefFriendList, ivOk , ivShareToWeChat;
	ImageView ivMessageFriendList, ivMessageHome, ivFavSetting, ivFavMessage,
			ivAddFriend , ivNearByTutorial , ivFriendTutorial , ivMessageTutorial;
	LinearLayout llNearByTutorial , llMessageTutorial , llFriendTutorial;
	TextView tvMessageCount, tvMessageBadge, tvFriendCount;
	SharedPreferences pref;
	Bitmap current_photo = null;
	String file_path, insterestedIn = "";

	ArrayList<String> f = new ArrayList<String>();// list of file paths
	File[] listFile;
	ViewPager pager;
	View v;
	// public static Bitmap bmp;
	// public static boolean is_Add = false;
	Button btnMale, btnFemale, btnOther;

	ArrayList<ClassUserDetail> near_by_users = new ArrayList<ClassUserDetail>();
	ArrayList<ClassChat> message_list = new ArrayList<ClassChat>();
	ArrayList<ClassFriend> friends_list = new ArrayList<ClassFriend>();
	ArrayList<String> user_ids = new ArrayList<String>();
	ArrayList<String> chat_user_senderids = new ArrayList<String>();
	ArrayList<String> message_ids = new ArrayList<String>();

	ArrayList<String> friend_ids = new ArrayList<String>();

	private DisplayImageOptions options;
	protected ImageLoader imageLoader;
	
	/**
	 * 活动内容开始
	 */
	private ListView event_list_view;
	private ArrayList<ClassEvent> events_list = new ArrayList<ClassEvent>();
	private EventListAdapter event_list_adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ClassUserDetail obj = SessionManager.getObject(MainActivity.this);
		Log.d("UUID", obj.id + "  " + SessionManager.getUUID(MainActivity.this)
				+ " " + System.currentTimeMillis());
		try {
			Crittercism.initialize(getApplicationContext(),
					"53b6b541b573f11a8c000002");
		} catch (Exception e) {

		}
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.color.main_bg_color)
				.showImageForEmptyUri(R.color.main_bg_color).cacheInMemory()
				.cacheOnDisc().build();
		imageLoader = ImageLoader.getInstance();
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		width = metrics.widthPixels;
		height = metrics.heightPixels - 42;

		requestNearByPeopleWebservice();
		pager = (ViewPager) findViewById(R.id.viewPager);
		pager.setOffscreenPageLimit(4);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int page_no) {
				if (page_no == 1) {
					requestFetchEventListWebservice();
				} else if (page_no == 2) {
					reloadChat();
				} else if (page_no == 3) {
					requestFetchChatWebservice(true);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		pager.setAdapter(new ViewPagerAdapter());
		pager.setCurrentItem(3);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void getFromSdcard() {
		File file = new File(
				android.os.Environment.getExternalStorageDirectory(), "Demo");

		if (file.isDirectory()) {
			listFile = file.listFiles();

			for (int i = 0; i < listFile.length; i++) {
				f.add(listFile[i].getAbsolutePath());
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("req code", "" + requestCode);
		Log.d("resultCode ", "" + resultCode);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case 1001:

				File folder = null;
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					folder = new File(
							Environment.getExternalStorageDirectory(), "/Demo/");
					if (!folder.isDirectory()) {
						folder.mkdir();
					}
				} else {
					folder = getDir("Demo", Context.MODE_WORLD_READABLE);
				}
				image_no = pref.getInt("Image_No", 1);
				File fileCamera = new File(folder, "Demo_" + image_no + ".png");
				image_no++;
				Editor edit = pref.edit();
				edit.putInt("Image_No", image_no);
				edit.commit();
				file_path = fileCamera.getPath();
				try {
					current_photo = BitmapFactory.decodeFile(fileCamera
							.getPath());
					current_photo = Bitmap.createScaledBitmap(current_photo,
							width, height, true);
					img_id++;
					ImageView image = new ImageView(MainActivity.this);
					image.setId(img_id);
					image.setImageBitmap(current_photo);
					image.setScaleType(ScaleType.FIT_XY);
					llImages.addView(image);

				} catch (Exception e) {

				}

				break;
			}
		}
	};

	public class ViewPagerAdapter extends PagerAdapter {
		// private DisplayImageOptions options_view_pager;

		public int getCount() {
			return 4;
		}

		public ViewPagerAdapter() {
			// options_view_pager = new
			// DisplayImageOptions.Builder().showStubImage(R.drawable.big_default_image).showImageForEmptyUri(R.drawable.big_default_image)
			// .showImageOnFail(R.drawable.big_default_image).cacheInMemory(true).cacheOnDisc(true).build();
		}

		public View instantiateItem(View collection, int position) {
			v = null;

			switch (position) {
			case 0:
				v = MainActivity.this.getLayoutInflater().inflate(
						R.layout.pref_screen, null);
				btnMale = (Button) v.findViewById(R.id.btnMale);
				btnFemale = (Button) v.findViewById(R.id.btnFemale);
				btnOther = (Button) v.findViewById(R.id.btnOther);
				ivGender = (ImageView) v.findViewById(R.id.ivGender);
				ivPrefCamera = (ImageView) v.findViewById(R.id.ivPrefCamera);
				ivPrefFriendList = (ImageView) v
						.findViewById(R.id.ivPrefFriendList);
				ivOk = (ImageView) v.findViewById(R.id.ivOk);

				ivPrefFriendList.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pager.setCurrentItem(1);
					}
				});

				ivOk.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						requestEditProfileWebserviceMainActivity();
					}
				});

				ivPrefCamera.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this,
								TakePictureActivity.class);
						i.putExtra("is_first_time", false);
						MainActivity.this.startActivity(i);
						MainActivity.this.overridePendingTransition(
								R.anim.slide_up, R.anim.slide_up_out);
					}
				});

				ClassUserDetail obj = SessionManager
						.getObject(MainActivity.this);
				if (obj.interestIn.equalsIgnoreCase("M")) {
					insterestedIn = "M";
					ivGender.setBackgroundResource(R.drawable.gender_m);
				} else if (obj.interestIn.equalsIgnoreCase("F")) {
					insterestedIn = "F";
					ivGender.setBackgroundResource(R.drawable.gender_f);
				} else {
					insterestedIn = "O";
					ivGender.setBackgroundResource(R.drawable.gender_o);
				}

				btnMale.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						insterestedIn = "M";
						ivGender.setBackgroundResource(R.drawable.gender_m);
					}
				});

				btnFemale.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						insterestedIn = "F";
						ivGender.setBackgroundResource(R.drawable.gender_f);
					}
				});

				btnOther.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						insterestedIn = "O";
						ivGender.setBackgroundResource(R.drawable.gender_o);
					}
				});
				break;
			case 1:
				
				v = MainActivity.this.getLayoutInflater().inflate(
						R.layout.event_list, null);
				event_list_view = (ListView)v.findViewById(R.id.listview_event);
				event_list_adapter = new EventListAdapter(events_list, MainActivity.this);
				event_list_view.setAdapter(event_list_adapter);
				
				
				
				
				/*
				v = MainActivity.this.getLayoutInflater().inflate(
						R.layout.friends_list, null);

				mPullRefreshScrollViewFriend = (PullToRefreshScrollView) v
						.findViewById(R.id.scrollFriend);
				mPullRefreshScrollViewFriend
						.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

							@Override
							public void onRefresh(
									PullToRefreshBase<ScrollView> refreshView) {
								is_reshesh_friend = true;
								page_no_friend++;
								requestFetchFriendWebservice();
							}
						});

				scrollFriend = mPullRefreshScrollViewFriend
						.getRefreshableView();

				scrollFriend.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						switch (event.getAction()) {

						case MotionEvent.ACTION_DOWN:
							y = event.getY();
							break;
						case MotionEvent.ACTION_MOVE:
							view.onTouchEvent(event);
							break;
						case MotionEvent.ACTION_UP:
							after_scrool_y = event.getY();
							if (after_scrool_y - y > 20) {
								if (postion_friend > 0) {
									postion_friend--;
									new Handler().post(new Runnable() {
										@Override
										public void run() {
											scrollFriend.smoothScrollTo(0,
													postion_friend
															* (height));
										}
									});
								}
							} else if (y - after_scrool_y > 20) {
								if (postion_friend < img_friend_id - 1) {
									postion_friend++;
									new Handler().post(new Runnable() {
										@Override
										public void run() {
											scrollFriend.smoothScrollTo(0,
													postion_friend
															* (height));
										}
									});
								}
							}
							break;
						}
						return true;
					}
				});
				llFriend = (LinearLayout) v.findViewById(R.id.llFriend);

				ivFavSetting = (ImageView) v.findViewById(R.id.ivFavSetting);
				ivFavMessage = (ImageView) v.findViewById(R.id.ivFavMessage);
				
				ivFriendTutorial = (ImageView)v.findViewById(R.id.ivFriendTutorial);
				llFriendTutorial = (LinearLayout)v.findViewById(R.id.llFriendTutorial);
				if(!SessionManager.getFriendTuorial(MainActivity.this))
				{
					llFriendTutorial.setVisibility(View.VISIBLE);
				}
				llFriendTutorial.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						SessionManager.setFriendTutorial(MainActivity.this,true);
						llFriendTutorial.setVisibility(View.GONE);
					}
				});

				ivFavSetting.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pager.setCurrentItem(0);
					}
				});

				ivFavMessage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pager.setCurrentItem(2);
					}
				});

				View v1 = MainActivity.this.getLayoutInflater().inflate(
						R.layout.fridend_view, null);
				ImageView image = (ImageView) v1
						.findViewById(R.id.ivTempFriend);
				image.getLayoutParams().height = height;
				tvFriendCount = (TextView) v1.findViewById(R.id.tvFriendCount);
				ivAddFriend = (ImageView) v1.findViewById(R.id.ivAddFriend);

				ivAddFriend.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this,
								PhoneContactActivity.class);
						MainActivity.this.startActivity(i);
						MainActivity.this.overridePendingTransition(
								R.anim.slide_up, R.anim.slide_up_out);
					}
				});

				llFriend.addView(v1);
				*/

				break;
			case 2:
				v = MainActivity.this.getLayoutInflater().inflate(
						R.layout.message_list, null);
				mPullRefreshScrollViewMessage = (PullToRefreshScrollView) v
						.findViewById(R.id.scrollMessage);
				mPullRefreshScrollViewMessage
						.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

							@Override
							public void onRefresh(
									PullToRefreshBase<ScrollView> refreshView) {
								is_refresh_chat = true;
								page_no_chat++;
								requestFetchChatWebservice(false);
							}
						});

				scrollMessage = mPullRefreshScrollViewMessage
						.getRefreshableView();

				scrollMessage.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						switch (event.getAction()) {

						case MotionEvent.ACTION_DOWN:
							y = event.getY();
							break;
						case MotionEvent.ACTION_MOVE:
							view.onTouchEvent(event);
							break;
						case MotionEvent.ACTION_UP:
							after_scrool_y = event.getY();
							if (after_scrool_y - y > 20) {
								if (postion_message > 0) {
									postion_message--;
									new Handler().post(new Runnable() {
										@Override
										public void run() {
											scrollMessage.smoothScrollTo(0,
													postion_message
															* (height));
										}
									});
								}
							} else if (y - after_scrool_y > 20) {
								if (postion_message < img_message_id - 1) {
									postion_message++;
									new Handler().post(new Runnable() {
										@Override
										public void run() {
											scrollMessage.smoothScrollTo(0,
													postion_message
															* (height));
										}
									});
								}
							}
							break;
						}
						return true;
					}
				});
				llMessage = (LinearLayout) v.findViewById(R.id.llMessage);

				ivMessageFriendList = (ImageView) v
						.findViewById(R.id.ivMessageFriendList);
				ivMessageHome = (ImageView) v.findViewById(R.id.ivMessageHome);
				
				ivMessageTutorial = (ImageView)v.findViewById(R.id.ivMessageTutorial);
				llMessageTutorial = (LinearLayout)v.findViewById(R.id.llMessageTutorial);
				
				if(!SessionManager.getMessageTuorial(MainActivity.this))
				{
					llMessageTutorial.setVisibility(View.VISIBLE);
				}
				llMessageTutorial.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						SessionManager.setMessageTutorial(MainActivity.this,true);
						llMessageTutorial.setVisibility(View.GONE);
					}
				});

				ivMessageFriendList.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pager.setCurrentItem(1);
					}
				});
				
				//add by seeyet,2015/8/11，隐藏朋友圈
				ivMessageFriendList.setVisibility(TRIM_MEMORY_UI_HIDDEN);

				ivMessageHome.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pager.setCurrentItem(3);
					}
				});

				View v_1 = MainActivity.this.getLayoutInflater().inflate(
						R.layout.message_view, null);
				ImageView image_1 = (ImageView) v_1.findViewById(R.id.ivTemp);
				image_1.getLayoutParams().height = height;
				tvMessageCount = (TextView) v_1
						.findViewById(R.id.tvMessageCount);
				llMessage.addView(v_1);

				break;
			case 3:
				v = MainActivity.this.getLayoutInflater().inflate(
						R.layout.nearby_people, null);
				ivNearByTutorial = (ImageView)v.findViewById(R.id.ivNearByTutorial);
				llImages = (LinearLayout) v.findViewById(R.id.llImages);
				ivHomeMessage = (ImageView) v.findViewById(R.id.ivHomeMessage);
				tvMessageBadge = (TextView) v.findViewById(R.id.tvMessageBadge);
				ivShareToWeChat = (ImageView)v.findViewById(R.id.ivShareToWeChat);
				
				llNearByTutorial = (LinearLayout)v.findViewById(R.id.llNearByTutorial);
				
				if(!SessionManager.getNearByTuorial(MainActivity.this))
				{
					llNearByTutorial.setVisibility(View.VISIBLE);
				}
				
				llNearByTutorial.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						SessionManager.setNearByTutorial(MainActivity.this,true);
						llNearByTutorial.setVisibility(View.GONE);
					}
				});
				
				ivShareToWeChat.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						shareToWechat();
					}
				});

				ivHomeMessage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pager.setCurrentItem(2);
					}
				});

				ivComposeMessage = (ImageView) v
						.findViewById(R.id.ivComposeMessage);
				ivComposeMessage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this,
								SendMessageActivity.class);
						i.putExtra("receiver", user_ids.get(postion));
						i.putExtra("reply_mode", false);
						MainActivity.this.startActivity(i);
						MainActivity.this.overridePendingTransition(
								R.anim.slide_up, R.anim.slide_up_out);
					}
				});

				ivCamera = (ImageView) v.findViewById(R.id.ivCamera);
				ivCamera.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// Intent intentCamera = new
						// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						//
						// File folder = null;
						// if
						// (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
						// {
						// folder = new
						// File(Environment.getExternalStorageDirectory(),
						// "/Demo/");
						// if (!folder.isDirectory()) {
						// folder.mkdir();
						// }
						// } else {
						// folder = MainActivity.this.getDir("Demo",
						// Context.MODE_WORLD_READABLE);
						// }
						// image_no = pref.getInt("Image_No", 1);
						// File fileCamera = new File(folder, "Demo_" + image_no
						// + ".png");
						// Uri uri = Uri.fromFile(fileCamera);
						// intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
						// intentCamera.putExtra("return-data", true);
						// MainActivity.this.startActivityForResult(intentCamera,1001);
						Intent i = new Intent(MainActivity.this,
								TakePictureActivity.class);
						i.putExtra("is_first_time", false);
						MainActivity.this.startActivity(i);
						MainActivity.this.overridePendingTransition(
								R.anim.slide_up, R.anim.slide_up_out);
					}
				});

				/*
				 * getFromSdcard();
				 * 
				 * if (f.size() > 0) { for (int i = 0; i < f.size(); i++) { try
				 * {
				 * 
				 * if (current_photo != null) { current_photo.recycle();
				 * current_photo = null; }
				 * 
				 * current_photo = BitmapFactory.decodeFile(f.get(i));
				 * current_photo = Bitmap.createScaledBitmap( current_photo,
				 * width, height, true); img_id++; ImageView image = new
				 * ImageView(MainActivity.this); image.setId(img_id);
				 * image.setImageBitmap(current_photo);
				 * image.setScaleType(ScaleType.FIT_XY);
				 * llImages.addView(image);
				 * 
				 * } catch (Exception e) {
				 * 
				 * } } }
				 */
				mPullRefreshScrollView = (PullToRefreshScrollView) v
						.findViewById(R.id.scrollImages);
				mPullRefreshScrollView
						.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

							@Override
							public void onRefresh(
									PullToRefreshBase<ScrollView> refreshView) {
								is_refresh = true;
								page_no++;
								requestNearByPeopleWebservice();
							}
						});

				scrollImages = mPullRefreshScrollView.getRefreshableView();
				// scrollImages = (ScrollView)
				// v.findViewById(R.id.scrollImages);
				scrollImages.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						switch (event.getAction()) {

						case MotionEvent.ACTION_DOWN:
							y = event.getY();
							break;
						case MotionEvent.ACTION_MOVE:
							view.onTouchEvent(event);
							break;
						case MotionEvent.ACTION_UP:
							after_scrool_y = event.getY();
							if (after_scrool_y - y > 20) {
								if (postion > 0) {
									postion--;
									new Handler().post(new Runnable() {
										@Override
										public void run() {
											scrollImages.smoothScrollTo(0,
													postion * height);
										}
									});
								}
							} else if (y - after_scrool_y > 20) {
								if (postion < img_id - 1) {
									postion++;
									new Handler().post(new Runnable() {
										@Override
										public void run() {
											scrollImages.smoothScrollTo(0,
													postion * height);
										}
									});
								}
							}
							break;
						}
						return true;
					}
				});
				break;
			}

			((ViewPager) collection).addView(v, 0);
			return v;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void finishUpdate(View arg0) {
			Log.d("Tag", "FinishUpdate");
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			Log.d("Tag", "restoreState");
		}

		@Override
		public void startUpdate(View arg0) {
			Log.d("Tag", "startUpdate");
		}

	}

	private void requestNearByPeopleWebservice() {

		final ClassAPIResponse apiResponse = new ClassAPIResponse();

		NearByPeopleDataTask task = new NearByPeopleDataTask(MainActivity.this,
				apiResponse, "r/user/profile", String.valueOf(page_no),
				String.valueOf(page_size), near_by_users, is_refresh) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (is_refresh) {
					is_refresh = false;
					mPullRefreshScrollView.onRefreshComplete();
				}

				if (result.equals(GlobalData.FAIL)) {
					if (!Internet_Check
							.checkInternetConnection(MainActivity.this)) {
						StaticMethodsUtility.showNegativeToast(
								MainActivity.this,
								getResources().getString(R.string.no_internet));
					}
					return;
				}

				if (apiResponse.ack.equalsIgnoreCase("Success")) {
					for (int i = 0; i < near_by_users.size(); i++) {
						user_ids.add(near_by_users.get(i).id);
						img_id++;
						View v = MainActivity.this.getLayoutInflater().inflate(
								R.layout.nearby_people_image, null);
						ImageView image = (ImageView) v
								.findViewById(R.id.ivImage);
						image.getLayoutParams().height = height;
						image.getLayoutParams().width = width;
						// image.setImageResource(R.drawable.ic_launcher);
						final ProgressBar pb = (ProgressBar) v
								.findViewById(R.id.pbNearBy);
						// image.setId(img_id);
						image.setScaleType(ScaleType.CENTER_CROP);
						Log.d("Pic", near_by_users.get(i).pic);
						imageLoader.displayImage(near_by_users.get(i).pic,
								image, options, new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(
											String imageUri, View view) {
										pb.setVisibility(View.VISIBLE);
									}

									@Override
									public void onLoadingFailed(
											String imageUri, View view,
											FailReason failReason) {
										pb.setVisibility(View.GONE);
									}

									@Override
									public void onLoadingComplete(
											String imageUri, View view,
											Bitmap loadedImage) {
										pb.setVisibility(View.GONE);
									}

									@Override
									public void onLoadingCancelled(
											String imageUri, View view) {
										pb.setVisibility(View.GONE);
									}
								});
						llImages.addView(v);
					}
					requestFetchChatWebservice(true);
				} else {
					StaticMethodsUtility.showNegativeToast(MainActivity.this,
							"Something went wrong");
				}
			}
		};

		if (Internet_Check.checkInternetConnection(MainActivity.this)) {
			try {
				near_by_users.clear();
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			StaticMethodsUtility.showNegativeToast(MainActivity.this,
					getResources().getString(R.string.no_internet));
		}
	}

	private void requestFetchChatWebservice(final boolean is_badge_display) {

		final ClassAPIResponse apiResponse = new ClassAPIResponse();
		message_list.clear();
		FetchChatDataTask task = new FetchChatDataTask(MainActivity.this,
				apiResponse, "r/message/", String.valueOf(page_no_chat),
				String.valueOf(page_size_chat), message_list, is_refresh_chat,
				is_badge_display) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (is_refresh_chat) {
					is_refresh_chat = false;
					mPullRefreshScrollViewMessage.onRefreshComplete();
				}

				if (result.equals(GlobalData.FAIL)) {
					if (!Internet_Check
							.checkInternetConnection(MainActivity.this)) {
						StaticMethodsUtility.showNegativeToast(
								MainActivity.this,
								getResources().getString(R.string.no_internet));
					}
					return;
				}

				if (apiResponse.ack.equalsIgnoreCase("Success")) {

					if (is_badge_display) {
						if (apiResponse.count.equalsIgnoreCase("0")) {
							tvMessageBadge.setVisibility(View.INVISIBLE);
						} else {
							tvMessageBadge.setVisibility(View.VISIBLE);
							tvMessageBadge.setText(apiResponse.count);
						}
					} else {
						tvMessageCount.setText(apiResponse.count);
						for (int i = 0; i < message_list.size(); i++) {
							chat_user_senderids
									.add(message_list.get(i).senderId);
							message_ids.add(message_list.get(i).id);
							img_message_id++;
							View v = MainActivity.this.getLayoutInflater()
									.inflate(R.layout.message_image, null);
							ImageView image = (ImageView) v
									.findViewById(R.id.ivMessageImage);
							image.getLayoutParams().height = height ;
							image.getLayoutParams().width = width;
							// image.setImageResource(R.drawable.ic_launcher);
							final ProgressBar pb = (ProgressBar) v
									.findViewById(R.id.pbMessage);
							// image.setId(img_id);
							image.setScaleType(ScaleType.CENTER_CROP);
							// Log.d("Pic", near_by_users.get(i).pic);
							imageLoader.displayImage(
									message_list.get(i).senderPic, image,
									options, new ImageLoadingListener() {

										@Override
										public void onLoadingStarted(
												String imageUri, View view) {
											pb.setVisibility(View.VISIBLE);
										}

										@Override
										public void onLoadingFailed(
												String imageUri, View view,
												FailReason failReason) {
											pb.setVisibility(View.GONE);
										}

										@Override
										public void onLoadingComplete(
												String imageUri, View view,
												Bitmap loadedImage) {
											pb.setVisibility(View.GONE);
										}

										@Override
										public void onLoadingCancelled(
												String imageUri, View view) {
											pb.setVisibility(View.GONE);
										}
									});

							ImageView ivMessageClose = (ImageView) v
									.findViewById(R.id.ivMessageClose);
							ImageView ivMessageFriendRequest = (ImageView) v
									.findViewById(R.id.ivMessageFriendRequest);
							ImageView ivMessageReply = (ImageView) v
									.findViewById(R.id.ivMessageReply);
							ImageView ivMessageAcceptFriendRequest = (ImageView) v
									.findViewById(R.id.ivMessageAcceptFriendRequest);

							if (message_list.get(i).isFriend
									.equalsIgnoreCase("true")
									|| message_list.get(i).isFriend
											.equalsIgnoreCase("1")) {
								ivMessageFriendRequest.setVisibility(View.GONE);
								ivMessageAcceptFriendRequest
										.setVisibility(View.INVISIBLE);
							} else if (message_list.get(i).msgType
									.equalsIgnoreCase("friendRequest")
									&& message_list.get(i).msgStatus
											.equalsIgnoreCase("inrequest")) {
								ivMessageAcceptFriendRequest
										.setVisibility(View.VISIBLE);
								ivMessageFriendRequest.setVisibility(View.GONE);
							} else {
								ivMessageAcceptFriendRequest
										.setVisibility(View.GONE);
								ivMessageFriendRequest
										.setVisibility(View.VISIBLE);
							}

							ivMessageClose
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											requestDeleteMessageWebservice();
										}
									});

							ivMessageFriendRequest
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											requestAddFriendWebservice();
										}
									});

							ivMessageAcceptFriendRequest
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											requestAcceptFriendWebservice();
										}
									});

							ivMessageReply
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											Intent i = new Intent(
													MainActivity.this,
													SendMessageActivity.class);
											i.putExtra(
													"receiver",
													chat_user_senderids
															.get(postion_message - 1));
											i.putExtra("reply_mode", true);
											MainActivity.this.startActivity(i);
											MainActivity.this
													.overridePendingTransition(
															R.anim.slide_up,
															R.anim.slide_up_out);
										}
									});

							llMessage.addView(v);
						}
					}
				} else {
					StaticMethodsUtility.showNegativeToast(MainActivity.this,
							"Something went wrong");
				}
			}
		};

		if (Internet_Check.checkInternetConnection(MainActivity.this)) {
			try {
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (is_refresh_chat) {
				is_refresh_chat = false;
				mPullRefreshScrollViewMessage.onRefreshComplete();
			}
			StaticMethodsUtility.showNegativeToast(MainActivity.this,
					getResources().getString(R.string.no_internet));
		}
	}

	private void requestFetchFriendWebservice() {

		final ClassAPIResponse apiResponse = new ClassAPIResponse();
		friends_list.clear();
		FetchFriendDataTask task = new FetchFriendDataTask(MainActivity.this,
				apiResponse, "r/friend/", String.valueOf(page_no_friend),
				String.valueOf(page_size_friend), friends_list, is_reshesh_friend) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (is_reshesh_friend) {
					is_reshesh_friend = false;
					mPullRefreshScrollViewFriend.onRefreshComplete();
				}

				if (result.equals(GlobalData.FAIL)) {
					if (!Internet_Check
							.checkInternetConnection(MainActivity.this)) {
						StaticMethodsUtility.showNegativeToast(
								MainActivity.this,
								getResources().getString(R.string.no_internet));
					}
					return;
				}

				if (apiResponse.ack.equalsIgnoreCase("Success")) {

					tvFriendCount.setText(apiResponse.count);

					for (int i = 0; i < friends_list.size(); i++) {
						friend_ids.add(friends_list.get(i).friendId);
						img_friend_id++;
						View v = MainActivity.this.getLayoutInflater().inflate(
								R.layout.friend_image, null);
						ImageView image = (ImageView) v
								.findViewById(R.id.ivFriendImage);
						image.getLayoutParams().height = height;
						image.getLayoutParams().width = width; //
						image.setImageResource(R.drawable.ic_launcher);
						final ProgressBar pb = (ProgressBar) v
								.findViewById(R.id.pbFriend); // image.setId(img_id);
						image.setScaleType(ScaleType.CENTER_CROP); //
						//Log.d("Pic", near_by_users.get(i).pic);
						imageLoader.displayImage(friends_list.get(i).friendPic,
								image, options, new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(
											String imageUri, View view) {
										pb.setVisibility(View.VISIBLE);
									}

									@Override
									public void onLoadingFailed(
											String imageUri, View view,
											FailReason failReason) {
										pb.setVisibility(View.GONE);
									}

									@Override
									public void onLoadingComplete(
											String imageUri, View view,
											Bitmap loadedImage) {
										pb.setVisibility(View.GONE);
									}

									@Override
									public void onLoadingCancelled(
											String imageUri, View view) {
										pb.setVisibility(View.GONE);
									}
								});

						ImageView ivUnFriend = (ImageView) v
								.findViewById(R.id.ivUnFriend);
						ImageView ivFriendComposer = (ImageView) v
								.findViewById(R.id.ivFriendComposer);

						ivUnFriend
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										requestUnBindFriendWebservice();
									}
								});

						ivFriendComposer
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent i = new Intent(MainActivity.this,SendMessageActivity.class);
										i.putExtra("receiver",friend_ids.get(postion_friend - 1));
										i.putExtra("reply_mode", false);
										MainActivity.this.startActivity(i);
										MainActivity.this.overridePendingTransition(R.anim.slide_up,R.anim.slide_up_out);
									}
								});

						llFriend.addView(v);
					}
				} else {
					StaticMethodsUtility.showNegativeToast(MainActivity.this,
							"Something went wrong");
				}
			}
		};

		if (Internet_Check.checkInternetConnection(MainActivity.this)) {
			try {
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (is_reshesh_friend) {
				is_reshesh_friend = false;
				mPullRefreshScrollViewFriend.onRefreshComplete();
			}
			StaticMethodsUtility.showNegativeToast(MainActivity.this,
					getResources().getString(R.string.no_internet));
		}
	}

	private void requestEditProfileWebserviceMainActivity() {

		final ClassAPIResponse apiResponse = new ClassAPIResponse();

		EditProfileDataTask task = new EditProfileDataTask(MainActivity.this,
				apiResponse, "r/user/profile/edit", true) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (result.equals(GlobalData.FAIL)) {
					if (!Internet_Check
							.checkInternetConnection(MainActivity.this)) {
						StaticMethodsUtility.showNegativeToast(
								MainActivity.this,
								getResources().getString(R.string.no_internet));
					}
					return;
				}

				if (apiResponse.ack.equalsIgnoreCase("Success")) {
					pager.setCurrentItem(3);
					StaticMethodsUtility.showPositiveToast(MainActivity.this,
							"Profile updated successfully");
				}

			}
		};

		if (Internet_Check.checkInternetConnection(MainActivity.this)) {
			try {

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {

					ClassUserDetail obj = SessionManager
							.getObject(MainActivity.this);
					reqEntity.addPart(WebElements.SIGNUP.UUID, new StringBody(
							SessionManager.getUUID(MainActivity.this)));
					reqEntity.addPart(WebElements.SIGNUP.GENDER,
							new StringBody(obj.gender));
					reqEntity.addPart(WebElements.SIGNUP.INTERESTIN,
							new StringBody(insterestedIn));
					Log.d("LATLONG",
							"Location  "
									+ SessionManager
											.getLatitude(MainActivity.this)
									+ "  "
									+ SessionManager
											.getLongitude(MainActivity.this));
					if (SessionManager.getLatitude(MainActivity.this)
							.equalsIgnoreCase("")) {
						reqEntity.addPart(WebElements.SIGNUP.LAT,
								new StringBody("22.959991"));
						reqEntity.addPart(WebElements.SIGNUP.LNG,
								new StringBody("72.909516"));
					} else {
						reqEntity.addPart(
								WebElements.SIGNUP.LAT,
								new StringBody(SessionManager
										.getLatitude(MainActivity.this)));
						reqEntity.addPart(
								WebElements.SIGNUP.LNG,
								new StringBody(SessionManager
										.getLongitude(MainActivity.this)));
					}
					task.execute(reqEntity);
				} catch (Exception e) {

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			StaticMethodsUtility.showNegativeToast(MainActivity.this,
					getResources().getString(R.string.no_internet));
		}
	}

	private void requestAddFriendWebservice() {

		//Receiver
		final ClassAPIResponse apiResponse = new ClassAPIResponse();

		CommonDataTask task = new CommonDataTask(MainActivity.this,
				apiResponse, "r/message/sendFriendRequest",
				chat_user_senderids.get(postion_message - 1), 0, "Loading") {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (result.equals(GlobalData.FAIL)) {
					if (!Internet_Check
							.checkInternetConnection(MainActivity.this)) {
						StaticMethodsUtility.showNegativeToast(
								MainActivity.this,
								getResources().getString(R.string.no_internet));
					}
					return;
				}

				if (apiResponse.ack.equalsIgnoreCase("Success")) {
					StaticMethodsUtility.showPositiveToast(MainActivity.this,
							"Friend request sent successfully");
					reloadChat();
				} else {
					StaticMethodsUtility.showNegativeToast(MainActivity.this,
							"Something went wrong");
				}
			}
		};

		if (Internet_Check.checkInternetConnection(MainActivity.this)) {
			try {
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			StaticMethodsUtility.showNegativeToast(MainActivity.this,
					getResources().getString(R.string.no_internet));
		}
	}
	
	private void requestDeleteMessageWebservice() {

		//Message Id
		final ClassAPIResponse apiResponse = new ClassAPIResponse();

		CommonDataTask task = new CommonDataTask(MainActivity.this,
				apiResponse, "r/message/delete",
				message_ids.get(postion_message - 1), 1, "Loading") {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (result.equals(GlobalData.FAIL)) {
					if (!Internet_Check
							.checkInternetConnection(MainActivity.this)) {
						StaticMethodsUtility.showNegativeToast(
								MainActivity.this,
								getResources().getString(R.string.no_internet));
					}
					return;
				}

				if (apiResponse.ack.equalsIgnoreCase("Success")) {
					StaticMethodsUtility.showPositiveToast(MainActivity.this,
							"Message deleted successfully");
					reloadChat();
				} else {
					StaticMethodsUtility.showNegativeToast(MainActivity.this,
							"Something went wrong");
				}
			}
		};

		if (Internet_Check.checkInternetConnection(MainActivity.this)) {
			try {
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			StaticMethodsUtility.showNegativeToast(MainActivity.this,
					getResources().getString(R.string.no_internet));
		}
	}
	
	private void requestUnBindFriendWebservice() {

		//Message Id
		final ClassAPIResponse apiResponse = new ClassAPIResponse();

		CommonDataTask task = new CommonDataTask(MainActivity.this,
				apiResponse, "r/friend/unbind",
				friend_ids.get(postion_friend - 1), 2, "Loading") {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (result.equals(GlobalData.FAIL)) {
					if (!Internet_Check
							.checkInternetConnection(MainActivity.this)) {
						StaticMethodsUtility.showNegativeToast(
								MainActivity.this,
								getResources().getString(R.string.no_internet));
					}
					return;
				}

				if (apiResponse.ack.equalsIgnoreCase("Success")) {
					StaticMethodsUtility.showPositiveToast(MainActivity.this,
							"Friend removed successfully");
					reloadFriend();
				} else {
					StaticMethodsUtility.showNegativeToast(MainActivity.this,
							"Something went wrong");
				}
			}
		};

		if (Internet_Check.checkInternetConnection(MainActivity.this)) {
			try {
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			StaticMethodsUtility.showNegativeToast(MainActivity.this,
					getResources().getString(R.string.no_internet));
		}
	}


	private void requestAcceptFriendWebservice() {
		
		//Message Id
		
		final ClassAPIResponse apiResponse = new ClassAPIResponse();
   
		CommonDataTask task = new CommonDataTask(MainActivity.this,
				apiResponse, "r/message/acceptFriendRequest",
				message_ids.get(postion_message - 1), 1 ,"Loading") {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (result.equals(GlobalData.FAIL)) {
					if (!Internet_Check
							.checkInternetConnection(MainActivity.this)) {
						StaticMethodsUtility.showNegativeToast(
								MainActivity.this,
								getResources().getString(R.string.no_internet));
					}
					return;
				}

				if (apiResponse.ack.equalsIgnoreCase("Success")) {
					StaticMethodsUtility.showPositiveToast(MainActivity.this,
							"Friend request accepted");
					reloadChat();

				} else {
					StaticMethodsUtility.showNegativeToast(MainActivity.this,
							"Something went wrong");
				}
			}
		};

		if (Internet_Check.checkInternetConnection(MainActivity.this)) {
			try {
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			StaticMethodsUtility.showNegativeToast(MainActivity.this,
					getResources().getString(R.string.no_internet));
		}
	}
	
	
	private void requestFetchEventListWebservice() {

		final ClassAPIResponse apiResponse = new ClassAPIResponse();
		events_list.clear();
		FetchEventListDataTask task = new FetchEventListDataTask(MainActivity.this,
				apiResponse, "r/event/", events_list, is_refresh_event) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (is_refresh_event) {
					is_refresh_event = false;
				//	mPullRefreshScrollViewEvent.onRefreshComplete();
				}

				if (result.equals(GlobalData.FAIL)) {
					if (!Internet_Check
							.checkInternetConnection(MainActivity.this)) {
						StaticMethodsUtility.showNegativeToast(
								MainActivity.this,
								getResources().getString(R.string.no_internet));
					}
					return;
				}

				if (apiResponse.ack.equalsIgnoreCase("Success")) {
					event_list_adapter.notifyDataSetChanged();
				} else {
					StaticMethodsUtility.showNegativeToast(MainActivity.this,
							"Something went wrong");
				}
			}
		};

		if (Internet_Check.checkInternetConnection(MainActivity.this)) {
			try {
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (is_reshesh_friend) {
				is_reshesh_friend = false;
			//	mPullRefreshScrollViewEvent.onRefreshComplete();
			}
			StaticMethodsUtility.showNegativeToast(MainActivity.this,
					getResources().getString(R.string.no_internet));
		}
	}
	

	private void reloadChat() {
		page_no_chat = 1;
		postion_message = 0;
		img_message_id = 1;
		chat_user_senderids.clear();
		message_ids.clear();
		llMessage.removeAllViews();
		View v1 = MainActivity.this.getLayoutInflater().inflate(
				R.layout.message_view, null);
		ImageView image = (ImageView) v1.findViewById(R.id.ivTemp);
		image.getLayoutParams().height = height;
		tvMessageCount = (TextView) v1.findViewById(R.id.tvMessageCount);
		llMessage.addView(v1);
		requestFetchChatWebservice(false);
	}

	private void reloadFriend() {
		page_no_friend = 1;
		postion_friend = 0;
		img_friend_id = 1;
		friend_ids.clear();
		llFriend.removeAllViews();
		View v1 = MainActivity.this.getLayoutInflater().inflate(
				R.layout.fridend_view, null);
		ImageView image = (ImageView) v1.findViewById(R.id.ivTempFriend);
		image.getLayoutParams().height = height;
		tvFriendCount = (TextView) v1.findViewById(R.id.tvFriendCount);
		ivAddFriend = (ImageView) v1.findViewById(R.id.ivAddFriend);

		ivAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						PhoneContactActivity.class);
				MainActivity.this.startActivity(i);
				MainActivity.this.overridePendingTransition(R.anim.slide_up,
						R.anim.slide_up_out);
			}
		});
		llFriend.addView(v1);
		requestFetchFriendWebservice();
	}
	
	private void shareToWechat()
	{
		// Do We chat click code...
	}
}