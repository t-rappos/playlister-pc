package com.website.playlister;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Created by Thomas Rappos (6336361) on 12/21/2017.
 */
public class Track {
    public String filename = "";
    public String path = "";
    public String title = "";
    public String artist = "";
    public String album = "";
    public long filesize = 0;
    public String hash = "";

    //TODO: optimise this
    //https://www.mkyong.com/java/how-to-generate-a-file-checksum-value-in-java/
    private void generateHash(File f){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(f);
            byte[] dataBytes = new byte[1000];
            int nread = 0;

            int counter = 0;    //only reads the first 150*5 bytes
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
                if(counter++ > 5){
                    break;
                }
            };
            byte[] mdbytes = md.digest();
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            this.hash = sb.toString();
            System.out.println(this.hash);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFileProperties(File f){
        //http://www.javazoom.net/mp3spi/documents.html
        AudioFileFormat baseFileFormat = null;
        try {
            baseFileFormat = AudioSystem.getAudioFileFormat(f);
            if (baseFileFormat instanceof TAudioFileFormat)
            {
                Map properties = ((TAudioFileFormat)baseFileFormat).properties();
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
        }
    }

    Track(File f){
        filename = f.getName();
        path = f.getParent();
        filesize = f.length();
        generateHash(f);
        getFileProperties(f);
    }
}
