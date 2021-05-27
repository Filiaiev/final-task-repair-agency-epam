package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.entity.Employee;
import com.filiaiev.agency.entity.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static final String SQL__GET_ALL_REPAIRERS =
            "SELECT" +
            " p.id, p.last_name," +
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

    public List<Person> getAllRepairersInfo(){
        List<Person> repairers = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            rs = con.createStatement().executeQuery(SQL__GET_ALL_REPAIRERS);
            EmployeePersonMapper personMapper = new EmployeePersonMapper();
            while(rs.next()){
                repairers.add(personMapper.mapRow(rs));
            }
            rs.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
                throw new IllegalStateException(e);
            }
            return employee;
        }
    }


}
