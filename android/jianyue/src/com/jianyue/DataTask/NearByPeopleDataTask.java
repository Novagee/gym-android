package com.jianyue.DataTask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jianyue.utils.ClassAPIResponse;
import com.jianyue.utils.ClassUserDetail;
import com.jianyue.utils.GlobalData;
import com.jianyue.utils.SessionManager;
import com.jianyue.webservices.CallWebService;
import com.jianyue.webservices.WebElements;

public class NearByPeopleDataTask extends AsyncTask<String, Void, String>
{
	private Context context;
	private ClassAPIResponse apiResponse;
	private String append_url;
	private ProgressDialog mDialog;
	private String page_size , page_no;
	private ArrayList<ClassUserDetail> nearyby_users;
	private boolean is_refresh = false;

	public NearByPeopleDataTask(Context context, ClassAPIResponse apiResponse , String append_url , String page_no , String page_size , ArrayList<ClassUserDetail> nearyby_users , boolean is_refresh)
	{
		this.context = context;
		this.apiResponse = apiResponse;
		this.append_url = append_url;
		this.page_no = page_no;
		this.page_size = page_size;
		this.nearyby_users = nearyby_users;
		this.is_refresh = is_refresh;
	}

	protected void onPreExecute()
	{
		if(!is_refresh)
		{
			mDialog = new ProgressDialog(context);
			mDialog.setMessage("获取附近人员...  ");
			mDialog.setCancelable(false);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.show();
		}
	}

	protected String doInBackground(String... params)
	{
		try
		{
			ClassUserDetail obj = SessionManager.getObject(context);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.UUID, obj.uuid));
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.LAT, "22.959991"));
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.LNG, "72.909516"));
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.INTERESTIN, obj.interestIn));
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.PAGENUMBER, page_no));
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.PAGESIZE, page_size));
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.TIMESTAMP, String.valueOf(System.currentTimeMillis())));
//			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.UUID, "0bbc4f9a-da53-478c-a930-b1faaf4abd44"));
//			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.LAT, "22.959991"));
//			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.LNG, "72.909516"));
//			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.INTERESTIN, "F"));
//			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.PAGENUMBER, "1"));
//			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.PAGESIZE, "1"));
//			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.TIMESTAMP, String.valueOf(System.currentTimeMillis())));
			String result = CallWebService.Webserice_Call_Json(nameValuePairs , append_url);
			Log.d("result", result);

			if (result != null)
			{
				parceJsonResponse(result);
			}
			return GlobalData.SUCCESS;
		} catch (Exception e)
		{
			e.printStackTrace();
			return GlobalData.FAIL;
		}
	}

	private void parceJsonResponse(String result)
	{
		try {
			JSONObject j_result = new JSONObject(result);
			apiResponse.ack = j_result.getString("ack");
			if(apiResponse.ack.equalsIgnoreCase("Success"))
			{
				JSONArray j_list = j_result.getJSONArray("list");
				for(int i = 0 ; i < j_list.length() ; i++)
				{
					JSONObject j_user = j_list.getJSONObject(i);
					ClassUserDetail obj = new ClassUserDetail();
					obj.id = j_user.getString("id");
					obj.name = j_user.getString("name");
					obj.deviceToken = j_user.getString("deviceToken");
					obj.gender = j_user.getString("gender");
					obj.interestIn = j_user.getString("interestIn");
					obj.pic = j_user.getString("pic");
					obj.picHeight = j_user.getString("picHeight");
					obj.picWidth = j_user.getString("picWidth");
					obj.uuid = j_user.getString("uuid");
					nearyby_users.add(obj);
				}
			}
			else
			{
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onPostExecute(String result)
	{
		if(!is_refresh)
		{
			try
			{
				mDialog.dismiss();
			} catch (Exception e)
			{
	
			}
		}
	}

}
