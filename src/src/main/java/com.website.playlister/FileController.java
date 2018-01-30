package com.website.playlister;
import PlaylisterMain2.TrackCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.ArrayList;
import static java.nio.file.StandardCopyOption.*;

/**
 * Created by Thomas Rappos (6336361) on 1/30/2018.
 */
class FileController {

    static void DeleteFiles(ArrayList<String> paths){
        for(String spath : paths){
            Path path = Paths.get(spath);
            try {
                Files.delete(path);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void CopyFilesToFolder(ArrayList<String> paths, File folder){
        for(String spath : paths){
            Path path = Paths.get(spath);
            try{
                if(folder.isDirectory() && path.toFile().isFile()){
                    Files.copy(path, Paths.get(folder.toPath().toString() +"/"+ path.getFileName().toString()), REPLACE_EXISTING);
                }
            } catch( Exception e){
                System.out.println(e.getMessage());
                System.out.println(path);
            }
        }

    }

    //https://stackoverflow.com/questions/1053467/how-do-i-save-a-string-to-a-text-file-using-java
    static void MakeM3UPlaylist(ArrayList<String> paths, File DestFolder, String playlistName) throws FileNotFoundException {
        String destPath = DestFolder.getPath() + "/" + playlistName + ".m3u";
        try(  PrintWriter out = new PrintWriter( destPath )  ){
            for( String path : paths){
                out.println(path);
            }
        }
    }

}
