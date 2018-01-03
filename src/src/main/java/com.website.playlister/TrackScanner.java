package com.website.playlister;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;

import PlaylisterMain2.*;
import org.apache.commons.io.*;


/**
 * Created by Thomas Rappos (6336361) on 12/21/2017.
 */


class ScanRetryTask implements Runnable{
    private int index = 0;
    private IRetryTaskContext<Track> parent;
    private ArrayList<File> files;

    ScanRetryTask(int i, IRetryTaskContext<Track> p, ArrayList<File> f){
        System.out.println("Starting task @ " + i);
        index = i;
        parent = p;
        files = f;
    }
    @Override
    public void run() {
        FilePropertyReader fpr = new FilePropertyReader();
        for(int i = index; i < files.size(); i++){
            File f = files.get(i);
            if(TrackScanner.isFileValid(f)){
                if(f.getName().compareTo("Crookers - Magic Bus(1).mp3")==0){
                    int bp = 0;
                }
                System.out.println(f.getName());
                Track t = new Track(f,fpr);
                parent._addDataAndProgress(t);
            }
        }
        parent._setComplete();
        files.clear();
    }

}

class ScanRetryTaskContext extends IRetryTaskContext<Track> {
    private ArrayList<File> files;
    private Messenger messenger;

    ScanRetryTaskContext(ArrayList<File> pFiles, Messenger m){
        files = pFiles;
        messenger = m;
    }

    @Override
    public Runnable _getNewTask(int progressIndex) {
        return new ScanRetryTask(progressIndex, this, files);
    }

    @Override
    public void _onCompletion() {
        System.out.println("ScanRetryTaskContext: FInished Scanning");
        TrackStore trackStore = new TrackStore();
        TrackCollection col = new TrackCollection();
        col.tracks.addAll(this.getResults());
        trackStore.checkInTracks(col,new UserManager());
        System.out.println(trackStore.toAdd.tracks.size() + " tracks to be added");
        messenger.sendTracks(trackStore.toAdd, trackStore.toRemove);
        files.clear();
        col.tracks.clear();
    }
}

public class TrackScanner {

    public static boolean isFileValid(File f){
        boolean isFile = f.isFile();
        boolean isMp3 =  FilenameUtils.getExtension(f.getName())
                .compareToIgnoreCase("mp3") == 0;
        return isFile && isMp3;
    }

    public static ArrayList<File> getFiles(File pf){
        ArrayList<File> res = new ArrayList<File>();
        File[] files = pf.listFiles();
        if (files != null && files.length > 0) {
            for (File f:files) {
                if(f.isDirectory()){
                    res.addAll(getFiles(f));
                } else {
                    if(isFileValid(f)){
                        res.add(f);
                    }
                }
            }
        }
        return res;
    }

    public static void scan(MenuItem scan, UserManager userManager, Messenger m){
        long startTime = System.nanoTime();
        ArrayList<File> musicFolders = userManager.getMusicFolders();

        ArrayList<File> files = new ArrayList<>();
        for(File f : musicFolders){
            files.addAll(getFiles(f));
        }

        ScanRetryTaskContext task = new ScanRetryTaskContext(files, m);
        task.Run();

        files.clear();
        long dt = System.nanoTime() - startTime;
        System.out.println("Completed in " + (float)dt/1000000f + " ms");

        scan.setLabel("Scanning: verifying scanned tracks");
        scan.setLabel("Scanning: sending to server");
    }
}
