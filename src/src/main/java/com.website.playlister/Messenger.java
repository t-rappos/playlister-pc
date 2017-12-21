package com.website.playlister;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by Thomas Rappos (6336361) on 12/18/2017.
 */

public class Messenger {
    private static HttpTransport httpTransport;
    private boolean valid = false;
    private String apiUrl =  "http://localhost:8080/";

    public boolean validateConnection(){
        valid = sendGETRequest("api/me") != null;
        return valid;
    }

    public void sendTracks(TrackCollection tracks){
        sendPOSTRequest("tracks",MyJson.toJson(tracks));
        System.out.println("Sent tracks to server");
    }

    public Messenger(){
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadDeviceId(){
        //if(!UserManager.hasDeviceId()){   //TODO: enabled this, also check if server hasnt been reset since last id was gathered
            String deviceName = "tomsJavaPcApp/";
            HttpResponse r = sendGETRequest("device/"+deviceName + "PC");
            if(r != null){
                try {
                    DeviceInfo di = MyJson.toDeviceInfo(r.parseAsString());
                    UserManager.saveDeviceId(di.id);
                    System.out.println("Acquired device id = " + di.id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        //} else {
        //    System.out.println("Loaded device id = " + UserManager.getDeviceId());
        //}
    }

    HttpResponse sendPOSTRequest(String endpoint, String body){
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

    HttpResponse sendGETRequest(String endpoint){
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
