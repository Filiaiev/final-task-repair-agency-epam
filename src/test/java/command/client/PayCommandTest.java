package command.client;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.client.PayCommand;
import org.junit.*;
import org.junit.runners.MethodSorters;
import util.DBManagerUtil;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import static util.HttpMockUtil.*;
import static util.DBManagerUtil.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PayCommandTest {

    private static Client client;
    private static BigDecimal startCash;

    @BeforeClass
    public static void prepare() throws SQLException {
        // Mocking javax.servlet.http
        mockHttp();

        // Mocking DBManager
        mockDBManager();
    }

    @Before
    public void prepareMap(){
        resetAttributesMap();
        client = new ClientDAO().getClientById(1);

        when(getRequest().getParameter("orderId")).thenReturn("1");
        when(getSession().getAttribute("user")).thenReturn(new User());
        when(getSession().getAttribute("client")).thenReturn(client);
    }

    @Test
    public void test1payCommandTestFail() throws ServletException, IOException {
        String url = new PayCommand().execute(getRequest(), getResponse());
        Assert.assertTrue(url.contains("payment_not_enough_money"));
    }

    @Test
    public void test2payCommandTestSuccess() throws ServletException, IOException {
        new ClientDAO().setClientCashById(BigDecimal.valueOf(1000.0), 1);
        startCash = client.getCash();

        when(getSession().getAttribute("orderInfo")).thenReturn(new HashMap<String, String>());
        new PayCommand().execute(getRequest(), getResponse());
        Order order = new OrderDAO().getOrderByOrderId(1);
        Assert.assertEquals(order.getStatusId(), OrderStatus.PAID.ordinal());
    }

    @AfterClass
    public static void reset() throws SQLException {
        Connection con = DBManagerUtil.getDefaultConnection();
        con.createStatement().execute("UPDATE order_headers " +
                "SET status_id = " + OrderStatus.COMPLETED.ordinal());
        con.createStatement().execute("UPDATE clients " +
                "SET cash = " + startCash + " WHERE id = " + client.getId());
        resetDBManagerStaticMock();
    }

}
