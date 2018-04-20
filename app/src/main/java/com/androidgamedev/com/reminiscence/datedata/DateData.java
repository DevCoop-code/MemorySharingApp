package com.androidgamedev.com.reminiscence.datedata;

public class DateData
{
    private int year;
    private int month;

    public DateData(int year, int month)
    {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
