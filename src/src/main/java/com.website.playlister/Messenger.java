package com.website.playlister;

import PlaylisterMain2.TrackCollection;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * Created by Thomas Rappos (6336361) on 12/18/2017.
 */

class Messenger {
    private static HttpTransport httpTransport;
    private String apiUrl =  "http://localhost:8080/";

    //returns true if user credentials are correct
    boolean validateConnection(){
        return sendGETRequest("api/me") != null;
    }

    private void sendTracksPartitioned(TrackCollection col, String endpoint ){
        if(!col.tracks.isEmpty()){
            ArrayList<TrackCollection> cols = col.split(100);
            for(TrackCollection c : cols){
                if(!c.tracks.isEmpty()){
                    sendPOSTRequest(endpoint, MyJson.toJson(c));
                    System.out.println("Sent tracks to server: " + endpoint);
                }
            }
        }
    }

    void sendTracks(TrackCollection toAdd, TrackCollection toRemove){
        sendTracksPartitioned(toAdd, "tracks");
        sendTracksPartitioned(toRemove, "removetracks");
        System.out.println("Finished sending tracks to server");
    }

    Messenger(){
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean checkIfServerDBHasReset(){
        boolean serverHasReset = true;
        HttpResponse r = sendGETRequest("status/");
        if(r!=null) {
            try {
                ServerStatus s = (ServerStatus)MyJson.toObject(r.parseAsString(), ServerStatus.class);
                long lastResetId = UserManager.getServerDBResetId();
                if(s.dbResetId == lastResetId){
                    System.out.println("Server has not reset since last run");
                    serverHasReset = false;
                } else {
                    System.out.println("Server has reset since last run");
                    UserManager.saveServerDBResetId(s.dbResetId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return serverHasReset;
    }

    void loadDeviceId(){
        if(!UserManager.hasDeviceId() || checkIfServerDBHasReset()){   //TODO: enabled this, also check if server hasnt been reset since last id was gathered
            String deviceName = "tomsJavaPcApp/";
            HttpResponse r = sendGETRequest("device/"+deviceName + "PC");
            if(r != null){
                try {
                    TrackStore.invalidateStore();
                    DeviceInfo di = (DeviceInfo)MyJson.toObject(r.parseAsString(), DeviceInfo.class);
                    UserManager.saveDeviceId(di.id);
                    System.out.println("Acquired device id = " + di.id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Using last device id " + UserManager.getDeviceId());
        }
    }

    private HttpResponse sendPOSTRequest(String endpoint, String body){
        GenericUrl url2 = new GenericUrl(apiUrl + endpoint);
        BasicAuthentication ba = new BasicAuthentication(UserManager.getUsername(), UserManager.getPassword());
        try{
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();//credential
            HttpRequest request = requestFactory.buildPostRequest(url2, ByteArrayContent.fromString("application/json", body));
            request.getHeaders().setContentType("application/json");
            ba.initialize(request);
            return request.execute();
        } catch(Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    private HttpResponse sendGETRequest(String endpoint){
        GenericUrl url2 = new GenericUrl(apiUrl + endpoint);
        BasicAuthentication ba = new BasicAuthentication(UserManager.getUsername(), UserManager.getPassword());
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
