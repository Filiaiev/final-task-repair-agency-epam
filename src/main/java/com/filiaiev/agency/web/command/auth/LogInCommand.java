package com.filiaiev.agency.web.command.auth;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.EmployeeDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.database.util.Hasher;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// Servlet whose task is to provide logging in to the user
public class LogInCommand implements Command {

    private static Logger logger = Logger.getLogger(LogInCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("userLogin");
        String pass = Hasher.hash(req.getParameter("userPass"));

        User user = new UserDAO().getUserByLogin(login);

        /*
         * If log in fails --> redirecting to the same page
         * and showing error message
        */
        if(user == null) {
            req.getSession().setAttribute("loginMessage", "user_not_found");
            return Paths.URL__WELCOME;
        }else if(!pass.equals(user.getPass())){
            logger.info("Wrong password entered for user (" + user.getLogin() + ")");
            req.getSession().setAttribute("loginMessage", "wrong_login_or_pass");
            return Paths.URL__WELCOME;
        }

        HttpSession session = req.getSession();
        Person person = new PersonDAO().getPersonByUserId(user.getId());

        if(session.getAttribute("lang") == null){
            session.setAttribute("lang", req.getServletContext().getInitParameter("javax.servlet.jsp.jstl.fmt.locale"));
        }

        // Providing log in by role
        if(user.getRoleId() == Role.CLIENT.ordinal()){
            ClientDAO clientDAO = new ClientDAO();
            Client client = clientDAO.getClientByPersonId(person.getId());

            session.setAttribute("client", client);
            // If client did not change his lang before log in, set it to his prefferable
            String prefferableLocale = session.getAttribute("lang") == null ? client.getPreferredLocale() : (String)session.getAttribute("lang");
            clientDAO.updatePreferredLocaleById(client.getId(), prefferableLocale);

            logger.info("Client #" + client.getId() + "(" + user.getLogin() + ") has logged in");
        }else if(user.getRoleId() == Role.MANAGER.ordinal()){
            session.setAttribute("repairers", new EmployeeDAO().getAllRepairersInfo());
            logger.info("Manager " + "(" + user.getLogin() + ") has logged in");
        }else if(user.getRoleId() == Role.REPAIRER.ordinal()){
            int employeeId = new EmployeeDAO().getEmployeeByPersonId(person.getId()).getId();
            session.setAttribute("employeeId", employeeId);
            logger.info("Repairer #" + employeeId + "(" + user.getLogin() + ") has logged in");
        }

        session.setAttribute("user", user);
        session.setAttribute("person", person);
        session.setAttribute("role", Role.values()[user.getRoleId()]);

        return "/controller?command=" + CommandContainer.TO_HOME;
    }
}
