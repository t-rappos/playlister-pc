package com.website.playlister;

import java.io.File;
import java.util.ArrayList;
import org.apache.commons.io.*;

/**
 * Created by Thomas Rappos (6336361) on 12/21/2017.
 */

class TrackCollection{
    public long deviceId;
    public ArrayList<Track> tracks = new ArrayList<Track>();
    public TrackCollection(){
    }
    public void addTrack(Track t){
        tracks.add(t);
    }
    public void addTrackCollection(TrackCollection tc){
        tracks.addAll(tc.tracks);
    }
};

public class TrackScanner {


    public static boolean isFileValid(File f){
        boolean isFile = f.isFile();
        boolean isMp3 =  FilenameUtils.getExtension(f.getName())
                .compareToIgnoreCase("mp3") == 0;
        return isFile && isMp3;
    }

    static TrackCollection scanRecursive(String d){
        File[] files = new File(d).listFiles();
        TrackCollection col = new TrackCollection();
        if (files != null && files.length > 0){
            for (File f:files) {
                if(isFileValid(f)){
                    System.out.println(f.getName());
                    Track t = new Track(f);
                    col.addTrack(t);
                } else if(f.isDirectory()){
                    col.addTrackCollection(scanRecursive(f.getAbsolutePath()));
                }
            }
        }
        return col;
    }



    public static TrackCollection scan(String directory){
        //File[] files = new File("E:\\music\\Gabriel_And_Dresden_-_The_Only_Road-(ANJCD058)-WEB-2017-MMS_INT [EDM RG]").listFiles();
        long startTime = System.nanoTime();
        TrackCollection col = scanRecursive("E:\\music\\Gabriel_And_Dresden_-_The_Only_Road-(ANJCD058)-WEB-2017-MMS_INT [EDM RG]");

        long dt = System.nanoTime() - startTime;
        System.out.println("Completed in " + (float)dt/1000000f + " ms");
        col.deviceId = UserManager.getDeviceId();
        return col;
    }
}
