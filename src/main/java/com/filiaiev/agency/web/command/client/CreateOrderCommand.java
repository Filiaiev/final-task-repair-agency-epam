package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.database.util.Query;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateOrderCommand extends Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("-----------\nOrder creation process...");
        System.out.println(req.getParameter("orderText"));
        // Getting user session info
        Client client = (Client)req.getSession().getAttribute("client");

        Connection con = null;
        PreparedStatement ps = null;
        String orderText = req.getParameter("orderText");

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(Query.SQL__INSERT_ORDER);
            ps.setInt(1, client.getId());
            ps.setString(2, orderText);
            System.out.println("Order text is = " + orderText);
            ps.execute();
            ps.close();
        }catch (SQLException e){
            // eror 404
            DBManager.getInstance().rollbackAndClose(con);
            e.printStackTrace();
            System.out.println("rollbacked order");
            return "";
        }
        DBManager.getInstance().commitAndClose(con);
        System.out.println("----------\nOrder commited!");
        //req.getRequestDispatcher(Paths.JSP__USER_HOME).forward(req, resp);
        return Paths.JSP__HOME;
    }
}
