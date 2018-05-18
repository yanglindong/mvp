package com.android.mylibrary.utils;

import android.text.TextUtils;
import android.util.Log;

public class MyLog {
	private static boolean DEBUG_ENABLE =true;
	private static final String TAG = "office_sh";

	public static void d(String tag, String msg) {
		if(!DEBUG_ENABLE){
			return;
		}
		if(TextUtils.isEmpty(tag)){
			d(msg);
		}else{
			Log.d(tag, msg);
		}
	}


	public static void e(String tag, String msg) {
		if(!DEBUG_ENABLE){
			return;
		}
		if(TextUtils.isEmpty(tag)){
			e(msg);
		}else{
			Log.e(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if(!DEBUG_ENABLE){
			return;
		}
		if(TextUtils.isEmpty(tag)){
			i(msg);
		}else{
			Log.i(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if(!DEBUG_ENABLE){
			return;
		}
		if(TextUtils.isEmpty(tag)){
			v(msg);
		}else{
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if(!DEBUG_ENABLE){
			return;
		}
		if(TextUtils.isEmpty(tag)){
			w(msg);
		}else{
			Log.w(tag, msg);
		}
	}
	public static void d(String msg) {
		Log.d(TAG, msg);
	}
	public static void e(String msg) {
		Log.e(TAG, msg);
	}
	public static void w(String msg) {
		Log.w(TAG, msg);
	}
	public static void v(String msg) {
		Log.v(TAG, msg);
	}
	public static void i(String msg) {
		Log.i(TAG, msg);
	}
}
