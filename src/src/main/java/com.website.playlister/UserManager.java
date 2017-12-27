package com.website.playlister;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * Created by Thomas Rappos (6336361) on 12/18/2017.
 */
class UserManager {
    private static Preferences prefs = Preferences.userNodeForPackage(UserManager.class);

    static void saveCredentials(String username, String password){
        prefs.put("username", username);
        prefs.put("password", password);
    }

    static void saveDeviceId(long deviceId){
        prefs.putLong("deviceId", deviceId);
    }

    static String getUsername(){
        return prefs.get("username", "");
    }

    static String getPassword(){
        return prefs.get("password", "");
    }

    //defaults to -1 if no device id is set.
    static long getDeviceId(){
        return prefs.getLong("deviceId", -1);
    }

    static boolean hasDeviceId(){
        return getDeviceId() != -1;
    }

    static long getServerDBResetId(){
        return prefs.getLong("dbResetId", 0);
    }

    static void saveServerDBResetId(long id){
        prefs.putLong("dbResetId", id);
    }

    static void saveMusicFolders(ArrayList<File> dirs) {
        ArrayList<String> paths = new ArrayList<String>();
        for(File f : dirs){
            paths.add(f.getAbsolutePath());
        }
        String jsonFolders = MyJson.toJson(paths);
        prefs.put("folders", jsonFolders);
        System.out.println("saving music folders :" + jsonFolders);
    }

    static ArrayList<File> getMusicFolders(){
        ArrayList<File> result = new ArrayList<File>();
        String jsonFolders = prefs.get("folders","");
        System.out.println("loading music folders :" + jsonFolders);
        ArrayList<String> folderList = MyJson.toStringList(jsonFolders);
        for(String s : folderList){
            result.add(new File(s));
        }
        return result;
    }

}
