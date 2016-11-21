package com.yzi.doutu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yzi.doutu.bean.DataBean;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by yzh on 2015/10/26.
 */
public class DBTools {

    private List<DataBean> infos;
    private Cursor cursor;
    private static DBHelpers dbHelpers;
    private static DBTools dbTools;

    public static DBTools getInstance(Context context) {
        if (dbTools == null) {
            dbTools = new DBTools();
            dbHelpers = new DBHelpers(context);
        }
        return dbTools;
    }

    //--------------收藏表相关------------

    /**
     * 获取收藏的列表
     *
     * @param fromCatch 是否取缓存 操作更新过SQLite 传false
     * @return
     */
    public List<DataBean> getFavorites(boolean fromCatch) {
        if (infos != null && !infos.isEmpty()) {
            if (fromCatch)
                return infos;
        }

        infos = new ArrayList<>();
        cursor = dbHelpers.select();
        if (cursor.getCount() <= 0) {
            return infos;
        }
        cursor.moveToFirst();
        while (true) {
            DataBean info = new DataBean();
            info.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setGifPath(cursor.getString(cursor.getColumnIndex("url")));
            info.setPicPath(cursor.getString(cursor.getColumnIndex("url")));
            infos.add(info);
            if (cursor.isLast()) {
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();


        return infos;
    }

    /**
     * 添加收藏
     *
     * @param fav
     */
    public void addFavorites(DataBean fav) {
        String  id=String.valueOf(fav.getId());
        //查询id
        Cursor cursor = dbHelpers.selectById(id);
        //存在就修改，不存在就新增
        if (cursor != null && cursor.getCount() > 0) {
            System.err.println("Count" + cursor.getCount());
            cursor.moveToFirst();
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            dbHelpers.update(String.valueOf(_id), fav.getName(), fav.getGifPath());
        } else {
            dbHelpers.insert(id, fav.getName(), fav.getGifPath());
        }
        cursor.close();
    }

    public void remove(int id){
        dbHelpers.deleteByid(String.valueOf(id));
    }

    public void removeAll(){
        dbHelpers.deleteAll();
    }


    //--------------制作表相关------------

    /**
     * 获取制作的列表
     *
     * @return
     */
    public List<DataBean> getMades() {

        infos = new ArrayList<>();
        cursor = select_made();
        if (cursor.getCount() <= 0) {
            return infos;
        }
        cursor.moveToFirst();
        while (true) {
            DataBean info = new DataBean();
            info.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setGifPath(cursor.getString(cursor.getColumnIndex("url")));
            info.setPicPath(cursor.getString(cursor.getColumnIndex("url")));
            info.setMadeUrl(cursor.getString(cursor.getColumnIndex("madeUrl")));
            info.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
            infos.add(info);
            if (cursor.isLast()) {
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();


        return infos;
    }

    /**
     * 添加制作
     *
     * @param fav
     */
    public void addMades(DataBean fav) {
        String  id=String.valueOf(fav.getId());
        //查询id
        Cursor cursor = selectById_made(id);
        //存在就修改，不存在就新增
        if (cursor != null && cursor.getCount() > 0) {
            System.err.println("Count" + cursor.getCount());
            cursor.moveToFirst();
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            update_made(String.valueOf(_id), fav.getName(), fav.getGifPath(),fav.getMadeUrl(),fav.getFileName());
        } else {
            insert_made(id, fav.getName(), fav.getGifPath(),fav.getMadeUrl(),fav.getFileName());
        }
        cursor.close();
    }

    /**
     * 根据图片id查询该条信息
     * @param id
     * @return
     */
    public DataBean madeById(String id){
        DataBean info=null;
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+dbHelpers.TABLE_MADE+" where id='"+id+"'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            info=new DataBean();
            info.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setGifPath(cursor.getString(cursor.getColumnIndex("url")));
            info.setPicPath(cursor.getString(cursor.getColumnIndex("url")));
            info.setMadeUrl(cursor.getString(cursor.getColumnIndex("madeUrl")));
            info.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
            cursor.close();
        }
        return info;
    }


    /**
     * 根据图片查询该条数据的_id
     * @param id
     * @return
     */
    public Cursor selectById_made(String id){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        Cursor cursor=db.rawQuery("select _id from "+dbHelpers.TABLE_MADE+" where id='"+id+"'", null);

        return cursor;
    }

    public Cursor select_made(){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        //Cursor c=db.rawQuery("select * from "+TABLE_NAME, null);
        Cursor cursor=db.query(dbHelpers.TABLE_MADE, null, null, null, null, null, null);
        return cursor;
    }


    /**
     * 新增
     */
    public void insert_made(String id,String name,String url,String madeUrl,String fileName){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        db.execSQL("insert into "+dbHelpers.TABLE_MADE+"(id,name,url,madeUrl,fileName) values(?,?,?,?,?)",
                new String[]{id,name,url,madeUrl,fileName});
    }

    /**
     * 修改
     */
    public void update_made(String _id,String name,String url,String madeUrl,String fileName){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();

        ContentValues cv=new ContentValues();
        //cv.put("id", id);
        cv.put("name", name);
        cv.put("url", url);
        cv.put("madeUrl", madeUrl);
        cv.put("fileName", fileName);
        db.update(dbHelpers.TABLE_MADE, cv, "_id= ?", new String[]{_id});
    }

    public  void  deleteByid_made(String id){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        db.delete(dbHelpers.TABLE_MADE, "id= ?", new String[]{id});
    }

    /**
     * 删除全部
     */
    public  void  deleteAll_made(){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        db.delete(dbHelpers.TABLE_MADE,null,null);

    }

}
