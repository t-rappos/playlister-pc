package PlaylisterMain2;

import java.util.ArrayList;

/**
 * Created by Thomas Rappos (6336361) on 12/29/2017.
 */

public class TrackCollection {
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
}
