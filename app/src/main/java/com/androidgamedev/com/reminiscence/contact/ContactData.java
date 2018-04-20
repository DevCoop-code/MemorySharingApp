package com.androidgamedev.com.reminiscence.contact;

public class ContactData
{
    private String imagepath = null;
    private String orientation = null;
    private String description = null;

    public ContactData(String imagepath, String orientation, String description)
    {
        this.imagepath = imagepath;
        this.orientation = orientation;
        this.description = description;
    }

    public String getImagepath()
    {
        return imagepath;
    }

    public void setImagepath(String imagepath)
    {
        this.imagepath = imagepath;
    }

    public String getOrientation()
    {
        return orientation;
    }

    public void setOrientation(String orientation)
    {
        this.orientation = orientation;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String[] getCDataArray()
    {
        String[] cData = {this.imagepath, this.orientation, this.description};

        return cData;
    }
}
