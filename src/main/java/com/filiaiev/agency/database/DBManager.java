package com.filiaiev.agency.database;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {

    private static DBManager instance;
    private static Logger logger = Logger.getLogger(DBManager.class);

    private DBManager(){}

    public static synchronized DBManager getInstance(){
        if(instance == null){
            logger.debug("DBManager has been instantiated, DataSource loaded");
            instance = new DBManager();
        }
        return instance;
    }

    /**
    * Getting connection from the DataSource
     * received from the Context
     *
     * @return connection to the DB
    */
    public Connection getConnection() throws SQLException {
        Connection con = null;
        try{
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");

            DataSource ds = (DataSource)envContext.lookup("jdbc/repair_agency");
            con = ds.getConnection();
        } catch (NamingException ex) {
            logger.fatal("Cannot obtain a connection from the pool");
            throw new Error(ex);
        }
        return con;
    }

    /**
     * Commiting changes and closing connection
     *
     * @param con connection to the DB
    */
    public void commitAndClose(Connection con) {
        if(con == null){
            return;
        }
        try {
            con.commit();
            con.close();
        } catch (SQLException ex) {
            logger.error("Committing fails");
            ex.printStackTrace();
        }
    }

    /**
     * Rollbacking changes and closing connection
     *
     * @param con connection to the DB
     */
    public void rollbackAndClose(Connection con) {
        if(con == null){
            return;
        }
        try {
            con.rollback();
            con.close();
        } catch (SQLException ex) {
            logger.error("Rollback fails");
            ex.printStackTrace();
        }
        logger.trace("Transaction has been rollbacked");
    }
}
