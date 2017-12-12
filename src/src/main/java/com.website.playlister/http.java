package com.website.playlister;


import com.google.common.net.HttpHeaders;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.*;
import org.apache.http.impl.client.BasicCredentialsProvider;



/**
 * Created by Thomas Rappos (6336361) on 12/12/2017.
 */
public class http {

    public static void main(String[] args) {

        try {

            /*
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials
                    = new UsernamePasswordCredentials("jack", "secret");
            provider.setCredentials(AuthScope.ANY, credentials);

            HttpClient client = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(provider)
                    .build();

            HttpResponse response = client.execute(
                    new HttpGet(URL_SECURED_BY_BASIC_AUTHENTICATION));
            int statusCode = response.getStatusLine()
                    .getStatusCode();
*/
/*
            HttpGet request = new HttpGet("http://localhost:3000/api/me");
            String auth = "jack" + ":" + "secret";
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("ISO-8859-1")));
            String authHeader = "Basic " + new String(encodedAuth);
            request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            //assertThat(statusCode, equalTo(HttpStatus.SC_OK));
*/
            /*
            URL url = new URL ("http://localhost:3000/api/me");


            String encoding = Base64.getEncoder().encodeToString(("jack:secret").getBytes());

            HttpPost httppost = new HttpPost("http://host:post/test/login");
            httppost.setHeader("Authorization", "Basic " + encoding);

            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            */

        } catch(Exception e) {
            e.printStackTrace();
        }

    }


}
