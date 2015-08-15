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
import com.jianyue.utils.ClassChat;
import com.jianyue.utils.GlobalData;
import com.jianyue.utils.SessionManager;
import com.jianyue.webservices.CallWebService;
import com.jianyue.webservices.WebElements;

public class FetchChatDataTask extends AsyncTask<String, Void, String>
{
	private Context context;
	private ClassAPIResponse apiResponse;
	private String append_url;
	private ProgressDialog mDialog;
	private String page_size , page_no;
	private ArrayList<ClassChat> message_list;
	private boolean is_refresh = false;
	private boolean is_badge_display = false;

	public FetchChatDataTask(Context context, ClassAPIResponse apiResponse , String append_url , String page_no , String page_size , ArrayList<ClassChat> message_list , boolean is_refresh , boolean is_badge_display)
	{
		this.context = context;
		this.apiResponse = apiResponse;
		this.append_url = append_url;
		this.page_no = page_no;
		this.page_size = page_size;
		this.message_list = message_list;
		this.is_refresh = is_refresh;
		this.is_badge_display = is_badge_display;
	}

	protected void onPreExecute()
	{
		if(!is_refresh && !is_badge_display)
		{
			mDialog = new ProgressDialog(context);
			mDialog.setMessage("获取聊天数据...  ");
			mDialog.setCancelable(false);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.show();
		}
	}

	protected String doInBackground(String... params)
	{
		try
		{
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.UUID, SessionManager.getUUID(context)));
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.PAGENUMBER, page_no));
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.PAGESIZE, page_size));
			nameValuePairs.add(new BasicNameValuePair(WebElements.NEARBYPEOPLE.TIMESTAMP, String.valueOf(System.currentTimeMillis())));
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
				JSONObject j_obj = j_result.getJSONObject("object");
				apiResponse.count = j_obj.getString("count");
				JSONArray j_messages = j_obj.getJSONArray("messages");
				for(int i = 0 ; i < j_messages.length() ; i++)
				{
					JSONObject j_obj1 = j_messages.getJSONObject(i);
					ClassChat obj = new ClassChat();
					obj.id = j_obj1.getString("id");
					obj.senderId = j_obj1.getString("senderId");
					obj.senderName = j_obj1.getString("senderName");
					obj.senderPic = j_obj1.getString("senderPic");
					obj.senderPicHeight = j_obj1.getString("senderPicHeight");
					obj.senderPicWidth = j_obj1.getString("senderPicWidth");
					obj.receiverId = j_obj1.getString("receiverId");
					obj.msg = j_obj1.getString("msg");
					obj.isRead = j_obj1.getString("isRead");
					obj.isDeleted = j_obj1.getString("isDeleted");
					obj.isReplied = j_obj1.getString("isReplied");
					obj.isFriend = j_obj1.getString("isFriend");
					obj.sendtime = j_obj1.getString("sendtime");
					obj.senderName = j_obj1.getString("senderName");
					if(!j_obj1.isNull("msgType"))
					{
						obj.msgType = j_obj1.getString("msgType");
					}
					if(!j_obj1.isNull("msgStatus"))
					{
						obj.msgStatus = j_obj1.getString("msgStatus");
					}
					message_list.add(obj);
				}
			}
			else
			{
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onPostExecute(String result)
	{
		if(!is_refresh && !is_badge_display)
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
