package com.website.playlister;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import PlaylisterMain2.IUserManager;
import PlaylisterMain2.MyJson;

/**
 * Created by Thomas Rappos (6336361) on 12/18/2017.
 */
class UserManager implements IUserManager {
    private static Preferences prefs = Preferences.userNodeForPackage(UserManager.class);

    public void saveCredentials(String username, String password){
        prefs.put("username", username);
        prefs.put("password", password);
    }

    public void saveDeviceId(int deviceId){
        prefs.putInt("deviceId", deviceId);
    }

    public String getUsername(){
        return prefs.get("username", "");
    }

    public String getPassword(){
        return prefs.get("password", "");
    }

    public String getEmail(){
        return prefs.get("email", "");
    }

    public void saveEmail(String email){
        prefs.put("email", email);
    }

    //defaults to -1 if no device id is set.
    public int getDeviceId(){
        return prefs.getInt("deviceId", -1);
    }

    public boolean hasDeviceId(){
        return getDeviceId() != -1;
    }

    public long getServerDBResetId(){
        return prefs.getLong("dbResetId", 0);
    }

    public void saveServerDBResetId(long id){
        prefs.putLong("dbResetId", id);
    }

    public void saveMusicFolders(ArrayList<File> dirs) {
        ArrayList<String> paths = new ArrayList<String>();
        for(File f : dirs){
            paths.add(f.getAbsolutePath());
        }
        String jsonFolders = MyJson.toJson(paths);
        prefs.put("folders", jsonFolders);
        System.out.println("saving music folders :" + jsonFolders);
    }

    public ArrayList<File> getMusicFolders(){
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
