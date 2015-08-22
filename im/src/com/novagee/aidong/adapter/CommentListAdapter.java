package com.novagee.aidong.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.novagee.aidong.activity.CommentActivity;
import com.novagee.aidong.activity.PictureActivity;
import com.novagee.aidong.activity.UserDetailActivity;
import com.novagee.aidong.controller.UserManager;
import com.novagee.aidong.controller.WallManager;
import com.novagee.aidong.controller.WallManager.LikeCallback;
import com.novagee.aidong.im.controller.IMManager;
import com.novagee.aidong.im.controller.IMManager.GetMessageCallback;
import com.novagee.aidong.im.model.Chat;
import com.novagee.aidong.im.model.ChatUser;
import com.novagee.aidong.im.model.Message;
import com.novagee.aidong.im.view.MessageListItem;
import com.novagee.aidong.imageloader.ImageLoader;
import com.novagee.aidong.model.Comment;
import com.novagee.aidong.model.Like;
import com.novagee.aidong.model.Post;
import com.novagee.aidong.model.User;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.DBug;
import com.novagee.aidong.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import com.novagee.aidong.R;

public class CommentListAdapter extends BaseAdapter {
	private Context ct;
	private List<Comment> commentList;
	
	public CommentListAdapter(Context ct){
		this.ct = ct;
		commentList = new ArrayList<Comment>();
	}
	
	public void applyData(List<Comment> msgs){
		commentList.clear();
		commentList.addAll(msgs);
		
		notifyDataSetChanged();
	}
	
	public void addComment(Comment comment){
		commentList.add(comment);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return commentList.size();
	}

	@Override
	public Comment getItem(int position) {
		return commentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentListItem view = (CommentListItem) convertView;
		if (convertView == null) {
			view = new CommentListItem(parent.getContext());
		}
		view.setData(getItem(position));
		
		return view;
	}
	
	public class CommentListItem extends RelativeLayout{
		private ImageView imgUserIcon;
		private TextView textUserName,textContent;
		public CommentListItem(Context context) {
			super(context);
	        inflate(getContext(), R.layout.view_comment_list_item, this);
	        imgUserIcon = (ImageView) findViewById(R.id.comment_list_item_icon);
	        textUserName = (TextView) findViewById(R.id.comment_list_item_name);
	        textContent = (TextView) findViewById(R.id.comment_list_item_content);
		}
		
		public void setData(final Comment data){
			String strUserName = "";
			if(data.owner!=null){
				ImageLoader.getInstance(getContext()).DisplayImage(data.owner.userPhotoUrl, imgUserIcon, R.drawable.friend_default, true);
				strUserName += data.owner.userName;
			}else{
				imgUserIcon.setImageResource(R.drawable.friend_default);
			}
			if(data.replyUser!=null && !data.replyUser.userId.equals(data.post.owner.userId)){
				strUserName += " "+getContext().getString(R.string.wall_comment_reply)+" "+data.replyUser.userName;
			}
			
			textUserName.setText(strUserName);
			textContent.setText(data.content);
			imgUserIcon.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent i = new Intent(v.getContext(),UserDetailActivity.class);
					i.putExtra(Constant.INTENT_EXTRA_KEY_CLIENT, data.owner.clientId);
					getContext().startActivity(i);
					((Activity) getContext()).overridePendingTransition(R.anim.push_up_in,android.R.anim.fade_out);
				}
			});
		}
	}
	
}
