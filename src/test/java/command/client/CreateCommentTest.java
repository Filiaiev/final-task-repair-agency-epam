package command.client;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.client.CreateCommentCommand;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

import static util.HttpMockUtil.*;
import static util.DBManagerUtil.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateCommentTest {

    private static OrderDAO orderDAO = new OrderDAO();

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
    public void createCommentTest() throws ServletException, IOException {
        String comment = "Comment command test";
        when(getRequest().getParameter("commentText")).thenReturn(comment);
        when(getRequest().getParameter("orderId")).thenReturn("1");

        Client mockedClient = new Client();
        mockedClient.setId(1);
        mockedClient.setPersonId(1);

        when(getSession().getAttribute("client")).thenReturn(mockedClient);
        when(getSession().getAttribute("user")).thenReturn(new User());
        new CreateCommentCommand().execute(getRequest(), getResponse());

        Order order = orderDAO.getOrderByOrderId(1);
        Assert.assertEquals(comment, order.getComment());
    }

    @AfterClass
    public static void reset(){
        orderDAO.updateOrderCommentById(1, "Thanks!");
        resetDBManagerStaticMock();
    }

}
