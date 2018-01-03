package com.website.playlister;

import PlaylisterMain2.IFilePropertyReader;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Thomas Rappos (6336361) on 1/3/2018.
 */
public class FileID3Reader implements IFilePropertyReader {
    String artist = "";
    String album = "";
    String title = "";

    @Override
    public void readFileProperties(File f) {
        try {
            Mp3File mp3file = new Mp3File(f);
            //System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
            //System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
            //System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
            //System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
            //System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
            //System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }
    }

    @Override public String getArtist(){
        return artist;
    }
    @Override public String getTitle(){
        return title;
    }
    @Override public String getAlbum(){
        return album;
    }
}
