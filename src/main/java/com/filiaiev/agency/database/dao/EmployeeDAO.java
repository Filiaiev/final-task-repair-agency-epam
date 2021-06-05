package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.entity.Employee;
import com.filiaiev.agency.entity.Person;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDAO {

    private static Logger logger = Logger.getLogger(EmployeeDAO.class);

    private static final String SQL__GET_ALL_REPAIRERS =
            "SELECT" +
            " p.id, e.id as employee_id, p.last_name," +
            " p.first_name, p.middle_name," +
            " p.birthdate, p.user_id" +
            " FROM persons as p" +
            " INNER JOIN employees as e" +
            " ON e.person_id = p.id" +
            " INNER JOIN users as u" +
            " ON u.id = p.user_id" +
            " INNER JOIN roles as r" +
            " ON u.role_id = r.id" +
            " WHERE r.name = 'repairer';";

    private static final String SQL__GET_EMPLOYEE_BY_PERSON_ID =
            "SELECT * FROM employees WHERE person_id = ?;";

    public Map<String, Person> getAllRepairersInfo(){
        Map<String, Person> repairers = new HashMap<>();
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            rs = con.createStatement().executeQuery(SQL__GET_ALL_REPAIRERS);
            EmployeePersonMapper personMapper = new EmployeePersonMapper();
            while(rs.next()){
                repairers.put(rs.getString("employee_id"), personMapper.mapRow(rs));
            }
            logger.trace("Got all repairers --> " + repairers.size() + " persons");
            rs.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get repairers info", e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return repairers;
    }

    public Employee getEmployeeByPersonId(int personId){
        Employee employee = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_EMPLOYEE_BY_PERSON_ID);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            EmployeeMapper employeeMapper = new EmployeeMapper();
            if(rs.next()){
                employee = employeeMapper.mapRow(rs);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get employee by person id = " + personId, e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return employee;
    }

    private static class EmployeePersonMapper extends PersonDAO.PersonMapper{}

    private static class EmployeeMapper implements EntityMapper<Employee>{
        @Override
        public Employee mapRow(ResultSet rs) {
            Employee employee = new Employee();
            try{
                employee.setId(rs.getInt("id"));
                employee.setPersonId(rs.getInt("person_id"));
            }catch (SQLException e){
                logger.error("Cannot map Employee with given ResultSet", e);
                return null;
            }
            return employee;
        }
    }


}
