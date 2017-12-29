package PlaylisterMain2;


import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * Created by Thomas Rappos (6336361) on 12/29/2017.
 */

public class Track implements java.io.Serializable {

    public String filename = "";
    public String path = "";
    public String title = "";
    public String artist = "";
    public String album = "";
    public long filesize = 0;
    public String hash = "";
    //transient private IFilePropertyReader filePropertyReader;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (filesize != track.filesize) return false;
        if (!filename.equals(track.filename)) return false;
        if (!path.equals(track.path)) return false;
        if (title != null ? !title.equals(track.title) : track.title != null) return false;
        if (artist != null ? !artist.equals(track.artist) : track.artist != null) return false;
        if (album != null ? !album.equals(track.album) : track.album != null) return false;
        return hash.equals(track.hash);
    }

    @Override
    public int hashCode() {
        int result = filename.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + (int) (filesize ^ (filesize >>> 32));
        result = 31 * result + hash.hashCode();
        return result;
    }

    //TODO: optimise this
    //TODO: can we use the hashcode above?

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
            //System.out.println(this.hash);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFileProperties(File f, IFilePropertyReader reader){
        reader.readFileProperties(f);
        title = reader.getTitle();
        album = reader.getAlbum();
        artist = reader.getArtist();
    }

    public Track(File f, IFilePropertyReader reader){
        filename = f.getName();
        path = f.getParent();
        filesize = f.length();
        generateHash(f);
        getFileProperties(f, reader);
    }
}
