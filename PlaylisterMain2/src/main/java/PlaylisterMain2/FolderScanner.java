package PlaylisterMain2;

import java.io.File;

/**
 * Created by Thomas Rappos (6336361) on 12/29/2017.
 */
public class FolderScanner {
    public static int countFolders(File folder){
        File[] files = folder.listFiles();
        int i = 1;
        for (File f: files){
            if(f.isDirectory()) {
                i += countFolders(f);
            }
        }
        return i;
    }
}
