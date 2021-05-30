package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.EmployeeDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.entity.Employee;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SetRepairerCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int repairerPersonId = Integer.parseInt(req.getParameter("repairer"));
        Map<String, String> orderInfo = (Map<String, String>)req.getSession().getAttribute("order_info");

        int orderId = Integer.parseInt(orderInfo.get("id"));
        Employee employee = new EmployeeDAO().getEmployeeByPersonId(repairerPersonId);
        new OrderDAO().setOrderRepairerById(employee.getId(),
                orderId);

        Person person = new PersonDAO().getPersonById(repairerPersonId);
        orderInfo.put("worker_last_name", person.getLastName());
        orderInfo.put("worker_first_name", person.getFirstName());
        orderInfo.put("worker_middle_name", person.getMiddleName());

        req.getSession().setAttribute("order_info", orderInfo);
        return "/controller?command=" + CommandContainer.getOrderInfoCmd +
                "&orderId=" + orderId;
    }
}
