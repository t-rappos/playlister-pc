package com.website.playlister;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Thomas Rappos (6336361) on 12/21/2017.
 */




public class MyJson {
    public static String toJson(Object o){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(o);
    }
    public static DeviceInfo toDeviceInfo(String s){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(s, DeviceInfo.class);
    }
}
