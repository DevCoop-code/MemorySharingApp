package com.androidgamedev.com.reminiscence.server;

import java.util.Date;

public class MemoryDataDTO
{
    private int bno;
    private String uemail;
    private String uname;
    private String imagepath;
    private String description;
    private Date regdate;

    public int getBno() {
        return bno;
    }

    public void setBno(int bno) {
        this.bno = bno;
    }

    public String getUemail()
    {
        return uemail;
    }

    public void setUemail(String uemail)
    {
        this.uemail = uemail;
    }

    public String getUname()
    {
        return uname;
    }

    public void setUname(String uname)
    {
        this.uname = uname;
    }

    public String getImagepath()
    {
        return imagepath;
    }

    public void setImagepath(String imagepath)
    {
        this.imagepath = imagepath;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getRegdate()
    {
        return regdate;
    }

    public void setRegdate(Date regdate)
    {
        this.regdate = regdate;
    }
}
