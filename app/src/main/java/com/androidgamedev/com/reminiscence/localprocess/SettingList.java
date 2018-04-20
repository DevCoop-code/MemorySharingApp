package com.androidgamedev.com.reminiscence.localprocess;

import java.util.ArrayList;
import java.util.List;

public class SettingList
{
    public static List<String> generateStringListData()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("비밀번호 변경");
        list.add("버전정보");
        list.add("로그아웃");
        return list;
    }
}
