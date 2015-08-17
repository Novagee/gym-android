package com.jianyue.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.jianyue.main.controller.R;

public class AlertDialogUtility
{
	public static void showFlexibleAlert(Context context, String msg, String positive_text , String negative_text , DialogInterface.OnClickListener onOkClick)
	{
		new AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name)).setMessage(msg).setCancelable(false).setPositiveButton(positive_text, null).setNegativeButton(negative_text, onOkClick).show();
	}
	
	public static void showLocationAlert(Context context, String msg, DialogInterface.OnClickListener onOkClick , DialogInterface.OnClickListener onDontallowClick)
	{
		new AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name)).setMessage(msg).setCancelable(false).setPositiveButton("不同意", onDontallowClick).setNegativeButton("同意", onOkClick).show();
	}
}
