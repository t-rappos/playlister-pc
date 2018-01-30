package com.website.playlister;

/**
 * Created by Thomas Rappos (6336361) on 12/18/2017.
 */

import PlaylisterMain2.Messenger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog {


    public LoginDialog(final Messenger messenger,
                       final TrayApp tray){
        final JFrame frame = new JFrame("DialogDemo");
        final JTextField userFrame = new JTextField("Username");
        final JPasswordField passFrame = new JPasswordField("Password");
        JButton loginB = new JButton("Login");

        frame.getContentPane().add(userFrame,BorderLayout.NORTH);
        frame.getContentPane().add(passFrame,BorderLayout.CENTER);
        frame.getContentPane().add(loginB,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

        loginB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = new String(passFrame.getPassword());
                UserManager userManager = new UserManager();
                userManager.saveCredentials(userFrame.getText(),password );

                boolean valid = messenger.validateConnection(Config.getLocalServerURL())
                        || messenger.validateConnection(Config.getRemoteServerURL());
                if(valid){
                    frame.setVisible(false);
                    tray.setLoggedIn();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Username or Password is incorrect");
                    tray.setLoggedOut();
                }
            }
        });

    }
}


        /*
        userFrame.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e){
                userFrame.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        */