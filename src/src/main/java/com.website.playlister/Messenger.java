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

    public boolean validateConnection(){
        valid = send();
        return valid;
    }

    public void sendTracks(){

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
    public boolean send(){
        GenericUrl url2 = new GenericUrl("http://localhost:8080/api/me");
        BasicAuthentication ba = new BasicAuthentication(UserManager.getUsername(), UserManager.getPassword());
        try{
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();//credential
            HttpRequest request = requestFactory.buildGetRequest(url2);
            ba.initialize(request);
            HttpResponse execute = request.execute();
            System.out.println(execute.parseAsString());
            return true;
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        return false;
    }
}
