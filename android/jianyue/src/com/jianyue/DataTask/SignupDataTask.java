package com.jianyue.DataTask;

import org.apache.http.entity.mime.MultipartEntity;
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

public class SignupDataTask extends AsyncTask<MultipartEntity, Void, String>
{
	private Context context;
	private ClassAPIResponse apiResponse;
	private String append_url;
	private ProgressDialog mDialog;

	public SignupDataTask(Context context, ClassAPIResponse apiResponse , String append_url)
	{
		this.context = context;
		this.apiResponse = apiResponse;
		this.append_url = append_url;
	}

	protected void onPreExecute()
	{
		mDialog = new ProgressDialog(context);
		mDialog.setMessage("Signing up...  ");
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}

	protected String doInBackground(MultipartEntity... params)
	{
		try
		{
			String result = CallWebService.Webserice_Call_Json_Multipart(params[0] , append_url);
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
			try
			{
				 apiResponse.ack = j_result.getString("ack");
			}
			catch(Exception e)
			{
				
			}
			if(apiResponse.ack.equalsIgnoreCase("Success"))
			{
				JSONObject j_obj = j_result.getJSONObject("object");
				ClassUserDetail obj = new ClassUserDetail();
				obj.id = j_obj.getString("id");
				obj.name = j_obj.getString("name");
				obj.deviceToken = j_obj.getString("deviceToken");
				obj.gender = j_obj.getString("gender");
				obj.interestIn = j_obj.getString("interestIn");
				obj.pic = j_obj.getString("pic");
				obj.picHeight = j_obj.getString("picHeight");
				obj.picWidth = j_obj.getString("picWidth");
				obj.uuid = j_obj.getString("uuid");
				SessionManager.saveObject(context, obj);
				SessionManager.setUUID(context, obj.uuid);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onPostExecute(String result)
	{
		try
		{
			mDialog.dismiss();
		} catch (Exception e)
		{

		}
	}
}
