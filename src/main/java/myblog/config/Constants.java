package myblog.config;

/**
 * Constants Variable
 * @author FANG
 */
public class Constants {
    public static String FILE_UPLOAD_DICTIONARY = null;
    static {
        FILE_UPLOAD_DICTIONARY = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\admin\\coverImg";
    }
}
