package com.yzi.doutu.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

public class PraseUtils {

	/**
	 * @param json 要解析的数据
	 * @param classOfT 解析成的对象
	 * @param ail  根节点名称ail
	 * @return  返回解析成的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseJson(String json, Class<T> classOfT, String ail){
		Object object=null;
		Gson gson=new Gson();
		try {
			JSONObject jsonObject=new JSONObject(json);
			object=gson.fromJson(jsonObject.getString(ail), classOfT);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (T)object;
	}
	/**
	 * 
	 * @param json 要解析的数据
	 * @param classOfT 解析成的对象
	 * @return  返回解析成的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseJson(String json, Class<T> classOfT){
		Object object=null;
//		Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		Gson gson=new Gson();
		try {
			JSONObject jsonObject=new JSONObject(json);
			object=gson.fromJson(jsonObject.getString("res"), classOfT);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (T)object;
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public static <T> T parseJsons(String json, Class<T> classOfT){
		Object object=null;
		json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
		
		//JSONObject resultJson = new JSONObject(json);
		Gson gson=new Gson();
		try {
			object=gson.fromJson(json, classOfT);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		return (T)object;
	}
}
