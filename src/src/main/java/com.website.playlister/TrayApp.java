package com.website.playlister;

import PlaylisterMain2.Messenger;
import PlaylisterMain2.Playlist;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Thomas Rappos (6336361) on 12/18/2017.
 */
public class TrayApp {
    private final PopupMenu popup = new PopupMenu();
    private URL u2 = PlusSample.class.getResource("/bulb.gif");
    private Image image = Toolkit.getDefaultToolkit().getImage(u2);
    private final TrayIcon trayIcon = new TrayIcon(image);
    private final SystemTray tray = SystemTray.getSystemTray();

    // Create a pop-up menu components
    private MenuItem aboutItem = new MenuItem("About");
    private MenuItem settings = new MenuItem("Settings");

    private MenuItem scan = new MenuItem("Scan and Upload");
    private MenuItem login = new MenuItem("Log In");
    private MenuItem logout = new MenuItem("Log Out");
    private MenuItem exit = new MenuItem("Exit");
    UserManager userManager = new UserManager();
    final Messenger messenger = new Messenger(userManager);

    public TrayApp(){
        //https://docs.oracle.com/javase/tutorial/uiswing/misc/systemtray.html
        System.out.println("using details stored for " + userManager.getUsername());

        if(!SystemTray.isSupported()){
            System.out.println("SystemTray is not supported");
            return;
        }

        final TrayApp trap = this;

        logout.addActionListener((ActionEvent e)->{
            userManager.saveCredentials("","");
            setLoggedOut();
        });

        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginDialog ld = new LoginDialog(messenger,trap);
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    tray.remove(trayIcon);
                    System.exit(0);
                } catch(Exception e1){
                    e1.printStackTrace();
            }
            }
        });

        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MusicFolderDialog mfd = new MusicFolderDialog(trap);
            }
        });

        scan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(() -> {
                    TrackScanner.scan(scan, userManager, messenger);    //TODO: scanning shouldnt have anything to do with sending...
                    //messenger.sendTracks(results.toAdd, results.toRemove);
                    scan.setLabel("Finished Scanning");
                    try {
                        Thread.sleep(10000);
                        scan.setLabel("Scan and Upload");
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                });
                t.start();
            }
        });

        if(messenger.validateConnection(Config.getLocalServerURL())
                || messenger.validateConnection(Config.getRemoteServerURL()))
        {
            TrackStore store = new TrackStore(); //TODO: make invalidateStore(), a static function?
            messenger.loadDeviceId(store);
            ArrayList<Playlist> pa = messenger.loadPlaylists();
            ArrayList<String> playlists = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Integer> ids = new ArrayList<>();
            for(Playlist p : pa){
                if(p.name != null){
                    names.add(p.name);
                    ids.add((int)(long)p.id);
                }
            }
            PlaylistListDialog.CreateAndShowGui(names, ids);
            setLoggedIn();
        } else {
            setLoggedOut();
        }

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }

    }

    public void setLoggedIn(){
        popup.removeAll();
        popup.add(aboutItem);
        popup.add(settings);
        popup.addSeparator();
        popup.add(logout);
        popup.add(scan);
        popup.addSeparator();
        popup.add(exit);
        trayIcon.setPopupMenu(popup);
    }

    public void setLoggedOut(){
        popup.removeAll();
        popup.add(aboutItem);
        popup.add(settings);
        popup.addSeparator();
        popup.add(login);
        popup.addSeparator();
        popup.add(exit);
        trayIcon.setPopupMenu(popup);
    }

}
