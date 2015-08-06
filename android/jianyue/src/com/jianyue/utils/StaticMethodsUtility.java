package com.jianyue.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jianyue.main.controller.R;

public class StaticMethodsUtility
{
	 public static void showPositiveToast(Activity activity, String msg)
		{
			LayoutInflater inflater = activity.getLayoutInflater();
			View layout = inflater.inflate(R.layout.toast, (ViewGroup) activity.findViewById(R.id.toast_layout_root));

			TextView txt = (TextView) layout.findViewById(R.id.tvToast);
			txt.setText(msg);
			txt.setBackgroundResource(R.drawable.green_toast);

			Toast toast = new Toast(activity);
			toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(layout);
			toast.show();
		}

		public static void showNegativeToast(Activity activity, String msg)
		{
			LayoutInflater inflater = activity.getLayoutInflater();
			View layout = inflater.inflate(R.layout.toast, (ViewGroup) activity.findViewById(R.id.toast_layout_root));

			TextView txt = (TextView) layout.findViewById(R.id.tvToast);
			txt.setText(msg);
			txt.setBackgroundResource(R.drawable.red_toast);

			Toast toast = new Toast(activity);
			toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(layout);
			toast.show();
		}

}
