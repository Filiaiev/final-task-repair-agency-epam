package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.database.util.Field;
import com.filiaiev.agency.entity.User;
import org.apache.log4j.Logger;

import java.sql.*;

public class UserDAO {

    private static Logger logger = Logger.getLogger(UserDAO.class);

    private static final String SQL__GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";

    private static final String SQL__GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";

    private static final String SQL__INSERT_USER = "INSERT INTO users(email, login, pass, role_id)" +
            " VALUES(?, ?, ?, ?);";

    private static final String SQL__GET_USER_BY_EMAIL = "SELECT * FROM users WHERE " +
            "email = ?;";

    public Integer insertUser(String email, String login, String pass, int roleId) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        Integer userId = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__INSERT_USER, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, email);
            ps.setString(2, login);
            ps.setString(3, pass);
            ps.setInt(4, roleId);

            ps.execute();
            rs = ps.getGeneratedKeys();
            if(rs.next()){
                userId = rs.getInt(1);
            }
            ps.close();
        }
        catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot insert new user with login '" + login + "'", e);
            return null;
        }

        DBManager.getInstance().commitAndClose(con);
        return userId;
    }

    public User getUserById(int userId){
        User user = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_USER_BY_ID);

            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if(rs.next()){
                user = new UserMapper().mapRow(rs);
            }

            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get user by id = " + userId, e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return user;
    }

    public User getUserByLogin(String login){
        User user = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_USER_BY_LOGIN);

            ps.setString(1, login);
            rs = ps.executeQuery();
            if(rs.next()){
                user = new UserMapper().mapRow(rs);
            }

            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get user by login '" + login + "'", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return user;
    }

    // TODO : delete?
    public User getUserByEmail(String email){
        User user = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_USER_BY_EMAIL);

            ps.setString(1, email);
            rs = ps.executeQuery();
            if(rs.next()){
                user = new UserMapper().mapRow(rs);
            }

            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Getting user by email exception", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return user;
    }

    private static class UserMapper implements EntityMapper<User> {
        @Override
        public User mapRow(ResultSet rs) {
            try{
                User user = new User();
                user.setId(rs.getInt(Field.ID));
                user.setLogin(rs.getString(Field.USERS__LOGIN));
                user.setEmail(rs.getString(Field.USERS__EMAIL));
                user.setPass(rs.getString(Field.USERS__PASS));
                user.setRoleId(rs.getInt(Field.USERS__ROLE_ID));
                return user;
            }catch (SQLException e){
                logger.error("Cannot map row with given ResultSet", e);
                return null;
            }
        }
    }
}
