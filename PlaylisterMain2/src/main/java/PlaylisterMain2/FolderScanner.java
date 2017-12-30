package PlaylisterMain2;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Thomas Rappos (6336361) on 12/29/2017.
 */


public class FolderScanner {
    public static int countFolders(File folder){
        File[] files = folder.listFiles();
        if(files == null || files.length == 0){return 0;}
        int i = 1;
        for (File f: files){
            if(f.isDirectory()) {
                i += countFolders(f);
            }
        }
        return i;
    }
    public static int countFolders(ArrayList<File> files){
        long startTime = System.nanoTime();
        int res = 0;
        for(File f : files){
            res += countFolders(f);
        }
        long dt = System.nanoTime() - startTime;
        System.out.println(res + " folders, completed in " + (float)dt/1000000f + " ms");
        return res;
    }
}
