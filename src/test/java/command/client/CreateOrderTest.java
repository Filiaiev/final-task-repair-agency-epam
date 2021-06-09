package command.client;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.client.CreateOrderCommand;
import org.junit.*;
import org.junit.runners.MethodSorters;
import util.DBManagerUtil;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static util.HttpMockUtil.*;
import static util.DBManagerUtil.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateOrderTest {

    private static OrderDAO orderDAO = new OrderDAO();
    private static Order insertedOrder;

    @BeforeClass
    public static void prepare() throws SQLException {
        // Mocking javax.http.servlet
        mockHttp();

        // Preparing mocked DBManager class
        mockDBManager();
    }

    @Before
    public void prepareMap() {
        resetAttributesMap();
    }

    @Test
    public void createOrderTest() throws ServletException, IOException {
        String orderText = "Created in test";
        when(getRequest().getParameter("orderText")).thenReturn(orderText);
        when(getSession().getAttribute("user")).thenReturn(new User());

        Client mockedClient = new Client();
        mockedClient.setId(1);

        when(getSession().getAttribute("client")).thenReturn(mockedClient);
        new CreateOrderCommand().execute(getRequest(), getResponse());

        List<Order> clientsOrders = orderDAO.getOrdersByPersonId(1, 0, 0);

        // Taking first element, because orders are getting sorted by order date by default
        insertedOrder = clientsOrders.get(0);
        Assert.assertEquals(orderText, insertedOrder.getDescription());
    }

    @AfterClass
    public static void clear() throws SQLException {
        Connection con = DBManagerUtil.getDefaultConnection();
        con.createStatement().execute("DELETE FROM order_headers WHERE id = "
                + insertedOrder.getId());
        resetDBManagerStaticMock();
    }
}
