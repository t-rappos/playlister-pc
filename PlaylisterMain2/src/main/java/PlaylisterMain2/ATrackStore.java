package PlaylisterMain2;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.HashSet;

/**
 * Created by Thomas Rappos (6336361) on 12/29/2017.
 */

public abstract class ATrackStore {
    public TrackCollection toAdd = new TrackCollection();
    public TrackCollection toRemove = new TrackCollection();

    private void setDeviceId(long deviceId){
        toAdd.deviceId = deviceId;
        toRemove.deviceId = deviceId;
    }

    public abstract HashSet<Track> loadStore();
    public abstract void saveStore(HashSet<Track> tracks);

    /*
    HashSet<Track> loadStore()
    {
        System.err.println("loadStore error, didnt extend base class");
        return null;
    }

    void saveStore(HashSet<Track> tracks){System.err.println("loadStore error, didnt extend base class");}
    */

    //TODO: if there has been a server reset we'll need to resend all tracks.
    //right now this is linked to when a new deviceId is required, we can
    //assume that the server has reset
    public abstract void invalidateStore();

    public void checkInTracks(TrackCollection col, IUserManager userManager){
        HashSet<Track> tracks = new HashSet<Track>(col.tracks);
        HashSet<Track> oldTracks = loadStore();

        System.out.println("Checking in " + col.tracks.size() + " tracks, and " + tracks.size() + " unique tracks");
        System.out.println("Comparing against " + oldTracks.size() + " unique tracks");

        for(Track t : tracks){
            if(!oldTracks.contains(t)){
                toAdd.addTrack(t);
                System.out.println("adding track: " + t.filename);
            }
        }
        for(Track ot : oldTracks){
            if (!tracks.contains(ot)) {
                toRemove.addTrack(ot);
                System.out.println("removing track: " + ot.filename);
            }
        }

        System.out.println("XFound " + toAdd.tracks.size() + " new untracked tracks that need to be added to server");
        System.out.println("Couldn't find " + toRemove.tracks.size() + " existing tracks from last scan, these will have to be removed from the server");

        setDeviceId(userManager.getDeviceId());
        saveStore(tracks);
    }
}
