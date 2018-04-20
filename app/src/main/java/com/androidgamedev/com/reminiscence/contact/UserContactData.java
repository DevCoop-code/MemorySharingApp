package com.androidgamedev.com.reminiscence.contact;

public class UserContactData
{
    private String email;
    private String name;
    private String auth;

    public UserContactData(String email, String name, String auth)
    {
        this.email = email;
        this.name = name;
        this.auth = auth;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String isAuth()
    {
        return auth;
    }

    public void setAuth(String auth)
    {
        this.auth = auth;
    }

    public String[] getCDataArray()
    {
        String[] cData = {this.email, this.name, this.auth};

        return cData;
    }
}
