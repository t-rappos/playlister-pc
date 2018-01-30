package com.website.playlister;

import PlaylisterMain2.TrackPath;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Thomas Rappos (6336361) on 1/30/2018.
 */

//https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ListDemoProject/src/components/ListDemo.java
public  class PlaylistListDialog
        extends JPanel
        implements ListSelectionListener {

//    private ArrayList<Integer> ids;
    private ArrayList<String> names;
    private ArrayList<ArrayList<TrackPath>> tracks;
    private JList<String> list;

    private static final String deleteString = "Delete from device";
    private static final String copyString = "Copy files to folder";
    private static final String makePlaylistString = "Export playlist as M3U";

    private PlaylistListDialog(ArrayList<String> pnames, ArrayList<Integer> pids, ArrayList<ArrayList<TrackPath>> ptracks){
        super(new BorderLayout());
        //ids = pids;
        names = pnames;
        tracks = ptracks;

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for(String s : pnames){
            listModel.addElement(s);
        }

        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(10);

        JButton delete = new JButton(deleteString);
        delete.setActionCommand(deleteString);
        delete.addActionListener(new deleteListener());

        JButton copy = new JButton(copyString);
        copy.setActionCommand(copyString);
        copy.addActionListener(new copyListener());

        JButton makePlaylist = new JButton(makePlaylistString);
        makePlaylist.setActionCommand(makePlaylistString);
        makePlaylist.addActionListener(new makePlaylistListener());

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(makePlaylist);
        buttonPane.add(copy);
        buttonPane.add(delete);


        JScrollPane listScrollPane = new JScrollPane(list);
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    class deleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            TrackDialog.CreateAndShowGui(PlaylistInteraction.DELETE,
                    tracks.get(index),
                    names.get(index) );
        }
    }

    class makePlaylistListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //System.out.println(ids.get(list.getSelectedIndex()));
            int index = list.getSelectedIndex();
            TrackDialog.CreateAndShowGui(PlaylistInteraction.MAKE_PLAYLIST,
                    tracks.get(index),
                    names.get(index));
        }
    }

    class copyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //System.out.println(ids.get(list.getSelectedIndex()));
            int index = list.getSelectedIndex();
            TrackDialog.CreateAndShowGui(PlaylistInteraction.COPY,
                    tracks.get(index),
                    names.get(index));
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
    }

    static void CreateAndShowGui(ArrayList<String> playlistNames,
                                 ArrayList<Integer> playlistIds,
                                 ArrayList<ArrayList<TrackPath>> tracks)
    {
        JComponent newContentPane = new PlaylistListDialog(playlistNames, playlistIds, tracks);
        newContentPane.setOpaque(true); //content panes must be opaque

        JFrame frame = new JFrame("Playlists");
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }
}
