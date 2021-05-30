package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.database.util.Field;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.util.*;
import java.util.Date;

public class OrderDAO {

    private static final String SQL__FIND_ORDERS_BY_CLIENT_ID =
            "SELECT * FROM order_headers WHERE client_id = ?;";

    private static final String SQL__GET_ORDERS_BY_REPAIRER_ID =
            "SELECT * FROM order_headers WHERE worker_id = ?;";

    private static final String SQL__GET_ORDERS_BY_STATUS =
            "SELECT * FROM order_headers WHERE status_id = ?;";

    private static final String SQL__GET_ALL_ORDERS = "SELECT * FROM order_headers;";

    private static final String SQL__GET_ORDER_BY_ORDER_ID =
            "SELECT * FROM order_headers WHERE id = ?;";

    private static final String SQL__GET_ORDERS_BY_PERSON_ID =
            "SELECT * FROM order_headers" +
            " JOIN clients ON clients.id = order_headers.client_id" +
            " WHERE person_id = ?;";

    private static final String SQL__UPDATE_COMMENT_BY_ORDER_ID =
            "UPDATE order_headers SET comment = ? WHERE id = ?;";

    private static final String SQL__UPDATE_STATUS_BY_ID =
            "UPDATE order_headers SET status_id = ? WHERE id = ?;";

    private static final String SQL__COMPLETE_ORDER_BY_ID =
            "UPDATE order_headers SET complete_date = NOW(), " +
                    "status_id =" + OrderStatus.COMPLETED.ordinal() + " WHERE id = ?;";

    private static final String SQL__SET_ORDER_COST_BY_ID =
            "UPDATE order_headers SET cost = ? WHERE id = ?;";

    private static final String SQL__SET_REPAIRER_BY_ORDER_ID =
            "UPDATE order_headers SET worker_id = ? WHERE id = ?;";

    private static final String SQL__GET_ORDER_INFO_BY_ORDER_ID =
            "WITH order_worker" +
                    "(id, client_id, order_date, complete_date, cost, comment, description," +
                    " worker_last_name, worker_first_name, worker_middle_name, status)" +
                    " as(" +
                    "SELECT" +
                    " oh.id, client_id, order_date, complete_date, cost, comment, description," +
                    " last_name, first_name, middle_name," +
                    " status_id" +
                    " FROM order_headers as oh" +
                    " LEFT JOIN employees as e" +
                    " ON e.id = oh.worker_id" +
                    " LEFT JOIN persons as p" +
                    " ON p.id = e.person_id" +
                    " WHERE oh.id = ?" +
                    " UNION" +
                    " SELECT" +
                    " oh.id, client_id, order_date, complete_date, cost, comment, description," +
                    " last_name, first_name, middle_name," +
                    " status_id" +
                    " FROM order_headers as oh" +
                    " RIGHT JOIN employees as e" +
                    " ON e.id = oh.worker_id" +
                    " RIGHT JOIN persons as p" +
                    " ON p.id = e.person_id" +
                    " WHERE oh.id = ?) " +

                    " SELECT" +
                    " ow.id, client_id," +
                    " p.last_name, p.first_name, p.middle_name," +
                    " order_date, complete_date, cost, comment, description," +
                    " worker_last_name, worker_first_name, worker_middle_name," +
                    " ow.status as status_id" +
                    " FROM order_worker as ow" +
                    " JOIN statuses as s" +
                    " ON s.id = ow.status " +
                    " JOIN clients as c" +
                    " ON c.id = client_id" +
                    " JOIN persons as p" +
                    " ON p.id = c.person_id;";

    public List<Order> getAllOrders(){
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            rs = con.createStatement().executeQuery(SQL__GET_ALL_ORDERS);
            OrderMapper orderMapper = new OrderMapper();
            while(rs.next()){
                orders.add(orderMapper.mapRow(rs));
            }
            rs.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return orders;
    }

