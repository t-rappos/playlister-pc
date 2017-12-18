package com.website.playlister;

import java.util.prefs.Preferences;

/**
 * Created by Thomas Rappos (6336361) on 12/18/2017.
 */
public class UserManager {
    private static Preferences prefs = Preferences.userNodeForPackage(UserManager.class);

    public static void saveCredentials(String username, String password){
        prefs.put("username", username);
        prefs.put("password", password);
    }

    public static String getUsername(){
        String username = prefs.get("username", "");
        return username;
    }

    public static String getPassword(){
        String password = prefs.get("password", "");
        return password;
    }
}
