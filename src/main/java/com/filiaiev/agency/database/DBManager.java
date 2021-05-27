package com.filiaiev.agency.database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {

    private static DBManager instance;

    private DBManager(){}

    public static synchronized DBManager getInstance(){
        if(instance == null){
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection con = null;
        try{
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");

            DataSource ds = (DataSource)envContext.lookup("jdbc/repair_agency");
            con = ds.getConnection();
        } catch (NamingException ex) {
            System.out.println("Cannot obtain a connection from the pool");
        }
        return con;
    }

    public void commitAndClose(Connection con) {
        if(con == null){
            return;
        }
        try {
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void rollbackAndClose(Connection con) {
        if(con == null){
            return;
        }
        try {
            con.rollback();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
