package com.androidgamedev.com.reminiscence.contact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager
{
    private final String DB_NAME = "picdes.db";
    private final int DB_VERSION = 1;

    /*
    SQLiteOpenHelper = 데이터베이스의 생성 및 버전 관리를 도와줌
    SQLiteDatabase = 데이터베이스 관리를 위한 메서드 제공
     */
    private Context mContext = null;
    //데이터베이스 생성 및 버전관리를 도와줌
    private OpenHelper mOpener = null;
    //데이터베이스 관리를 위한 메서드 제공
    private SQLiteDatabase mDbController = null;

    private class OpenHelper extends SQLiteOpenHelper
    {
        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase aDb)
        {
            aDb.execSQL(DBSqlData.SQL_DB_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
        {

        }
    }

    //생성자
    public DBManager(Context aContext)
    {
        this.mContext = aContext;
        this.mOpener = new OpenHelper(mContext, DB_NAME, null, DB_VERSION);
    }

    public void dbOpen()
    {
        this.mDbController = mOpener.getWritableDatabase();
    }

    public void dbClose()
    {
        this.mDbController.close();
    }

    public void insertData(String aSql, ContactData aCData)
    {
        String[] sqlData = aCData.getCDataArray();
        this.mDbController.execSQL(aSql, sqlData);
    }

    public String select(String aSql, String[] imagepath)
    {
        Cursor results = this.mDbController.rawQuery(aSql, imagepath);
        results.moveToFirst();

        String des = null;
        while(!results.isAfterLast())
        {
            des = results.getString(0);

            results.moveToNext();
        }
        results.close();

        return des;
    }

    public void delete(String sql, String[] imagepath)
    {
        Cursor results = this.mDbController.rawQuery(sql, imagepath);
        results.moveToFirst();

        while(!results.isAfterLast())
        {
            results.moveToNext();
        }
        results.close();
    }
}
