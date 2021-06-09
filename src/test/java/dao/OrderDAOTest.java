package dao;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.database.exception.InsertingDuplicateException;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import org.junit.*;
import org.junit.runners.MethodSorters;
import util.DBManagerUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static util.DBManagerUtil.mockDBManager;
import static util.DBManagerUtil.resetDBManagerStaticMock;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderDAOTest {

    private static Order orderExpected;
    private static int insertedOrder;
    private static OrderDAO orderDAO = new OrderDAO();
    private static List<Order> orders = new ArrayList<>();

    @BeforeClass
    public static void prepare() throws SQLException{
        // Expected order for tests
        orderExpected = new Order();
        orderExpected.setId(1);
        orderExpected.setClientId(1);
        orderExpected.setWorkerId(2);
        orderExpected.setOrderDate(Timestamp.valueOf("2021-06-05 17:37:00"));
        orderExpected.setCompleteDate(Timestamp.valueOf("2021-06-08 17:37:00"));
        orderExpected.setCost(BigDecimal.valueOf(500).setScale(2, RoundingMode.CEILING));
        orderExpected.setDescription("Test order");
        orderExpected.setComment("Thanks!");
        orderExpected.setStatusId(5);

        // Preparing mocked DBManager class
        mockDBManager();
    }

    @Test
    public void test1GetOrderByOrderId(){
        Order orderActual = orderDAO.getOrderByOrderId(1);
        Assert.assertEquals(orderExpected, orderActual);
    }

    @Test
    public void test2InsertAndGetOrderTest(){
        insertedOrder = orderDAO.insertOrderByClientId(1, "I`m inserted");
        Order order = orderDAO.getOrderByOrderId(insertedOrder);
        orders.add(order);
        Assert.assertNotNull(order);
    }

    @Test
    public void test3GetOrders(){
        orders.add(orderExpected);
        List<Order> ordersActual = orderDAO.getOrders(null, null, null, 0, 0);
        Assert.assertEquals(orders, ordersActual);
    }

    @Test
    public void test4GetOrdersByStatus(){
        List<Order> ordersActual = orderDAO.getOrdersByStatus(OrderStatus.COMPLETED);
        List<Order> ordersExpected = new ArrayList<>();
        ordersExpected.add(orderExpected);
        Assert.assertEquals(ordersExpected, ordersActual);
    }

    @Test
    public void test5GetOrdersCountByField(){
        Integer countActual = orderDAO.getOrdersCountByField("description", "Test order");
        Assert.assertEquals(new Integer(1), countActual);
    }

    @Test
    public void test6GetOrdersByRepairerId(){
        List<Order> ordersActual = orderDAO.getOrdersByRepairerId(2, 0 ,0);
        List<Order> ordersExpected = new ArrayList<>();
        ordersExpected.add(orderExpected);
        Assert.assertEquals(ordersExpected, ordersActual);
    }

    @Test
    public void test7UpdateStatusByOrderId(){
        orderDAO.updateStatusById(OrderStatus.PAID, 1);
        Order order = orderDAO.getOrderByOrderId(1);
        Assert.assertEquals(OrderStatus.PAID.ordinal(), order.getStatusId());
    }

    @Test
    public void test8UpdateCommentByOrderId(){
        orderDAO.updateOrderCommentById(1, "Updated comment");
        Order order = orderDAO.getOrderByOrderId(1);
        Assert.assertEquals("Updated comment", order.getComment());
    }

    @Test
    public void test9SetOrderRepairerById(){
        orderDAO.setOrderRepairerById(1, 1);
        Order order = orderDAO.getOrderByOrderId(1);
        Assert.assertEquals(new Integer(1), order.getWorkerId());
    }

    @AfterClass
    public static void clear() throws SQLException {
        Connection con = DBManagerUtil.getDefaultConnection();
        con.createStatement().execute("DELETE FROM order_headers WHERE id = " + insertedOrder);
        con.createStatement().execute("UPDATE order_headers SET status_id = 5," +
                " comment = 'Thanks!', worker_id = 2 WHERE id = 1;");
        resetDBManagerStaticMock();
    }


}
