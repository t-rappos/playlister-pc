package com.website.playlister;
import PlaylisterMain2.Track;
import PlaylisterMain2.TrackCollection;
import PlaylisterMain2.ATrackStore;

import java.io.*;
import java.util.HashSet;


/**
 * Created by Thomas Rappos (6336361) on 12/27/2017.
 */


public class TrackStore extends PlaylisterMain2.ATrackStore{
    TrackCollection toAdd = new TrackCollection();
    TrackCollection toRemove = new TrackCollection();

    @Override
    public HashSet<Track> loadStore(){
        //load old tracks
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tracks.txt"));
            return (HashSet<Track>)ois.readObject();
        } catch (IOException e){
            e.printStackTrace();
            return new HashSet<Track>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new HashSet<Track>();
    }

    //https://stackoverflow.com/questions/7673424/how-to-dump-a-hashset-into-a-file-in-java
    @Override
    public void saveStore(HashSet<Track> tracks){
        //save new tracks
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(("tracks.txt")));
            oos.writeObject(tracks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: if there has been a server reset we'll need to resend all tracks.
    //right now this is linked to when a new deviceId is required, we can
    //assume that the server has reset
    public void invalidateStore(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(("tracks.txt")));
            oos.writeObject(new HashSet<Track>());
            System.out.println("Invalidating track storage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