    public List<Order> getSortedOrders(String field){
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try{
            con = DBManager.getInstance().getConnection();
            String query = "SELECT * FROM order_headers ORDER BY " + field + " DESC;";
            System.out.println("Query is = " + query);
            rs = con.createStatement().executeQuery(query);

            OrderMapper orderMapper = new OrderMapper();
            while(rs.next()){
                orders.add(orderMapper.mapRow(rs));
            }
            rs.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return orders;
    }

    public List<Order> getOrdersByStatus(OrderStatus status){
        List<Order> orders = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_ORDERS_BY_STATUS);
            ps.setInt(1, status.ordinal());
            rs = ps.executeQuery();
            OrderMapper orderMapper = new OrderMapper();
            while(rs.next()){
                orders.add(orderMapper.mapRow(rs));
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return orders;
    }

    public List<Order> getOrdersByRepairerId(int repairerId){
        List<Order> orders = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_ORDERS_BY_REPAIRER_ID);
            ps.setInt(1, repairerId);
            rs = ps.executeQuery();
            OrderMapper orderMapper = new OrderMapper();
            while(rs.next()){
                orders.add(orderMapper.mapRow(rs));
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return orders;
    }

    public List<Order> findOrdersByPersonId(int personId){
        List<Order> orders = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_ORDERS_BY_PERSON_ID);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            OrderMapper orderMapper = new OrderMapper();
            while(rs.next()){
                orders.add(orderMapper.mapRow(rs));
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
        }
        DBManager.getInstance().commitAndClose(con);
        return orders;
    }

    public Order getOrderByOrderId(int orderId){
        Order order = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_ORDER_BY_ORDER_ID);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            OrderMapper orderMapper = new OrderMapper();
            if(rs.next()){
                order = orderMapper.mapRow(rs);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
        }
        DBManager.getInstance().commitAndClose(con);
        return order;
    }

    public Map<String, String> getOrderInfoByOrderId(int orderId){
        Map<String, String> orderInfo = new HashMap<>();
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_ORDER_INFO_BY_ORDER_ID);
            ps.setInt(1, orderId);
            ps.setInt(2, orderId);
            rs = ps.executeQuery();
            if(rs.next()){
                orderInfo.put("id", rs.getString("id"));
                orderInfo.put("client_id", rs.getString("client_id"));
                orderInfo.put("client_last_name", rs.getString("last_name"));
                orderInfo.put("client_first_name", rs.getString("first_name"));
                orderInfo.put("client_middle_name", rs.getString("middle_name"));
                // TODO: DATE PARSE
//                DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, new Locale("UK", "ua"));
//                Date d = rs.getTimestamp("order_date");
//                orderInfo.put("order_date", df.format(d));
                orderInfo.put("order_date", rs.getString("order_date"));
                orderInfo.put("complete_date", rs.getString("complete_date"));
                orderInfo.put("cost", rs.getString("cost"));
                orderInfo.put("comment", rs.getString("comment"));
                orderInfo.put("description", rs.getString("description"));
                orderInfo.put("worker_last_name", rs.getString("worker_last_name"));
                orderInfo.put("worker_first_name", rs.getString("worker_first_name"));
                orderInfo.put("worker_middle_name", rs.getString("worker_middle_name"));
                orderInfo.put("status_id", rs.getString("status_id"));
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return orderInfo;
    }

    public void setOrderRepairerById(int repairerId, int orderId){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__SET_REPAIRER_BY_ORDER_ID);
            ps.setInt(1, repairerId);
            ps.setInt(2, orderId);
            ps.execute();

            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    public void updateStatusById(OrderStatus status, int id){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__UPDATE_STATUS_BY_ID);
            ps.setInt(1, status.ordinal());
            ps.setInt(2, id);
            ps.execute();

            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    public void completeOrderById(int orderId){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__COMPLETE_ORDER_BY_ID);
            ps.setInt(1, orderId);
            ps.execute();

            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    public void updateOrderCommentById(int id, String comment){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__UPDATE_COMMENT_BY_ORDER_ID);
            ps.setString(1, comment);
            ps.setInt(2, id);
            ps.execute();

            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    public void setOrderCostById(BigDecimal cost, int id){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__SET_ORDER_COST_BY_ID);
            ps.setBigDecimal(1, cost);
            ps.setInt(2, id);
            ps.execute();

            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    private static class OrderMapper implements EntityMapper<Order>{
        @Override
        public Order mapRow(ResultSet rs) {
            try{
                Order order = new Order();
                order.setId(rs.getInt(Field.ID));
                order.setClientId(rs.getInt(Field.ORDERS__CLIENT_ID));
                order.setWorkerId(rs.getInt(Field.ORDERS__WORKER_ID));
                order.setOrderDate(rs.getTimestamp(Field.ORDERS__ORDER_DATE));
                order.setCompleteDate(rs.getTimestamp(Field.ORDERS__COMPLETE_DATE));
                order.setCost(rs.getBigDecimal(Field.ORDERS__COST));
                order.setComment(rs.getString(Field.ORDERS__COMMENT));
                order.setDescription(rs.getString(Field.ORDERS__DESCRIPTION));
                order.setStatusId(rs.getInt(Field.ORDERS__STATUS_ID));
                return order;
            }catch (SQLException e){
                throw new IllegalStateException(e);
            }
        }
    }

}
