package PlaylisterMain2;

import java.io.File;

/**
 * Created by Thomas Rappos (6336361) on 12/29/2017.
 */
public interface IFilePropertyReader{
    void readFileProperties(File f);
    String getArtist();
    String getTitle();
    String getAlbum();
}
