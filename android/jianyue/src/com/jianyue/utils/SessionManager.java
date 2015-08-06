package com.jianyue.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SessionManager
{
	private static final String PREF_NAME = "JY_Session";

	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_UUID = "uuid";
	
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	
	private static final String KEY_TAKE_PIC_TUTORIAL = "take_pic_tutorial";
	private static final String KEY_NEAR_BY_TUTORIAL = "near_by_tutorial";
	private static final String KEY_MESSAGE_TUTORIAL = "message_tutorial";
	private static final String KEY_FRIEND_TUTORIAL = "friend_tutorial";
	
	/*public static boolean isLogin(Context context)
	{
		if (context.getSharedPreferences(PREF_NAME, 0).getLong(KEY_UUID, 0) > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}*/
	
	public static ClassUserDetail getObject(Context context){
    	Gson gson = new Gson();
        String json = context.getSharedPreferences(PREF_NAME, 0).getString("MyObject", "");
        ClassUserDetail obj = gson.fromJson(json, ClassUserDetail.class);
        return obj ;
    }
    
    public static void saveObject(Context context , ClassUserDetail myobject){
    	 Gson gson = new Gson();
         String json = gson.toJson(myobject);
         SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
         editor.putString("MyObject", json);
         editor.commit();
    }
    
    public static String getUUID(Context context)
	{
		String uuid = context.getSharedPreferences(PREF_NAME, 0).getString(KEY_UUID, "");
		return uuid;
	}
	
	public static void setUUID(Context context, String uuid)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(KEY_UUID, uuid);
		editor.commit();
	}
	
	public static void setLatLong(Context context, String latitude , String longitude)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putString(KEY_LATITUDE, latitude);
		editor.putString(KEY_LONGITUDE, longitude);
		editor.commit();
	}
	
	public static String getLatitude(Context context)
	{
		String latitude = context.getSharedPreferences(PREF_NAME, 0).getString(KEY_LATITUDE, "");
		return latitude;
	}
	
	public static String getLongitude(Context context)
	{
		String longtide = context.getSharedPreferences(PREF_NAME, 0).getString(KEY_LONGITUDE, "");
		return longtide;
	}
	
	public static boolean getTakePicTuorial(Context context)
	{
		boolean value = context.getSharedPreferences(PREF_NAME, 0).getBoolean(KEY_TAKE_PIC_TUTORIAL, false);
		return value;
	}
	
	public static void setTakePicTutorial(Context context, boolean value)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putBoolean(KEY_TAKE_PIC_TUTORIAL, value);
		editor.commit();
	}
	
	public static boolean getNearByTuorial(Context context)
	{
		boolean value = context.getSharedPreferences(PREF_NAME, 0).getBoolean(KEY_NEAR_BY_TUTORIAL, false);
		return value;
	}
	
	public static void setNearByTutorial(Context context, boolean value)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putBoolean(KEY_NEAR_BY_TUTORIAL, value);
		editor.commit();
	}
	
	public static boolean getMessageTuorial(Context context)
	{
		boolean value = context.getSharedPreferences(PREF_NAME, 0).getBoolean(KEY_MESSAGE_TUTORIAL, false);
		return value;
	}
	
	public static void setMessageTutorial(Context context, boolean value)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putBoolean(KEY_MESSAGE_TUTORIAL, value);
		editor.commit();
	}
	
	public static boolean getFriendTuorial(Context context)
	{
		boolean value = context.getSharedPreferences(PREF_NAME, 0).getBoolean(KEY_FRIEND_TUTORIAL, false);
		return value;
	}
	
	public static void setFriendTutorial(Context context, boolean value)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
		editor.putBoolean(KEY_FRIEND_TUTORIAL, value);
		editor.commit();
	}
}