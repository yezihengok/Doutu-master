package com.yzi.doutu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.bean.Theme;
import com.yzi.doutu.service.DouApplication;

import java.util.ArrayList;
import java.util.List;

import static com.yzi.doutu.utils.CommUtil.isGif;

/**
 * Created by yzh on 2015/10/26.
 */
public class DBTools {

    private List<DataBean> infos;
    private Cursor cursor;
    private static DBHelpers dbHelpers;
    private static DBTools dbTools;

    public static DBTools getInstance() {
        if (dbTools == null) {
            dbTools = new DBTools();
            dbHelpers = new DBHelpers(DouApplication.getInstance());
        }
        return dbTools;
    }


    /**
     * 根据图片查询该条数据的_id
     * @param id
     * @return
     */
    public Cursor selectById(String id,String table){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        Cursor cursor=db.rawQuery("select _id from "+table+" where id='"+id+"'", null);
        return cursor;
    }

    /**
     * 查询所有
     * @return
     */
    public Cursor selectAll(String table){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+table+" order by _id desc", null);
        //Cursor cursor=db.query(table, null, null, null, null, null, null);
        return cursor;
    }
    public Cursor selectAll(int start,int end,String table){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+table+" order by _id desc"+" limit "+start+","+end, null);
        return cursor;
    }

    public  void  deleteById(String id,String table){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        db.delete(table, "id= ?", new String[]{id});
    }

    /**
     * 删除全部
     */
    public  void  deleteAll(String table){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        db.delete(table,null,null);
    }

    //--------------收藏表相关------------

    /**
     * 获取收藏的列表
     * @param start 查询条数的起止数，都为0则视为查询全部
     * @param end
     * @return
     */
    public List<DataBean> getFavorites(int start,int end) {

        infos = new ArrayList<>();
        if(start==0&&end==0){
            cursor = selectAll(DBHelpers.TABLE_NAME);
        }else{
            cursor = selectAll(start,end,DBHelpers.TABLE_NAME);
        }

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

//    public void remove(int id){
//        dbHelpers.deleteByid(String.valueOf(id));
//    }
//
//    public void removeAll(){
//        dbHelpers.deleteAll();
//    }


    //--------------主题收藏表相关------------
    /**
     * 获取主题的列表
     * @param start 查询条数的起止数，都为0则视为查询全部
     * @param end
     * @return
     */
    public List<Theme> getThemes(int start,int end) {

        List<Theme>  infos = new ArrayList<>();
        if(start==0&&end==0){
            cursor = selectAll(dbHelpers.TABLE_THEME);
        }else{
            cursor = selectAll(start,end,dbHelpers.TABLE_THEME);
        }
        if (cursor.getCount() <= 0) {
            return infos;
        }
        cursor.moveToFirst();
        while (true) {
            Theme info = new Theme();
            info.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
            info.setFolderId(cursor.getString(cursor.getColumnIndex("folderId")));
            info.setFolderName(cursor.getString(cursor.getColumnIndex("folderName")));
            info.setThumbs(cursor.getString(cursor.getColumnIndex("thumbs")));
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
    public void addThemes(Theme fav) {
        String  id=fav.getId();
        //查询id
        Cursor cursor = selectById(id,dbHelpers.TABLE_THEME);
        //存在就修改，不存在就新增
        if (cursor != null && cursor.getCount() > 0) {
            System.err.println("Count" + cursor.getCount());
            cursor.moveToFirst();
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            updateThemes(String.valueOf(_id), fav.getUserId(), fav.getFolderId(),fav.getFolderName(),fav.getThumbs());
        } else {
            insertThemes(id, fav.getUserId(), fav.getFolderId(),fav.getFolderName(),fav.getThumbs());
        }
        cursor.close();
    }

    /**
     * 新增
     */
    public void insertThemes(String id,String name,String url,String madeUrl,String fileName){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();
        db.execSQL("insert into "+dbHelpers.TABLE_THEME+"(id,userId,folderId,folderName,thumbs) values(?,?,?,?,?)",
                new String[]{id,name,url,madeUrl,fileName});
    }

    /**
     * 修改
     */
    public void updateThemes(String _id,String userId,String folderId,String folderName,String thumbs){
        SQLiteDatabase db=dbHelpers.getWritableDatabase();

        ContentValues cv=new ContentValues();
        //cv.put("id", id);
        cv.put("userId", userId);
        cv.put("folderId", folderId);
        cv.put("folderName", folderName);
        cv.put("thumbs", thumbs);
        db.update(dbHelpers.TABLE_THEME, cv, "_id= ?", new String[]{_id});
    }



    //--------------制作表相关------------
    /**
     * 获取制作的列表
     * @param start 查询条数的起止数，都为0则视为查询全部
     * @param end
     * @return
     */
    public List<DataBean> getMades(int start,int end) {

        infos = new ArrayList<>();
        if(start==0&&end==0){
            cursor = selectAll(dbHelpers.TABLE_MADE);
        }else{
            cursor = selectAll(start,end,dbHelpers.TABLE_MADE);
        }
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
            info.setIs_gif(isGif(info.getMadeUrl()));
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
        Cursor cursor = selectById(id,dbHelpers.TABLE_MADE);
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





}
