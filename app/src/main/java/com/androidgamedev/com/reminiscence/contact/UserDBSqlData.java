package com.androidgamedev.com.reminiscence.contact;

public class UserDBSqlData
{
    public static final String SQL_DB_CREATE_TABLE =
            "create table user_table " +
            "(uemail text primary key, " +
            "uname text not null, " +
            "uauth text not null)";

    public static final String SQL_DB_INSERT_DATA =
            "insert into user_table " +
            "(uemail, uname, uauth) " +
            "values(?,?,?)";

    public static final String SQL_DB_SELECT_AUTH =
            "select uemail from user_table where uauth = ?";

    public static final String CHECK_ACCOUNT =
            "select uemail from user_table where uemail = ?";

    public static final String SQL_DB_SELECT_NAME =
            "select uname from user_table where uemail = ?";

    public static final String SQL_DB_UPDATE_AUTH =
            "update user_table set uauth = 1 where uemail = ?";

    public static final String SQL_DB_UPDATE_AUTH2 =
            "update user_table set uauth = 0 where uemail = ?";
}
