package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.database.util.Field;
import com.filiaiev.agency.entity.User;

import java.sql.*;

public class UserDAO {

    private static final String SQL__GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";

    private static final String SQL__GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";

    private static final String SQL__INSERT_USER = "INSERT INTO users(email, login, pass, role_id)" +
            " VALUES(?, ?, ?, ?);";

    public Integer insertUser(String email, String login, String pass, int roleId){
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
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
                throw new IllegalStateException(e);
            }
        }
    }
}
