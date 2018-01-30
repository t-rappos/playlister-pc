package PlaylisterMain2;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.*;

public class Messenger {
    private IUserManager userManager;
    private static NetHttpTransport httpTransport;
    private String apiUrl;

    //returns true if user credentials are correct, sets api url
    public boolean validateConnection(String APIUrl){
        apiUrl = APIUrl;
        Boolean res = sendGETRequest("api/me") != null;
        if(!res){
            System.out.println("Couldn't validate user");
        } else {
            System.out.println("Connected to API "+APIUrl+" successfully");
        }
        return res;
    }

    private void sendTracksPartitioned(TrackCollection col, String endpoint ){
        System.out.println("sending in total " + col.tracks.size() + " tracks to " + endpoint);
        if(!col.tracks.isEmpty()){
            ArrayList<TrackCollection> cols = col.split(500);
            for(TrackCollection c : cols){
                if(!c.tracks.isEmpty()){
                    System.out.println("sending " + c.tracks.size() + " tracks to " + endpoint);
                    sendPOSTRequest(endpoint, MyJson.toJson(c));
                    System.out.println("Sent tracks to server: " + endpoint);
                }
            }
        }
    }

    public void sendTracks(TrackCollection toAdd, TrackCollection toRemove){
        sendTracksPartitioned(toAdd, "tracks");
        sendTracksPartitioned(toRemove, "removetracks");
        System.out.println("Finished sending tracks to server");
    }

    public Messenger(IUserManager pUserManager){
        httpTransport = new NetHttpTransport();
        userManager = pUserManager;
    }

    private boolean checkIfServerDBHasReset(){
        boolean serverHasReset = true;
        System.out.println("checkIfServerDBHasReset");
        HttpResponse r = sendGETRequest("status/");
        if(r!=null) {
            try {
                ServerStatus s = (ServerStatus)MyJson.toObject(r.parseAsString(), ServerStatus.class);
                long lastResetId = userManager.getServerDBResetId();
                if(s.dbResetId == lastResetId){
                    System.out.println("Server has not reset since last run");
                    serverHasReset = false;
                } else {
                    System.out.println("Server has reset since last run");
                    userManager.saveServerDBResetId(s.dbResetId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return serverHasReset;
    }

    public ArrayList<TrackPath> loadTrackPathsForPlaylist(long playlistId){
        HttpResponse r = sendGETRequest("mtracks/" + Long.toString(playlistId));
        if(r != null) {
            try {
                return MyJson.toTrackPathArray(r.parseAsString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Playlist> loadPlaylists(){
        HttpResponse r = sendGETRequest("mplaylists/");
        if(r != null) {
            try {
                return MyJson.toPlaylistArray(r.parseAsString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void loadDeviceId(ATrackStore trackStore){
        if(!userManager.hasDeviceId() || checkIfServerDBHasReset()){   //TODO: enabled this, also check if server hasnt been reset since last id was gathered
            String deviceName = "tomsJavaPcApp/";
            HttpResponse r = sendGETRequest("device/"+deviceName + "PC");
            if(r != null){
                try {
                    System.out.println("loadDeviceId: invalidateStore");
                    trackStore.invalidateStore();
                    DeviceInfo di = (DeviceInfo)MyJson.toObject(r.parseAsString(), DeviceInfo.class);
                    userManager.saveDeviceId((int)di.id);
                    System.out.println("Acquired device id = " + di.id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Using last device id " + userManager.getDeviceId());
        }
    }

    private HttpResponse sendPOSTRequest(String endpoint, String body){
        GenericUrl url2 = new GenericUrl(apiUrl + endpoint);
        BasicAuthentication ba = new BasicAuthentication(userManager.getUsername(), userManager.getPassword());
        try{
            System.out.println("sending post to " + url2.getRawPath());
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();//credential
            HttpRequest request = requestFactory.buildPostRequest(url2, ByteArrayContent.fromString("application/json", body));
            request.getHeaders().setContentType("application/json");
            ba.initialize(request);
            HttpResponse r =  request.execute();
            System.out.println("done sending post to " + url2.getRawPath());
            return r;
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    private HttpResponse sendGETRequest(String endpoint){
        GenericUrl url2 = new GenericUrl(apiUrl + endpoint);
        BasicAuthentication ba = new BasicAuthentication(userManager.getUsername(), userManager.getPassword());
        try{
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();//credential
            HttpRequest request = requestFactory.buildGetRequest(url2);
            ba.initialize(request);
            return request.execute();
        } catch(Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }
}

