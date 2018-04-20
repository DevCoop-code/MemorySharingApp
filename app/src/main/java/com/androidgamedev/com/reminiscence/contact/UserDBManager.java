package com.androidgamedev.com.reminiscence.contact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBManager
{
    private final String DB_NAME = "user.db";
    private final int DB_VERSION = 2;   //테이블에 Password 항목 삭제, 비밀번호는 서버에만 저장

    private Context mContext = null;
    private OpenHelper mOpener = null;
    private SQLiteDatabase mDbController = null;

    private class OpenHelper extends SQLiteOpenHelper
    {
        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(UserDBSqlData.SQL_DB_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
        {

        }
    }

    //생성자
    public UserDBManager(Context context)
    {
        this.mContext = context;
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

    //새로운 계정 여부 확인
    public String checkNewOrNot(String sql, String[] email)
    {
        //sql = select uemail from user_table where uemail = ?
        Cursor results = this.mDbController.rawQuery(sql, email);
        results.moveToFirst();

        String uemail = null;
        while(!results.isAfterLast())
        {
            //getString(index) -> index : 열 번호를 의미
            uemail = results.getString(0);

            results.moveToNext();
        }
        results.close();

        return uemail;
    }

    //새로운 계정을 추가할 경우 데이터베이스에 계정 추가
    public void insertData(String sql, UserContactData userData)
    {
        String[] sqlData = userData.getCDataArray();
        this.mDbController.execSQL(sql, sqlData);
    }

    //로그인을 한 경우 로그인 된 계정 이메일 가져오기
    public String selectAuthEmail(String sql, String[] auth)
    {
        //sql = "select uemail from user_table uauth = ?";
        Cursor results = this.mDbController.rawQuery(sql, auth);
        results.moveToFirst();

        String uemail = null;
        while(!results.isAfterLast())
        {
            uemail = results.getString(0);

            results.moveToNext();
        }
        results.close();

        return uemail;
    }

    public String selectName(String sql, String[] email)
    {
        Cursor result = this.mDbController.rawQuery(sql, email);
        result.moveToFirst();

        String name = null;
        while(!result.isAfterLast())
        {
            name = result.getString(0);

            result.moveToNext();
        }
        result.close();

        return name;
    }

    public void updateUauth(String sql, String[] email)
    {
        Cursor result = this.mDbController.rawQuery(sql, email);
        result.moveToFirst();

        while(!result.isAfterLast())
        {
            result.moveToNext();
        }
        result.close();
    }
}
