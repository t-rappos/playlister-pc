package PlaylisterMain2;

/**
 * Created by Thomas Rappos (6336361) on 12/29/2017.
 */
public interface IUserManager {

    void saveCredentials(String username, String password);

    void saveEmail(String email);

    String getUsername();
    String getPassword();
    String getEmail();

    //return -1 if no id was found
    int getDeviceId();

    boolean hasDeviceId();

    void saveDeviceId(int deviceId);

    long getServerDBResetId();

    void saveServerDBResetId(long id);
}
