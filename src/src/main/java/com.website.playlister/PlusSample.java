package com.website.playlister;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.*;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * @author Yaniv Inbar
 */
public class PlusSample {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "TestApp";

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/plus_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static Plus plus;

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(PlusSample.class.getResourceAsStream("/client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=plus "
                            + "into plus-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(
                dataStoreFactory).build();
        // authorize

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static void main(String[] args) {
        try {
            //https://docs.oracle.com/javase/tutorial/uiswing/misc/systemtray.html

            if(!SystemTray.isSupported()){
                System.out.println("SystemTray is not supported");
                return;
            }
            final PopupMenu popup = new PopupMenu();

            URL u2 = PlusSample.class.getResource("/bulb.gif");
            Image image = Toolkit.getDefaultToolkit().getImage(u2);

            final TrayIcon trayIcon = new TrayIcon(image);
            final SystemTray tray = SystemTray.getSystemTray();

            // Create a pop-up menu components
            MenuItem aboutItem = new MenuItem("About");
            MenuItem settings = new MenuItem("Settings");

            MenuItem scan = new MenuItem("Upload Music Data");
            MenuItem login = new MenuItem("Log In");
            MenuItem exit = new MenuItem("Exit");

            exit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    System.exit(0);
                }
            });

            //CheckboxMenuItem cb1 = new CheckboxMenuItem("Log In");

            //Add components to pop-up menu
            popup.add(aboutItem);
            popup.add(settings);
            popup.addSeparator();
            popup.add(login);

            boolean loggedIn = false;
            if(loggedIn){
                popup.add(scan);
            }
            popup.addSeparator();
            popup.add(exit);

            JFrame frame = new JFrame("DialogDemo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JOptionPane.showMessageDialog(frame,
                    "Eggs are not supposed to be green.");
            //newContentPane.setOpaque(true); //content panes must be opaque
            //frame.setContentPane(newContentPane);

            //Display the window.
            //frame.pack();
            //frame.setVisible(true);

            //showMessageDialog()
            trayIcon.setPopupMenu(popup);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
            }

            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            //GenericUrl url = new GenericUrl("http://localhost:8080/profile2");
            GenericUrl url2 = new GenericUrl("http://localhost:8080/api/me");

            /*
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();

            // set up global Plus instance
            plus = new Plus.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
                    APPLICATION_NAME).build();
            // run commands
            listActivities();
            getActivity();
            getProfile();

/*

 */
            BasicAuthentication ba = new BasicAuthentication("tom", "rap");

            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();//credential
            //HttpRequest request = requestFactory.buildGetRequest(url);
            HttpRequest request = requestFactory.buildGetRequest(url2);
            ba.initialize(request);
            HttpResponse execute = request.execute();
            System.out.println(execute.parseAsString());

            //HttpResponse execute = request.execute();
            //System.out.println(execute.parseAsString());
            //buildPostRequest(url, ByteArrayContent.fromString("application/json", requestBody));
            //request.getHeaders().setContentType("application/json");

            // success!
            return;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }

    /** List the public activities for the authenticated user. */
    private static void listActivities() throws IOException {
        View.header1("Listing My Activities");
        // Fetch the first page of activities
        Plus.Activities.List listActivities = plus.activities().list("me", "public");
        listActivities.setMaxResults(5L);
        // Pro tip: Use partial responses to improve response time considerably
        listActivities.setFields("nextPageToken,items(id,url,object/content)");
        ActivityFeed feed = listActivities.execute();
        // Keep track of the page number in case we're listing activities
        // for a user with thousands of activities. We'll limit ourselves
        // to 5 pages
        int currentPageNumber = 0;
        while (feed.getItems() != null && !feed.getItems().isEmpty() && ++currentPageNumber <= 5) {
            for (Activity activity : feed.getItems()) {
                View.show(activity);
                View.separator();
            }
            // Fetch the next page
            String nextPageToken = feed.getNextPageToken();
            if (nextPageToken == null) {
                break;
            }
            listActivities.setPageToken(nextPageToken);
            View.header2("New page of activities");
            feed = listActivities.execute();
        }
    }

    /** Get an activity for which we already know the ID. */
    private static void getActivity() throws IOException {
        // A known public activity ID
        String activityId = "z12gtjhq3qn2xxl2o224exwiqruvtda0i";
        // We do not need to be authenticated to fetch this activity
        View.header1("Get an explicit public activity by ID");
        Activity activity = plus.activities().get(activityId).execute();
        View.show(activity);
    }

    /** Get the profile for the authenticated user. */
    private static void getProfile() throws IOException {
        View.header1("Get my Google+ profile");
        Person profile = plus.people().get("me").execute();
        View.show(profile);
    }
}