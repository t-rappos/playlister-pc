package com.website.playlister;
import PlaylisterMain2.Track;
import PlaylisterMain2.TrackCollection;

import java.io.*;
import java.util.HashSet;

/**
 * Created by Thomas Rappos (6336361) on 12/27/2017.
 */


public class TrackStore {
    TrackCollection toAdd = new TrackCollection();
    TrackCollection toRemove = new TrackCollection();

    TrackStore(long deviceId){
        toAdd.deviceId = deviceId;
        toRemove.deviceId = deviceId;
    }

    private HashSet<Track> loadStore(){
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
    private void saveStore(HashSet<Track> tracks){
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
    static void invalidateStore(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(("tracks.txt")));
            oos.writeObject(new HashSet<Track>());
            System.out.println("Invalidating track storage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void checkInTracks(TrackCollection col){
        HashSet<Track> tracks = new HashSet<Track>(col.tracks);


        HashSet<Track> oldTracks = loadStore();

        for(Track t : tracks){
            if(!oldTracks.contains(t)){
                toAdd.addTrack(t);
                System.out.println("adding track " + t.filename);
            }
        }
        for(Track ot : oldTracks){
            if (!tracks.contains(ot)) {
                toRemove.addTrack(ot);
                System.out.println("removing track " + ot.filename);
            }
        }

        saveStore(tracks);
    }
}
