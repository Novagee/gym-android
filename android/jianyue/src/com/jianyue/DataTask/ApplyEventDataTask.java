package com.jianyue.DataTask;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jianyue.utils.ClassAPIResponse;
import com.jianyue.utils.GlobalData;
import com.jianyue.webservices.CallWebService;

public class ApplyEventDataTask extends AsyncTask<MultipartEntity, Void, String>
{
	private Context context;
	private ClassAPIResponse apiResponse;
	private String append_url;
	private ProgressDialog mDialog;
	private String mobile;
	private boolean is_refresh = false;

	public ApplyEventDataTask(Context context, ClassAPIResponse apiResponse , String append_url)
	{
		this.context = context;
		this.apiResponse = apiResponse;
		this.append_url = append_url;;
	}

	protected void onPreExecute()
	{
		if(!is_refresh)
		{
			mDialog = new ProgressDialog(context);
			mDialog.setMessage("正在报名...  ");
			mDialog.setCancelable(false);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.show();
		}
	}

	protected String doInBackground(MultipartEntity... params)
	{
		try
		{
			String result = CallWebService.Webserice_Call_Json_Multipart(params[0] , append_url);
			
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
