package com.androidgamedev.com.reminiscence.contact;

public class DBSqlData
{
    public static final String SQL_DB_CREATE_TABLE =
            "create table picdes_table " +
            "(reg_id integer primary key autoincrement, " +
            "imagepath text not null, " +
            "orientation text not null, " +
            "description text)";

    public static final String SQL_DB_INSERT_DATA =
            "insert into picdes_table " +
            "(imagepath, orientation, description) " +
            "values(?,?,?)";

    public static final String SQL_DB_SELECT_DATA =
            "select description from picdes_table where imagepath = ?";

    public static final String SQL_DB_DELETE_DATA =
            "delete from picdes_table where imagepath = ?";
}
