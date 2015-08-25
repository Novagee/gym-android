package com.novagee.aidong.adapter;

import java.util.ArrayList;
import java.util.List;

import com.novagee.aidong.activity.ApplyEventActivity;
import com.novagee.aidong.controller.FetchEventListDataTask;
import com.novagee.aidong.imageloader.ImageLoader;
import com.novagee.aidong.model.Event;
import com.novagee.aidong.utils.Constant;
import com.novagee.aidong.utils.InternetChecker;
import com.novagee.aidong.utils.webservices.APIResponse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.novagee.aidong.R;



public class EventListAdapter extends BaseAdapter {
	private List<Event> mDataList = null;
	private Context ct;


	public EventListAdapter(Context context){
		this.ct = context;
		this.mDataList = new ArrayList<Event>();
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(ct);
		View v = layoutInflater.inflate(R.layout.view_event_list_item, null);
		
		ImageView imageView = (ImageView) v.findViewById(R.id.event_pic);
		String pic = mDataList.get(position).getPic();
		if(!(pic == null || "".equals(pic))){
			System.out.println("event pic:"+pic);
			ImageLoader.getInstance(ct).DisplayImage(pic, imageView, null, false);
		}
			
		TextView tvEventTitle = (TextView)v.findViewById(R.id.event_title);
		tvEventTitle.setText(mDataList.get(position).getTitle());

		TextView tvEventDescription = (TextView)v.findViewById(R.id.event_description);
		tvEventDescription.setText(mDataList.get(position).getDescription());
		
		TextView eventBook = (TextView)v.findViewById(R.id.event_book);
		eventBook.setOnClickListener(getOnClickListener(mDataList.get(position)));

		return v;
	}
	
	private OnClickListener getOnClickListener(final Event event){
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(ct,
						ApplyEventActivity.class);
				i.putExtra("id", event.getId());
				i.putExtra("title", event.getTitle());
				i.putExtra("starttime", event.getStarttime());
				i.putExtra("endtime", event.getEndtime());
				i.putExtra("address", event.getAddress());
				i.putExtra("description", event.getDescription());
				i.putExtra("pic", event.getPic());
				i.putExtra("fee", Integer.toString(event.getFee()));
				ct.startActivity(i);
				
			//	overridePendingTransition(R.anim.slide_up, R.anim.slide_up_out);
		    
			}
		};
		
	}
	
	public void fillData() {
		final APIResponse apiResponse = new APIResponse();
		final Context context = ct;
		mDataList.clear();
		FetchEventListDataTask task = new FetchEventListDataTask(ct,
				apiResponse, "r/event/", mDataList, false) {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (result.equals(Constant.FAIL)) {
					if (!InternetChecker.checkInternetConnection(context)) {
						Toast.makeText(context, context.getString(R.string.event_no_internet),Toast.LENGTH_LONG).show();
					}
					return;
				}
				if (apiResponse.ack.equalsIgnoreCase("Success")) {
					notifyDataSetChanged();
				} else {
					Toast.makeText(context, context.getString(R.string.event_get_data_failed),Toast.LENGTH_LONG).show();
				}
			}
		};

		if (InternetChecker.checkInternetConnection(context)) {
			try {
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context, context.getString(R.string.event_no_internet),Toast.LENGTH_LONG).show();
		}
	}

}
