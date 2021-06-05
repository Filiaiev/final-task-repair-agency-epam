package com.filiaiev.agency.web.command.auth;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.EmployeeDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.database.util.DBUtil;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogInCommand implements Command {

    private static Logger logger = Logger.getLogger(LogInCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("userLogin");
        String pass = DBUtil.hash(req.getParameter("userPass"));
        User user = new UserDAO().getUserByLogin(login);

        if(user == null) {
            req.getSession().setAttribute("loginMessage", "user_not_found");
            return "/";
        }else if(!pass.equals(user.getPass())){
            logger.info("Wrong password entered for user (" + user.getLogin() + ")");
            req.getSession().setAttribute("loginMessage", "wrong_login_or_pass");
            return "/";
        }

        HttpSession session = req.getSession();
        Person person = new PersonDAO().getPersonByUserId(user.getId());

        if(user.getRoleId() == Role.CLIENT.ordinal()){
            Client client = new ClientDAO().getClientByPersonId(person.getId());
            session.setAttribute("client", client);
            logger.info("Client #" + client.getId() + "(" + user.getLogin() + ") has logged in");
        }else if(user.getRoleId() == Role.MANAGER.ordinal()){
            session.setAttribute("repairers", new EmployeeDAO().getAllRepairersInfo());
            System.out.println(session.getAttribute("repairers"));
            logger.info("Manager " + "(" + user.getLogin() + ") has logged in");
        }else if(user.getRoleId() == Role.REPAIRER.ordinal()){
            int employeeId = new EmployeeDAO().getEmployeeByPersonId(person.getId()).getId();
            session.setAttribute("employeeId", employeeId);
            logger.info("Repairer #" + employeeId + "(" + user.getLogin() + ") has logged in");
        }

        session.setAttribute("user", user);
        session.setAttribute("person", person);
        session.setAttribute("roleId", user.getRoleId());

        return "/controller?command=toHome";
    }
}