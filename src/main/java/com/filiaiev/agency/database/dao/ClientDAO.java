package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.entity.Client;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO {

    private static Logger logger = Logger.getLogger(ClientDAO.class);

    private static final String SQL__GET_CLIENT_BY_PERSON_ID = "SELECT * FROM clients" +
            " WHERE person_id = ?;";

    private static final String SQL__GET_CLIENT_BY_ID = "SELECT * FROM clients WHERE id = ?;";

    private static final String SQL__GET_CLIENT_CASH_BY_CLIENT_ID = "SELECT cash FROM clients" +
            " WHERE id = ?;";

    private static final String SQL__SET_CLIENT_CASH_BY_ID = "UPDATE clients" +
            " SET cash = ? WHERE id = ?;";

    public static final String SQL__INSERT_CLIENT = "INSERT INTO clients(person_id)" +
            " VALUES(?);";

    public boolean insertClient(int personId){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__INSERT_CLIENT);

            ps.setInt(1, personId);

            ps.execute();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot insert client", e);
            return false;
        }
        DBManager.getInstance().commitAndClose(con);
        return true;
    }

    public Client getClientByPersonId(int personId){
        Client client = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_CLIENT_BY_PERSON_ID);
            ps.setInt(1, personId);
            rs = ps.executeQuery();

            if(rs.next()){
                client = new ClientMapper().mapRow(rs);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get client by person id", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return client;
    }

    public Client getClientById(int clientId){
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        Client client = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_CLIENT_BY_ID);
            ps.setInt(1, clientId);
            rs = ps.executeQuery();

            if(rs.next()){
                client = new ClientMapper().mapRow(rs);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get client by id", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return client;
    }

    public BigDecimal getClientCashByClientId(int clientId){
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        BigDecimal cash = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_CLIENT_CASH_BY_CLIENT_ID);
            ps.setInt(1, clientId);
            rs = ps.executeQuery();

            if(rs.next()){
                cash = rs.getBigDecimal(1);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get client cash", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return cash;
    }

    public void setClientCashById(BigDecimal newCash, int clientId){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__SET_CLIENT_CASH_BY_ID);
            ps.setBigDecimal(1, newCash);
            ps.setInt(2, clientId);
            ps.execute();

            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot set client cash", e);
            return;
        }
        DBManager.getInstance().commitAndClose(con);
    }

    private static class ClientMapper implements EntityMapper<Client> {
        @Override
        public Client mapRow(ResultSet rs) {
            try{
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setCash(rs.getBigDecimal("cash"));
                client.setPersonId(rs.getInt("person_id"));
                return client;
            }catch (SQLException e){
                logger.error("Cannot map client with given ResultSet", e);
                return null;
            }
        }
    }
}
