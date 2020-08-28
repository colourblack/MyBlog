package myblog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
public class dataSourceTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void test01() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection != null);
        assert connection != null;
        connection.close();
    }

}
