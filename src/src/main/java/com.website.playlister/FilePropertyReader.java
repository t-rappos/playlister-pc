package com.website.playlister;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import PlaylisterMain2.IFilePropertyReader;
/**
 * Created by Thomas Rappos (6336361) on 12/29/2017.
 */
public class FilePropertyReader implements  IFilePropertyReader{

    String artist = "";
    String album = "";
    String title = "";

    public String getArtist(){
        return artist;
    }
    public String getTitle(){
        return title;
    }
    public String getAlbum(){
        return album;
    }

    public void readFileProperties(File f){
        //http://www.javazoom.net/mp3spi/documents.html
        AudioFileFormat baseFileFormat = null;
        try {
            baseFileFormat = AudioSystem.getAudioFileFormat(f);
            if (baseFileFormat instanceof TAudioFileFormat)
            {
                Map properties = baseFileFormat.properties();
                artist = (String) properties.get("author");
                if(artist == null || artist.compareTo("")==0){
                    artist = (String) properties.get("artist");
                    if(artist == null || artist.compareTo("")==0){
                        artist = (String) properties.get("albumartist");
                    }
                }
                album = (String) properties.get("album");
                title = (String) properties.get("title");
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
