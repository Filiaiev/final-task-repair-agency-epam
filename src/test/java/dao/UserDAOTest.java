package dao;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.database.exception.InsertingDuplicateException;
import com.filiaiev.agency.database.util.DBUtil;
import com.filiaiev.agency.entity.User;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.*;
import util.DBManagerUtil;

import java.io.IOException;
import java.sql.*;
import static util.DBManagerUtil.mockDBManager;
import static util.DBManagerUtil.resetDBManagerStaticMock;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserDAOTest {

    private static Integer userId;
    private static User userExpected;

    @BeforeClass
    public static void prepare() throws SQLException {
        // Expected user for tests
        userExpected = new User();
        userExpected.setId(1);
        userExpected.setEmail("furna@gmail.com");
        userExpected.setLogin("furna");
        userExpected.setPass(DBUtil.hash("123321"));
        userExpected.setRoleId(2);

        // Preparing mocked DBManager class
        mockDBManager();
    }

    @Test
    public void test1InsertAndReadUser() throws InsertingDuplicateException {
        userId = new UserDAO().insertUser("choppy@gmail.com", "choppy", "123456", 2);
        User user = new UserDAO().getUserById(userId);
        Assert.assertNotNull(user);
    }

    @Test
    public void test3GetUserByEmail(){
        User userActual = new UserDAO().getUserByEmail("furna@gmail.com");
        Assert.assertEquals(userExpected, userActual);
    }

    @Test
    public void test4GetUserByLogin(){
        User userActual = new UserDAO().getUserByLogin("furna");
        Assert.assertEquals(userExpected, userActual);
    }

    @Test
    public void test5GetUserByOrderClientId() {
        User userActual = new UserDAO().getUserByOrderClientId(1);
        Assert.assertEquals(userExpected, userActual);
    }

    @AfterClass
    public static void clear() throws SQLException {
        Connection con = DriverManager.getConnection(DBManagerUtil.getProperties().getProperty("url"),
                DBManagerUtil.getProperties());
        con.createStatement().execute("DELETE FROM users WHERE id = " + userId);
        resetDBManagerStaticMock();
    }
}
