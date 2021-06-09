package command;

import com.filiaiev.agency.entity.Entity;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.auth.LogInCommand;
import com.sun.org.apache.xml.internal.security.Init;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import util.HttpMockUtil;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static util.DBManagerUtil.*;
import static util.HttpMockUtil.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginCommandTest {

    @BeforeClass
    public static void prepare() throws SQLException {
        // Mocking javax.servlet.http
        mockHttp();

        // Preparing mocked DBManager class
        mockDBManager();
    }

    @Before
    public void prepareMap(){
        resetAttributesMap();
    }

    @Test
    public void test1ManagerLogIn() throws ServletException, IOException {
        when(getRequest().getParameter("userLogin")).thenReturn("manager");
        when(getRequest().getParameter("userPass")).thenReturn("5554444");
        new LogInCommand().execute(getRequest(), getResponse());
        Assert.assertEquals(2, ((User)getAttributesMap().get("user")).getId());
    }

    @Test
    public void test2ClientLogin() throws ServletException, IOException {
        when(getRequest().getParameter("userLogin")).thenReturn("furna");
        when(getRequest().getParameter("userPass")).thenReturn("123321");
        new LogInCommand().execute(getRequest(), getResponse());
        Assert.assertEquals(1, ((User)getAttributesMap().get("user")).getId());
    }

    @Test
    public void test3RepairerLogin() throws ServletException, IOException {
        when(getRequest().getParameter("userLogin")).thenReturn("repairer");
        when(getRequest().getParameter("userPass")).thenReturn("777888");
        new LogInCommand().execute(getRequest(), getResponse());
        Assert.assertEquals(3, ((User)getAttributesMap().get("user")).getId());
    }

    @AfterClass
    public static void clear(){
        resetDBManagerStaticMock();
    }
}
