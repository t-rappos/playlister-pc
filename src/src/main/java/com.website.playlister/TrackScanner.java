package com.website.playlister;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.*;
import PlaylisterMain2.FolderScanner;
import PlaylisterMain2.Track;
import PlaylisterMain2.TrackCollection;


/**
 * Created by Thomas Rappos (6336361) on 12/21/2017.
 */

/*
class TrackCollection{
    public long deviceId;
    public ArrayList<Track> tracks = new ArrayList<Track>();

    public void addTrack(Track t){
        tracks.add(t);
    }
    public void addTrackCollection(TrackCollection tc){
        tracks.addAll(tc.tracks);
    }

    public ArrayList<TrackCollection> split(int maxArraySize){
        ArrayList<TrackCollection> result = new ArrayList<TrackCollection>();
        int arrayCount = tracks.size() / maxArraySize;
        //int leftover = tracks.size() - arrayCount * maxArraySize;
        for(int i = 0; i < arrayCount; i++){
            TrackCollection col = new TrackCollection();
            col.deviceId = deviceId;
            col.tracks.addAll(tracks.subList(maxArraySize*i,maxArraySize*(i+1)));
            result.add(col);
        }
        TrackCollection col = new TrackCollection();
        col.tracks.addAll(tracks.subList(maxArraySize*arrayCount,tracks.size()));
        col.deviceId = deviceId;
        result.add(col);
        return result;
    }
};
*/

public class TrackScanner {

    public static boolean isFileValid(File f){
        boolean isFile = f.isFile();
        boolean isMp3 =  FilenameUtils.getExtension(f.getName())
                .compareToIgnoreCase("mp3") == 0;
        return isFile && isMp3;
    }

    static int folderCount = 0;
    static int maxFolderCount = 0;
    static TrackCollection scanRecursive(String d, MenuItem scan){
        File[] files = new File(d).listFiles();
        TrackCollection col = new TrackCollection();
        FilePropertyReader propReader = new FilePropertyReader(); //TODO: should this be moved higher scope?
        if (files != null && files.length > 0){
            for (File f:files) {
                if(isFileValid(f)){
                    //System.out.println(f.getName());
                    Track t = new Track(f,propReader);
                    col.addTrack(t);
                } else if(f.isDirectory()){
                    folderCount++;
                    scan.setLabel("Scanning " + folderCount + " / " + maxFolderCount);
                    System.out.println(folderCount + " / " + maxFolderCount);
                    col.addTrackCollection(scanRecursive(f.getAbsolutePath(), scan));
                }
            }
        }
        return col;
    }


    private static int countFolders(ArrayList<File> files){
        long startTime = System.nanoTime();
        int res = 0;
        for(File f : files){
            res += FolderScanner.countFolders(f);
        }
        long dt = System.nanoTime() - startTime;
        System.out.println(res + " folders, completed in " + (float)dt/1000000f + " ms");
        return res;
    }

    public static TrackStore scan(MenuItem scan){
        //File[] files = new File("E:\\music\\Gabriel_And_Dresden_-_The_Only_Road-(ANJCD058)-WEB-2017-MMS_INT [EDM RG]").listFiles();
        long startTime = System.nanoTime();
        TrackCollection col = new TrackCollection();
        ArrayList<File> musicFolders = UserManager.getMusicFolders();

        folderCount = 0;
        maxFolderCount = countFolders(musicFolders);

        for(File f : musicFolders){
            col.addTrackCollection(scanRecursive(f.getAbsolutePath(), scan));
        }
        long dt = System.nanoTime() - startTime;
        System.out.println("Completed in " + (float)dt/1000000f + " ms");

        scan.setLabel("Scanning: verifying scanned tracks");
        TrackStore trackStore = new TrackStore( UserManager.getDeviceId());
        trackStore.checkInTracks(col);

        scan.setLabel("Scanning: sending to server");
        return trackStore;
    }
}
