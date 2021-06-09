package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.database.util.Field;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class OrderDAO {

    private static Logger logger = Logger.getLogger(OrderDAO.class);

    private String locale;

    public OrderDAO(){
        locale = "uk";
    }

    public OrderDAO(String locale){
        this.locale = locale;
    }

    public static final String SQL__INSERT_ORDER = "INSERT INTO order_headers" +
            "(client_id, description) VALUES(?, ?)";

    private static final String SQL__GET_ALL_ORDERS =
            "SELECT oh.*, sl.status_name FROM order_headers as oh " +
            "JOIN statuses_localized as sl " +
            "ON sl.status_id = oh.status_id " +
            "JOIN locales as l " +
            "ON l.id = sl.locale_id " +
            "WHERE l.locale = ?";

    private static final String SQL__GET_ORDERS_COUNT = "SELECT COUNT(*) FROM order_headers";

    private static final String SQL__GET_ORDERS_BY_REPAIRER_ID =
            "SELECT oh.*, sl.status_name FROM order_headers as oh " +
            "JOIN statuses_localized as sl " +
            "ON sl.status_id = oh.status_id " +
            "JOIN locales as l " +
            "ON l.id = sl.locale_id " +
            "WHERE l.locale = ? AND oh.worker_id = ? ORDER BY oh.order_date DESC";

    private static final String SQL__GET_ORDERS_BY_STATUS =
            "SELECT oh.*, sl.status_name FROM order_headers as oh " +
                    "JOIN statuses_localized as sl " +
                    "ON sl.status_id = oh.status_id " +
                    "JOIN locales as l " +
                    "ON l.id = sl.locale_id " +
                    "WHERE l.locale = ? AND oh.status_id = ? ORDER BY oh.order_date DESC;";

    private static final String SQL__GET_ORDER_BY_ORDER_ID =
            "SELECT oh.*, sl.status_name FROM order_headers as oh " +
                    "JOIN statuses_localized as sl " +
                    "ON sl.status_id = oh.status_id " +
                    "JOIN locales as l " +
                    "ON l.id = sl.locale_id " +
                    "WHERE l.locale = ? AND oh.id = ?;";

    private static final String SQL__GET_ORDERS_BY_PERSON_ID =
            "SELECT oh.*, sl.status_name FROM order_headers as oh " +
            "JOIN statuses_localized as sl " +
            "ON sl.status_id = oh.status_id " +
            "JOIN locales as l " +
            "ON l.id = sl.locale_id " +
            "JOIN clients ON clients.id = oh.client_id " +
            "WHERE l.locale = ? AND person_id = ? ORDER BY oh.order_date DESC";

    private static final String SQL__UPDATE_COMMENT_BY_ORDER_ID =
            "UPDATE order_headers SET comment = ? WHERE id = ?;";

    private static final String SQL__SET_ORDER_COST_BY_ID =
            "UPDATE order_headers SET cost = ? WHERE id = ?;";

    private static final String SQL__SET_REPAIRER_BY_ORDER_ID =
            "UPDATE order_headers SET worker_id = ? WHERE id = ?;";

    private static final String SQL__GET_ORDER_INFO_BY_ORDER_ID =
            "SELECT oh.*, " +
            "sl.status_name, " +
            "pemp.last_name as worker_lname, " +
            "pemp.first_name as worker_fname, " +
            "pemp.middle_name as worker_mname, " +
            "pcli.last_name as client_lname, " +
            "pcli.first_name as client_fname, " +
            "pcli.middle_name as client_mname " +
            "FROM order_headers as oh " +
            "INNER JOIN clients as c " +
            "ON c.id = oh.client_id " +
            "INNER JOIN persons as pcli " +
            "ON pcli.id = c.person_id " +
            "LEFT JOIN employees as e " +
            "ON e.id = oh.worker_id " +
            "LEFT JOIN persons as pemp " +
            "ON e.person_id = pemp.id " +
            "JOIN statuses_localized as sl " +
            "ON sl.status_id = oh.status_id " +
            "JOIN locales as l " +
            "ON l.id = sl.locale_id " +
            "WHERE oh.id = ? AND l.locale = ?;";

    public List<Order> getOrders(Map<String, String> filters, String sortField,
                                 String ordering, int start, int offset){
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        String query = null;
        PreparedStatement ps = null;

        if(start == 0 && offset == 0){
            query = SQL__GET_ALL_ORDERS + getFilteringPart(filters, sortField)
                    + getOrderingPart(sortField, ordering);
        }else{
            query = SQL__GET_ALL_ORDERS
                    + getFilteringPart(filters, sortField)
                    + getOrderingPart(sortField, ordering)
                    + " LIMIT " + start + ", " + offset;
        }
        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, locale);

            rs = ps.executeQuery();
            OrderMapper orderMapper = new OrderMapper();
            while(rs.next()){
                orders.add(orderMapper.mapRow(rs));
            }
            rs.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get orders", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return orders;
    }

    public Integer getOrdersCountByField(String field, String value){
        Integer count = null;
        Connection con = null;
        ResultSet rs = null;
        String query = (field == null && value == null) ? SQL__GET_ORDERS_COUNT :
                (SQL__GET_ORDERS_COUNT + " WHERE " + field + " = " + "'" + value + "'");
        try{
            con = DBManager.getInstance().getConnection();
            rs = con.createStatement().executeQuery(query);
            if(rs.next()){
                count = rs.getInt(1);
            }
            rs.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get orders count by field '" + field + "' and value '"
                         + value + "'", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return count;
    }

    public List<Order> getOrdersByStatus(OrderStatus status){
        List<Order> orders = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_ORDERS_BY_STATUS);
            ps.setString(1, locale);
            ps.setInt(2, status.ordinal());
            rs = ps.executeQuery();
            OrderMapper orderMapper = new OrderMapper();
            while(rs.next()){
                orders.add(orderMapper.mapRow(rs));
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get order by status '" + status + "'", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return orders;
    }

    public List<Order> getOrdersByRepairerId(int repairerId, int start, int offset){
        List<Order> orders = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        String query = null;
        try{
            con = DBManager.getInstance().getConnection();
            if(start == 0 && offset == 0){
                query = SQL__GET_ORDERS_BY_REPAIRER_ID;
            }else{
                query = SQL__GET_ORDERS_BY_REPAIRER_ID + " LIMIT " + start + ", " + offset;
            }
            ps = con.prepareStatement(query);
            con = DBManager.getInstance().getConnection();
            ps.setString(1, locale);
            ps.setInt(2, repairerId);
            rs = ps.executeQuery();
            OrderMapper orderMapper = new OrderMapper();
            while(rs.next()){
                orders.add(orderMapper.mapRow(rs));
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get orders by repairer id = " + repairerId, e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return orders;
    }

    public List<Order> getOrdersByPersonId(int personId, int start, int offset){
        List<Order> orders = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        String query = null;
        try{
            con = DBManager.getInstance().getConnection();
            if(start == 0 && offset == 0){
                query = SQL__GET_ORDERS_BY_PERSON_ID;
            }else{
                query = SQL__GET_ORDERS_BY_PERSON_ID + " LIMIT " + start + ", " + offset;
            }
            ps = con.prepareStatement(query);
            con = DBManager.getInstance().getConnection();
            ps.setString(1, locale);
            ps.setInt(2, personId);
            rs = ps.executeQuery();
            OrderMapper orderMapper = new OrderMapper();
            while(rs.next()){
                orders.add(orderMapper.mapRow(rs));
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get orders be person id = " + personId, e);
            return null;
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
            ps.setString(1, locale);
            ps.setInt(2, orderId);
            rs = ps.executeQuery();
            OrderMapper orderMapper = new OrderMapper();
            if(rs.next()){
                order = orderMapper.mapRow(rs);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get order by order id = " + orderId, e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return order;
    }

    public Integer insertOrderByClientId(int clientId, String orderText) {
        PreparedStatement ps = null;
        Connection con = null;
        Integer generatedId = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, clientId);
            ps.setString(2, orderText);

            ps.execute();
            ResultSet genKeys = ps.getGeneratedKeys();
            if(genKeys.next()){
                generatedId = genKeys.getInt(1);
            }
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot insert order by client id = " + clientId, e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return generatedId;
    }

    public Map<String, String> getOrderInfoByOrderId(int orderId){
        Map<String, String> orderInfo = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_ORDER_INFO_BY_ORDER_ID);
            ps.setInt(1, orderId);
            ps.setString(2, locale);
            rs = ps.executeQuery();
            if(rs.next()){
                orderInfo = new HashMap<>();
                orderInfo.put("id", rs.getString("id"));
                orderInfo.put("clientId", rs.getString("client_id"));
                orderInfo.put("workerId", rs.getString("worker_id"));
                orderInfo.put("clientFirstName", rs.getString("client_fname"));
                orderInfo.put("clientLastName", rs.getString("client_lname"));
                orderInfo.put("clientMiddleName", rs.getString("client_mname"));
                orderInfo.put("orderDate", rs.getString("order_date"));
                orderInfo.put("completeDate", rs.getString("complete_date"));
                orderInfo.put("cost", rs.getString("cost"));
                orderInfo.put("comment", rs.getString("comment"));
                orderInfo.put("description", rs.getString("description"));
                orderInfo.put("workerFirstName", rs.getString("worker_fname"));
                orderInfo.put("workerLastName", rs.getString("worker_lname"));
                orderInfo.put("workerMiddleName", rs.getString("worker_mname"));
                orderInfo.put("statusId", rs.getString("status_id"));
                orderInfo.put("statusName", rs.getString("status_name"));
                System.out.println(orderInfo);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get order info by order id = " + orderId, e);
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
            logger.error("Cannot set order repairer by rep. id = " + repairerId
                        + " order id = " + orderId, e);
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    public void updateStatusById(OrderStatus status, int orderId){
        PreparedStatement ps = null;
        Connection con = null;
        StringBuilder query = new StringBuilder("UPDATE order_headers SET status_id = ?");
        try{
            con = DBManager.getInstance().getConnection();
            if(status == OrderStatus.COMPLETED){
                query.append(", complete_date = NOW()");
            }
            query.append(" WHERE id = ?");
            ps = con.prepareStatement(query.toString());
            ps.setInt(1, status.ordinal());
            ps.setInt(2, orderId);

            ps.execute();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot update status '" + status + "' for order id = " + orderId, e);
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    public void updateOrderCommentById(int orderId, String comment){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__UPDATE_COMMENT_BY_ORDER_ID);
            ps.setString(1, comment);
            ps.setInt(2, orderId);
            ps.execute();

            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot update comment for order id = " + orderId, e);
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    public void setOrderCostById(BigDecimal cost, int orderId){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__SET_ORDER_COST_BY_ID);
            ps.setBigDecimal(1, cost);
            ps.setInt(2, orderId);
            ps.execute();

            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot set order cost = " + cost + " for order id = " + orderId, e);
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    private static String getFilteringPart(Map<String, String> filters, String sortField){
        if((filters == null || filters.size() == 0) && sortField == null){
            return "";
        }else if((filters == null || filters.size() == 0)){
            return " AND oh." + sortField + " IS NOT NULL";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = filters.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<String, String> e = it.next();
            sb.append(" AND ");
            sb.append("oh.").append(e.getKey()).append("=").append(e.getValue());
        }
        return sb.toString();
    }

    private static String getOrderingPart(String sortField, String desc){
        if(sortField == null){
            return " ORDER BY oh.order_date DESC";
        }

        return " ORDER BY oh." + sortField + " " + Objects.toString(desc, "");
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
                order.setStatusName(rs.getString(Field.ORDERS__STATUS_NAME));
                return order;
            }catch (SQLException e){
                logger.error("Cannot map order by given ResultSet", e);
                return null;
            }
        }
    }

}
