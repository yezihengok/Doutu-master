package com.yzi.doutu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.yzi.doutu.service.DouApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class SharedUtils
{

	// 配置文件名称
	public static final String SHARED_NAME = "ABC";

	private static SharedPreferences getInstance(Context context, int mode, String shareName)
	{
		SharedPreferences sharedPreferences;
		if (TextUtils.isEmpty(shareName))
		{
			sharedPreferences = context.getSharedPreferences(SHARED_NAME, mode); // 读取文件,如果没有则会创建
		}
		else
		{
			sharedPreferences = context.getSharedPreferences(shareName, mode); // 读取文件,如果没有则会创建
		}

		return sharedPreferences;
	}

	private static Editor getShareEditor(Context context, String shareName)
	{
		SharedPreferences sharedPreferences = getInstance(context, android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.edit();
	}

	/**
	 *
	 * 设置数据
	 *
	 * */
	public static boolean putString(String shareName, Context context, String key, String value)
	{
		Editor editor = getShareEditor(context, shareName);
		editor.putString(key, value);
		return editor.commit();
	}
	/**
	 *
	 * 设置数据
	 *
	 * */
	public static boolean putString(String shareName, String key, String value)
	{
		Editor editor = getShareEditor(DouApplication.getInstance(), shareName);
		editor.putString(key, value);
		return editor.commit();
	}


	/**
	 *
	 * 设置数据
	 *
	 * */
	public static boolean putLong(String shareName, Context context, String key, Long value)
	{
		Editor editor = getShareEditor(context, shareName);
		editor.putLong(key, value);
		return editor.commit();
	}

	/**
	 *
	 * 设置数据
	 *
	 * */
	public static boolean putInt(String shareName, String key, int value)
	{
		Editor editor = getShareEditor(DouApplication.getInstance(), shareName);
		editor.putInt(key, value);
		return editor.commit();
	}

	public static boolean putBoolean(String shareName, Context context, String key, boolean value)
	{
		Editor editor = getShareEditor(context, shareName);
		editor.putBoolean(key, value);
		return editor.commit();
	}

	/**
	 *
	 * 获取数据
	 *
	 * */
	public static String getString(String shareName, Context context, String key)
	{
		SharedPreferences sharedPreferences = getInstance(context, android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.getString(key, "");

	}

	/**
	 *
	 * 获取数据
	 *
	 * */
	public static String getString(String shareName, Context context, String key, String defValue)
	{
		SharedPreferences sharedPreferences = getInstance(context, android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.getString(key, defValue);

	}

	public static String getString(String shareName, String key, String defValue)
	{
		SharedPreferences sharedPreferences = getInstance(DouApplication.getInstance(), android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.getString(key, defValue);

	}

	/**
	 *
	 * 获取数据
	 *
	 * */
	public static Long getLong(String shareName, Context context, String key)
	{
		SharedPreferences sharedPreferences = getInstance(context, android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.getLong(key, 0);
	}

	/**
	 *
	 * 获取数据
	 *
	 * */
	public static int getInt(String shareName, String key)
	{
		SharedPreferences sharedPreferences = getInstance(DouApplication.getInstance(), android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.getInt(key, 0);
	}

	public static boolean getBoolean(String shareName, Context context, String key)
	{
		SharedPreferences sharedPreferences = getInstance(context, android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.getBoolean(key, false);
	}

	public static boolean getBoolean(String shareName, Context context, String key, boolean defValue)
	{
		SharedPreferences sharedPreferences = getInstance(context, android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.getBoolean(key, defValue);
	}

	/**
	 *
	 * 清空数据
	 *
	 * */
	public boolean clear(String shareName, Context context)
	{
		SharedPreferences sharedPreferences = getInstance(context, android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.edit().clear().commit();
	}

	/***
	 *
	 * 删除数据
	 *
	 * @param context
	 * @param key
	 * @return
	 *
	 */
	public static boolean remove(String shareName, Context context, String key)
	{
		SharedPreferences sharedPreferences = getInstance(context, android.content.Context.MODE_PRIVATE, shareName);
		return sharedPreferences.edit().remove(key).commit();
	}

	/**
	 *  SharedPreferences 存储 Object 对象 （Object需 implements Serializable）
	 * @param key
	 * @param obj
	 */

	public static void putObject(String key , Object obj,Context context){
		if(context==null||obj==null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences("newObj",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();


		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			String serStr = bos.toString("ISO-8859-1");
			serStr = URLEncoder.encode(serStr, "UTF-8");
			editor.putString(key, serStr);
			editor.commit();

		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				oos.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Object getObject(String key,Context context){
		SharedPreferences sp = context.getSharedPreferences("newObj",
				Context.MODE_PRIVATE);
		String serStr = sp.getString(key, "");
		ByteArrayInputStream bai = null;
		ObjectInputStream ois = null;
		Object object = null;
		if(serStr != ""){

			try {
				String sedStr = URLDecoder.decode(serStr, "UTF-8");
				bai = new ByteArrayInputStream(sedStr.getBytes("ISO-8859-1"));
				ois = new ObjectInputStream(bai);
				object = ois.readObject();

			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					ois.close();
					bai.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		return object;
	}

}
