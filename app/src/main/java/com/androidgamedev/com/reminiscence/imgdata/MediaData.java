package com.androidgamedev.com.reminiscence.imgdata;

import android.widget.ImageView;

public class MediaData
{
    private int type = 0;               //비디오, 이미지 구분
    private int mediaid = 0;            //미디어 ID
    private String mediapath = null;    //미디어 경로
    private int orientation = 0;        //미디어 방향
    private ImageView imgview = null;   //이미지를 표시하기 위한 뷰

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMediaid() {
        return mediaid;
    }

    public void setMediaid(int mediaid) {
        this.mediaid = mediaid;
    }

    public String getMediapath() {
        return mediapath;
    }

    public void setMediapath(String mediapath) {
        this.mediapath = mediapath;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public ImageView getImgview() {
        return imgview;
    }

    public void setImgview(ImageView imgview) {
        this.imgview = imgview;
    }
}
