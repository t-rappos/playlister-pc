package com.website.playlister;

/**
 * Created by Thomas Rappos (6336361) on 1/30/2018.
 */
public class Config {
    private static final String REMOTE_SERVER = "https://thawing-atoll-11089.herokuapp.com/";
    private static final String LOCAL_SERVER = "http://localhost:8080/";
    public static String getRemoteServerURL(){return REMOTE_SERVER;}
    public static String getLocalServerURL(){return LOCAL_SERVER;}
}
