package com.filiaiev.agency.web.command;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.EmployeeDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.database.util.DBUtil;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogInCommand extends Command{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Login command start");
        if(req.getSession().getAttribute("user") != null){
            System.out.println("I`M NOT NULL");
//            resp.sendRedirect("home");
//            return null;
            return Paths.JSP__HOME;
        }
        /*
            * TODO: Check roles
            *       print wrong login/pass data
        */
//        if(req.getSession(false).getAttribute("user") != null){
//            return Paths.JSP__USER_HOME;
//        }
        String login = req.getParameter("userLogin");
        String pass = DBUtil.hash(req.getParameter("userPass"));
        User user = new UserDAO().getUserByLogin(login);

        String forward = null;
        if(user == null || !pass.equals(user.getPass())) {
            return "error.jsp";
        }

        HttpSession session = req.getSession();
        Person person = new PersonDAO().getPersonByUserId(user.getId());

        if(user.getRoleId() == Role.CLIENT.ordinal()){
            Client client = new ClientDAO().getClientByPersonId(person.getId());
            session.setAttribute("client", client);
        }else if(user.getRoleId() == Role.MANAGER.ordinal()){
            session.setAttribute("repairers", new EmployeeDAO().getAllRepairersInfo());
        }else if(user.getRoleId() == Role.REPAIRER.ordinal()){
            session.setAttribute("employeeId", new EmployeeDAO().getEmployeeByPersonId(person.getId()).getId());
        }

        session.setAttribute("user", user);
        session.setAttribute("person", person);
        session.setAttribute("roleId", user.getRoleId());

        forward = Paths.JSP__HOME;
        if(user.getRoleId() == Role.MANAGER.ordinal()){
            forward = CommandContainer.getCommand("getOrders").execute(req, resp);
        }
//        resp.sendRedirect("home");
        return forward;
//        return null;
    }
}
