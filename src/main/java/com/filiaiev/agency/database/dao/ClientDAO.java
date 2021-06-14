package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.database.exception.InsertingDuplicateException;
import com.filiaiev.agency.entity.Client;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;

public class ClientDAO{

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

    private static final String SQL__UPDATE_PREFERRED_LOCALE_BY_ID =
            "UPDATE clients SET preferred_locale = ? WHERE id = ?;";

    /**
     * Inserting new client to the 'clients' table
     *
     * @param personId an id of person to be inserted in 'clients' table
     * @return an id of inserted client
    */
    public Integer insertClient(int personId) throws InsertingDuplicateException{
        PreparedStatement ps = null;
        Connection con = null;
        Integer clientId = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__INSERT_CLIENT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, personId);

            ps.execute();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    clientId = rs.getInt(1);
                }
            }
            ps.close();
        }catch (SQLIntegrityConstraintViolationException e){
            DBManager.getInstance().rollbackAndClose(con);
            throw new InsertingDuplicateException("Client with person_id #" + personId
                    + " already exists", e);
        }
        catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot insert client", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return clientId;
    }

    /**
     * Getting client instance from the 'clients' table
     *
     * @param personId an id of person belongs to the client
     * @return Client instance
    */
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

    /**
     * Getting client instance from the 'clients' table
     *
     * @param clientId an id of client to return
     * @return Client instance
     */
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

    /**
     * Getting client`s cash by client id
     *
     * @param clientId an id of client whose cash to be returned
     * @return client`s cash
     */
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

    /**
     * Setting client`s cash by client id
     *
     * @param newCash cash value to replace old
     * @param clientId an id of client whose cash to be setted
     */
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

    /**
     * Updating client`s locale to use it in
     * email message localization
     *
     * @see com.filiaiev.agency.mail.MailSender
     * @param clientId an id of client whose locale updates
     * @param locale locale name
     */
    public void updatePreferredLocaleById(int clientId, String locale){
        PreparedStatement ps = null;
        Connection con = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__UPDATE_PREFERRED_LOCALE_BY_ID);
            ps.setString(1, locale);
            ps.setInt(2, clientId);
            ps.execute();

            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot update to locale --> " + locale, e);
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
                client.setPreferredLocale(rs.getString("preferred_locale"));
                return client;
            }catch (SQLException e){
                logger.error("Cannot map client with given ResultSet", e);
                return null;
            }
        }
    }
}
