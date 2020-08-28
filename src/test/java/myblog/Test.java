package myblog;

import lombok.extern.slf4j.Slf4j;
import myblog.config.Constants;

import java.io.File;
import java.net.URI;

@Slf4j(topic = "directory")
public class Test {

    @org.junit.Test
    public void test() {
        log.debug(
                Constants.FILE_UPLOAD_DICTIONARY
        );
        File file = new File(Constants.FILE_UPLOAD_DICTIONARY);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            log.debug(mkdirs + "");
        } else {
            log.debug(file.getPath());
            file = null;
        }
    }

    @org.junit.Test
    public void testURI(){
        URI uri = URI.create("http://localhost:8888");
        log.info("uri host: {}", uri.getHost());
        log.info("uri port: {}", uri.getPort());
        log.info("uri path: {}", uri.getPath());
        log.info("uri: {}", uri.toString());
        String fileName = "abc.txt";
        log.info("suffix : {}", fileName.substring(fileName.lastIndexOf(".")));
    }

}
