package com.website.playlister;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Thomas Rappos (6336361) on 12/22/2017.
 */
public class MusicFolderDialog extends JPanel
        implements ListSelectionListener{

    private JFrame frame = new JFrame("Music Folders");
    private JList list;
    private DefaultListModel listModel;
    private JButton removeFolderButton;
    private int selectedListIndex = 0;
    private JFileChooser fileChooser = new JFileChooser();
    private ArrayList<File> musicFolders = new ArrayList<File>();
    UserManager userManager = new UserManager();

    private void loadMusicFolders(){
        try{
            musicFolders = userManager.getMusicFolders();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    private ArrayList<File> getCurrentMusicFolders(){

        //ArrayList<File> t = new ArrayList<File>();
        //t.add(new File("E:\\music\\Gabriel_And_Dresden_-_The_Only_Road-(ANJCD058)-WEB-2017-MMS_INT [EDM RG]"));
        //t.add(new File("E:\\music\\Vandal Recors - Modern Soul"));
        return musicFolders;
    }
    */

    private void addMusicFolder(File dir){
        if(!dir.isDirectory()){
            System.out.println("Only directories can be selected!");
            return;
        }
        musicFolders.add(dir);
        listModel.addElement(dir.getAbsolutePath());
        System.out.println("adding folder: " + dir.getName());
        userManager.saveMusicFolders(musicFolders);
    }

    private void removeMusicFolder(File dir){
        if(!dir.isDirectory()){
            System.out.println("Only directories can be selected!");
            return;
        }
        musicFolders.remove(dir);
        listModel.removeElement(dir.getAbsolutePath());
        System.out.println("removing folder: " + dir.getName());
        userManager.saveMusicFolders(musicFolders);
    }
//https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
    public MusicFolderDialog(final TrayApp tray){

        System.out.println("building MusicFolderDialog" );
        listModel = new DefaultListModel();
        loadMusicFolders();
        for( File f : musicFolders){
            listModel.addElement(f.getAbsolutePath());
        }
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton selectFolderButton = new JButton("Add Folder");
        removeFolderButton = new JButton("Remove Folder");

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        selectFolderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
                int rv = fileChooser.showOpenDialog(frame);
                if (rv == JFileChooser.APPROVE_OPTION) {
                    addMusicFolder(fileChooser.getSelectedFile());
                } else {
                    System.out.println("Open command cancelled by user." );
                }
            }
        });
        removeFolderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("removeFolderButton pressed");
                removeMusicFolder(musicFolders.get(selectedListIndex));
            }
        });

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(selectFolderButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(removeFolderButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));


        final JLabel title = new JLabel("Music Folders");
        frame.getContentPane().add(title, BorderLayout.NORTH);
        frame.getContentPane().add(listScrollPane,BorderLayout.CENTER);
        frame.getContentPane().add(buttonPane,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            selectedListIndex = list.getSelectedIndex();
            if (selectedListIndex == -1) {
                //No selection, disable fire button.
                removeFolderButton.setEnabled(false);

            } else {
                //Selection, enable the fire button.
                removeFolderButton.setEnabled(true);
                System.out.println("Selected list item " + list.getSelectedIndex());
            }
        }
    }
}
