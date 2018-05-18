package com.android.mylibrary.MultiThreadDownload.filedao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yld on 2018/5/2.
 */

public class DBOpenHelper extends SQLiteOpenHelper{
    private static final String DBNAME = "eric.db";
    private static final int VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key" +
                "autoincrement,downpath varchar(100),threadid INTEGER,downlength INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS filedownlog");
        onCreate(sqLiteDatabase);
    }
}
