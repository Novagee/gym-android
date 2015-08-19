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

public class SendMessageDataTask extends AsyncTask<MultipartEntity, Void, String>
{
	private Context context;
	private ClassAPIResponse apiResponse;
	private String append_url;
	private ProgressDialog mDialog;

	public SendMessageDataTask(Context context, ClassAPIResponse apiResponse , String append_url)
	{
		this.context = context;
		this.apiResponse = apiResponse;
		this.append_url = append_url;
	}

	protected void onPreExecute()
	{
//		mDialog = new ProgressDialog(context);
//		mDialog.setMessage("Signing up...  ");
//		mDialog.setCancelable(false);
//		mDialog.setCanceledOnTouchOutside(false);
//		mDialog.show();
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
		
	}

	@Override
	protected void onPostExecute(String result)
	{
//		try
//		{
//			mDialog.dismiss();
//		} catch (Exception e)
//		{
//
//		}
	}
}
