package util;

import com.filiaiev.agency.database.DBManager;
import org.mockito.MockedStatic;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doCallRealMethod;

public class DBManagerUtil {

    private static Properties properties;
    private static MockedStatic<DBManager> mockedStatic;

    static{
        properties = new Properties();
        try {
            properties.load(new FileReader("src/test/resources/db_test.properties"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            properties = null;
        }
    }

    public static Properties getProperties() {
        return properties;
    }

    private static Connection prepareConnection() throws SQLException {
        Connection con = DriverManager.getConnection(properties.getProperty("url"), properties);
        con.setAutoCommit(false);
        con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return con;
    }

    public static Connection getDefaultConnection() throws SQLException {
        return DriverManager.getConnection(properties.getProperty("url"), properties);
    }

    public static void mockDBManager() throws SQLException {
        mockedStatic = mockStatic(DBManager.class);
        DBManager dbManagerMock = mock(DBManager.class);
        mockedStatic.when(DBManager::getInstance).thenReturn(dbManagerMock);

        // Getting new connection on every new call to DBManager#getConnection()
        when(dbManagerMock.getConnection()).thenAnswer(invocationOnMock -> prepareConnection());
        doCallRealMethod().when(dbManagerMock).commitAndClose(any(Connection.class));
        doCallRealMethod().when(dbManagerMock).rollbackAndClose(any(Connection.class));
    }

    public static void resetDBManagerStaticMock(){
        mockedStatic.close();
    }
}
