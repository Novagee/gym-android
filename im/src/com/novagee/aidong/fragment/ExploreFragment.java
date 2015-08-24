package com.novagee.aidong.fragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arrownock.exception.ArrownockException;
import com.novagee.aidong.activity.CreatePostActivity;
import com.novagee.aidong.activity.WallActivity;
import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.controller.WallManager;
import com.novagee.aidong.controller.UserManager.FetchUserCallback;
import com.novagee.aidong.controller.WallManager.LikeCallback;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.im.model.Message;
import com.novagee.aidong.model.Post;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.DBug;
import com.novagee.aidong.view.BadgeView;
import com.novagee.aidong.view.WallView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.novagee.aidong.R;

public class ExploreFragment extends BaseFragment {
	private WallView mWallView;
	private FrameLayout header;
	private ImageView addBtn;
	private Context ct;
	
	public ExploreFragment(String title) {
		super(title);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        this.ct = getActivity();
        mWallView = (WallView)rootView.findViewById(R.id.wall_wallView);
        addBtn = (ImageView)rootView.findViewById(R.id.wall_addBtn);
        addBtn.setVisibility(View.GONE);
        addBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),CreatePostActivity.class);
				startActivityForResult(intent, 0);
				getActivity().overridePendingTransition(R.anim.slide_in_right,android.R.anim.fade_out);
			}
		});
        initData();
        return rootView;
	}
	
	private void initData(){
		User me = UserManager.getInstance(ct).getCurrentUser();
		UserManager.getInstance(ct).getMyLocalFriends(new FetchUserCallback(){
			@Override
			public void onFinish(List<User> data) {
				Set<String> friendSet = new HashSet<String>();
				for(User friend : data){
					friendSet.add(friend.userId);
				}
				String wallId = getString(R.string.wall_id);
				
				WallManager mWallManager = new WallManager(ct,wallId,friendSet);
				mWallManager.setOnLikeListener(new LikeCallback(){
					public void onFailure(Post post) {}
					@Override
					public void onSuccess(Post post) {
						try {
							Map<String,String> cData = new HashMap<String,String>();
							cData.put(Constant.FRIEND_REQUEST_KEY_TYPE, Message.TYPE_LIKE);
							cData.put("notification_alert", UserManager.getInstance(ct).getCurrentUser().userName+" 對你的貼文按讚");
							IMManager.getInstance(ct).getAnIM().sendBinary(post.owner.clientId, new byte[1], Constant.FRIEND_REQUEST_TYPE_SEND, cData);
						} catch (ArrownockException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				mWallView.setWallManager(mWallManager);
			}
		});
	}
	
}
