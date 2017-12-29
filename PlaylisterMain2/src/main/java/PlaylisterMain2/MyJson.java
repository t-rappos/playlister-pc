package PlaylisterMain2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

/**
 * Created by Thomas Rappos (6336361) on 12/29/2017.
 */

public class MyJson {
    public static String toJson(Object o){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(o);
    }

    public static Object toObject(String s, Class c){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(s, c);
    }

    public static ArrayList<String> toStringList(String s){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        ArrayList<String> temp = new ArrayList<String>();
        return gson.fromJson(s, temp.getClass());
    }
}
