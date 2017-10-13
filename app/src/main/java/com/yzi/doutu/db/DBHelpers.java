package com.yzi.doutu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * @author yeziheng
 *2014-5-8 13:40:21
 */
public class DBHelpers extends SQLiteOpenHelper {
	
	private final static int DB_VERSION=4;
	private final static String DB_NAME="userInfo.db";
	public final static String TABLE_NAME="favorites";
	public final static String TABLE_MADE="made";
	public final static String TABLE_THEME="favoritesTheme";
	public DBHelpers(Context context) {
		super(context,DB_NAME , null, DB_VERSION);
	}


	 
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "+TABLE_NAME+"( _id integer primary key autoincrement,id text,name text,url text)");
		db.execSQL("create table "+TABLE_MADE+"( _id integer primary key autoincrement,id text,name text,url text,madeUrl text,fileName text,proportion text)");
		db.execSQL("create table "+TABLE_THEME+"( _id integer primary key autoincrement,id text,userId text,folderId text,folderName text,thumbs text)");
		//db.execSQL("insert into "+TABLE_NAME+"(name,psotalcode,address,province,city,area) values(?,?,?)",new String[]{"测试1","测试2","测试3"});
		 
	}

	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion==3){
			//newVersion==3时，原应用是没有TABLE_THEME 这张表的
			db.execSQL("create table "+TABLE_THEME+"( _id integer primary key autoincrement,id text,userId text,folderId text,folderName text,thumbs text)");
		}
		else if(newVersion==4){
			//TABLE_MADE表 新增字段表proportion
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MADE);
			db.execSQL("create table "+TABLE_MADE+"( _id integer primary key autoincrement,id text,name text,url text,madeUrl text,fileName text,proportion text)");
		}
		else{
			String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
			String sql2 = "DROP TABLE IF EXISTS " + TABLE_MADE;
			String sql3 = "DROP TABLE IF EXISTS " + TABLE_THEME;
			db.execSQL(sql);
			db.execSQL(sql2);
			db.execSQL(sql3);
			onCreate(db);
		}

	}
	
	/**
	 * 新增
	 */
	public void insert(String id,String name,String url){
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("insert into "+TABLE_NAME+"(id,name,url) values(?,?,?)",
				new String[]{id,name,url});
	}
	
	/**
	 * 修改
	 */
	public  void  update(String _id,String name,String url){
		SQLiteDatabase db=this.getWritableDatabase();
	 
		ContentValues cv=new ContentValues();
		//cv.put("id", id);
		cv.put("name", name);
		cv.put("url", url);
		db.update(TABLE_NAME, cv, "_id= ?", new String[]{_id});
	}
	
	public  void  delete(int _id){
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_NAME, "_id= ?", new String[]{Integer.toString(_id)});
	}

//	public  void  deleteByid(String id){
//		SQLiteDatabase db=this.getWritableDatabase();
//		db.delete(TABLE_NAME, "id= ?", new String[]{id});
//	}
//
//	/**
//	 * 删除全部
//	 */
//	public  void  deleteAll(){
//		SQLiteDatabase db=this.getWritableDatabase();
//		db.delete(TABLE_NAME,null,null);
//
//	}
	

	
	/**
	 * 根据图片查询该条数据的_id
	 * @param id
	 * @return
	 */
	public Cursor selectById(String id){
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery("select _id from "+TABLE_NAME+" where id='"+id+"'", null);

		return cursor;
	}

	public Cursor selectAllByDesc(){
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery("select * from "+TABLE_NAME+" order by _id desc", null);

		return cursor;
	}

	/**
	 * 根据所有列查询 _id
	 * @param id
	 * @param name
	 * @param url
     * @return
     */
	public Cursor selectAllColumn(String id,String name,String url){
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery("select _id from "+TABLE_NAME+" where id='"+id+"'"
				+" and name='"+name+"'"
				+" and phone='"+url+"'", null);

		return cursor;
	}
	
}
