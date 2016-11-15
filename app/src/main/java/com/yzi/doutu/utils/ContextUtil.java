package com.yzi.doutu.utils;

import android.content.Context;

/**
 * 上下文工具类,想要contxt就从这里拿
 * @author
 *
 */
public class ContextUtil {
	private static Context applicationContext;

	public static Context getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(Context applicationContext) {
		ContextUtil.applicationContext = applicationContext;
	}
	
}
