package com.android.mylibrary.MultiThreadDownload.filedao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.mylibrary.MultiThreadDownload.filedao.DBOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yld on 2018/5/2.
 */

public class FileService{
    private DBOpenHelper dbOpenHelper;
    public FileService(Context context) {
        dbOpenHelper = new DBOpenHelper(context);

    }

    /**
     * 获取特定url每条线程已经下载的文件长度
     * @param path
     * @return
     */

    public Map<Integer,Integer> getThreadIdAndDownlengthByPath(String path){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select threadid ,downlength from filedownlog where downpath=?",new String[]{path});
        Map<Integer,Integer> data = new HashMap<>();
        while (cursor.moveToNext()){
            data.put(cursor.getInt(0),cursor.getInt(0));
            data.put(cursor.getInt(cursor.getColumnIndexOrThrow("threadid")),cursor.getInt(cursor.getColumnIndexOrThrow("downlength")));

        }
        cursor.close();
        db.close();
        return data;
    }

    /**
     * 保存每条线程已经下载的文件长度
     * @param path 下载的路径
     * @param map 现在的id和已经下载的长度的集合
     */
    public  void  save(String path,Map<Integer,Integer> map){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            for (Map.Entry<Integer,Integer> entry : map.entrySet()) {
                db.execSQL("insert into filedownlog(downpath,threadid,downlength)value(?,?,?)",new Object[]{path,entry.getKey(),entry.getValue()});
                db.setTransactionSuccessful();
            }
        }finally {
            db.endTransaction();
        }
            db.close();
    }

    /**
     * 实时更新每条线程已经下载的文件长度
     * @param path
     * @param threadId
     * @param pos
     */
    public void update(String path,int threadId,int pos){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("update filedownlog set downlength=?where downpath=?and threadid=?",new Object[]{pos,path,threadId});
        db.close();
    }

    /**
     * 当文件下载完成后，删除对应的下载记录
     * @param path
     */
    public void delete(String path){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("delete from filedownlog where downpath=?",new Object[]{path});
        db.close();
    }

}
