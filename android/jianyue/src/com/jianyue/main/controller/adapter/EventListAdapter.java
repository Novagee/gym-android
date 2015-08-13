package com.jianyue.main.controller.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianyue.main.controller.ApplyEventActivity;
import com.jianyue.main.controller.MainActivity;
import com.jianyue.main.controller.R;
import com.jianyue.utils.ClassEvent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class EventListAdapter extends BaseAdapter {
	private List<ClassEvent> listItem = null;
	private Context mContext;
	private LayoutInflater mInflater;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options;
	public EventListAdapter(List<ClassEvent> listItem, Context context){
		this.listItem = listItem;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.color.main_bg_color)
		.showImageForEmptyUri(R.color.main_bg_color).cacheInMemory()
		.cacheOnDisc().build();
		imageLoader = ImageLoader.getInstance();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = mInflater.inflate(R.layout.event_item_layout, null);
		
		
		ImageView imageView = (ImageView) v.findViewById(R.id.img_msg_item);
		
		imageLoader.displayImage(
				listItem.get(position).getImageUrl(), imageView,
				options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(
							String imageUri, View view) {
					//	pb.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(
							String imageUri, View view,
							FailReason failReason) {
					//	pb.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(
							String imageUri, View view,
							Bitmap loadedImage) {
					//	pb.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingCancelled(
							String imageUri, View view) {
					//	pb.setVisibility(View.GONE);
					}
				});
		
		//imageView.setImageResource(listItem.get(position).getPhotoDrawableId());
		
		TextView nameMsg = (TextView)v.findViewById(R.id.event_title_item);
		nameMsg.setText(listItem.get(position).getEventTitle());

		TextView contentMsg = (TextView)v.findViewById(R.id.event_content_item);
		contentMsg.setText(listItem.get(position).getEventContent());
		
		TextView timeMsg = (TextView)v.findViewById(R.id.time_msg_item);
		timeMsg.setText(listItem.get(position).getEventId());
		timeMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(mContext,
						ApplyEventActivity.class);
				i.putExtra("reply_mode", false);
				mContext.startActivity(i);
			//	overridePendingTransition(R.anim.slide_up, R.anim.slide_up_out);
		    
			}
			});

		return v;
	}

}
