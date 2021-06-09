package command.order;

import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.web.command.order.GetOrdersCommand;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static util.HttpMockUtil.*;
import static util.DBManagerUtil.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GetOrdersTest {

    private static Person personDummy = new Person();
    private static List<Order> expectedOrders = new ArrayList<>();

    @BeforeClass
    public static void prepare() throws SQLException {
        // Mocking javax.servlet.http
        mockHttp();

        // Mocking DBManager
        mockDBManager();

        // Preparing order
        Order orderExpected = new Order();
        orderExpected.setId(1);
        orderExpected.setClientId(1);
        orderExpected.setWorkerId(2);
        orderExpected.setOrderDate(Timestamp.valueOf("2021-06-05 17:37:00"));
        orderExpected.setCompleteDate(Timestamp.valueOf("2021-06-08 17:37:00"));
        orderExpected.setCost(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING));
        orderExpected.setComment("Thanks!");
        orderExpected.setDescription("Test order");
        orderExpected.setStatusId(5);

        expectedOrders.add(orderExpected);
    }

    @Before
    public void prepareMap(){
        resetAttributesMap();
        when(getRequest().getParameter("page")).thenReturn(null);
        when(getSession().getAttribute("lang")).thenReturn("uk");

        when(getSession().getAttribute("person")).thenReturn(personDummy);
    }

    @Test
    public void test1GetOrdersClientTest() throws ServletException, IOException {
        when(getSession().getAttribute("role")).thenReturn(Role.CLIENT);
        personDummy.setId(1);
        new GetOrdersCommand().execute(getRequest(), getResponse());

        Assert.assertEquals(expectedOrders, getAttributesMap().get("orders"));
    }

    @Test
    public void test2GetOrdersManagerFilteredTest() throws ServletException, IOException {
        when(getSession().getAttribute("role")).thenReturn(Role.MANAGER);
        personDummy.setId(2);
        when(getRequest().getParameter("resetFilter")).thenReturn("false");
        when(getRequest().getParameterNames())
                .thenReturn(Collections.enumeration(Arrays.asList("statusId")));
        when(getRequest().getParameter("statusId")).thenReturn("5");
        when(getRequest().getParameter("sortBy")).thenReturn(null);
        when(getRequest().getParameter("ordering")).thenReturn(null);

        new GetOrdersCommand().execute(getRequest(), getResponse());
        Assert.assertEquals(expectedOrders, getAttributesMap().get("orders"));
    }

    @Test
    public void test3GetOrdersRepairerTest() throws ServletException, IOException {
        when(getSession().getAttribute("role")).thenReturn(Role.REPAIRER);
        personDummy.setId(3);

        new GetOrdersCommand().execute(getRequest(), getResponse());
        Assert.assertEquals(expectedOrders, getAttributesMap().get("orders"));
    }

    @AfterClass
    public static void clear(){
        resetDBManagerStaticMock();
    }
}
