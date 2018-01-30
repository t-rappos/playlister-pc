package com.website.playlister;

import PlaylisterMain2.TrackPath;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static javax.swing.JOptionPane.ERROR_MESSAGE;


public  class TrackDialog
        extends JPanel
        implements ListSelectionListener {

    private ArrayList<TrackPath> tracks;
    private PlaylistInteraction type;
    JFrame frame;
    private String playlistName;

    private TrackDialog(String confirmationString,
                        PlaylistInteraction ptype,
                        ArrayList<TrackPath> ptracks,
                        String pplaylistName){
        super(new BorderLayout());
        tracks = ptracks;
        type = ptype;
        playlistName = pplaylistName;

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for(TrackPath s : ptracks){
            listModel.addElement(s.filename);
        }

        JList list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(30);

        JButton conf = new JButton(confirmationString);
        conf.setActionCommand(confirmationString);
        conf.addActionListener(new confirmationListener());

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));

        buttonPane.add(conf);

        JScrollPane listScrollPane = new JScrollPane(list);
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    private static File getDirectory(JFrame frame){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int rv = fileChooser.showOpenDialog(frame);
        if (rv == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    class confirmationListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> paths = new ArrayList<>();
            for(TrackPath t : tracks){
                paths.add(t.path + "/" + t.filename);
            }
            switch(type){
                case COPY:
                    File dir = getDirectory(frame);
                    FileController.CopyFilesToFolder(paths, dir);

                    //https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    break;

                case DELETE:
                    FileController.DeleteFiles(paths);
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    break;

                case MAKE_PLAYLIST:
                    dir = getDirectory(frame);
                    try {
                        FileController.MakeM3UPlaylist(paths, dir, playlistName);
                    } catch (FileNotFoundException e1) {
                        JOptionPane.showMessageDialog(frame,
                                "Error couldn't create playlist: " + e1.getMessage(),
                                "error",
                                ERROR_MESSAGE );
                    }
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    break;

                default: break;
            }
            //System.out.println(ids.get(list.getSelectedIndex()));
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
    }

    static void CreateAndShowGui(PlaylistInteraction type,
                                 ArrayList<TrackPath> ptracks,
                                 String playlistName)
    {
        String title = "";
        switch(type){
            case COPY: title="Copy files to folder"; break;
            case DELETE: title="Delete files from device"; break;
            case MAKE_PLAYLIST: title="Export M3U playlist"; break;
            default: break;
        }
        JComponent newContentPane = new TrackDialog(title, type, ptracks, playlistName);
        newContentPane.setOpaque(true); //content panes must be opaque

        JFrame frame = new JFrame("Playlists");
        ((TrackDialog)newContentPane).frame = frame; //TODO: fix this hack
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }
}
