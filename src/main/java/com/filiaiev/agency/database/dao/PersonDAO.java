package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.database.exception.InsertingDuplicateException;
import com.filiaiev.agency.database.util.Field;
import com.filiaiev.agency.entity.Person;
import org.apache.log4j.Logger;

import java.sql.*;

public class PersonDAO {

    private static Logger logger = Logger.getLogger(PersonDAO.class);

    private static final String SQL__GET_PERSON_BY_USER_ID = "SELECT * FROM persons" +
            " WHERE user_id = ?";

    private static final String SQL__GET_PERSON_BY_ID = "SELECT * FROM persons" +
            " WHERE id = ?";

    private static final String SQL__GET_PERSON_BY_EMPLOYEE_ID = "SELECT " +
            "p.id, last_name, first_name, " +
            "middle_name, birthdate, user_id " +
            "FROM persons as p " +
            "INNER JOIN employees as e " +
            "ON e.person_id = p.id " +
            "WHERE e.id = ?;";

    private static final String SQL__INSERT_PERSON = "INSERT INTO" +
            " persons(first_name, middle_name, last_name, birthdate, user_id)" +
            " VALUES(?, ?, ?, ?, ?);";

    public Integer insertPerson(String firstName, String middleName, String lastName,
                                Date birthDate, int userId) throws InsertingDuplicateException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        Integer personId = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__INSERT_PERSON, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, firstName);
            ps.setString(2, middleName);
            ps.setString(3, lastName);
            ps.setDate(4, birthDate);
            ps.setInt(5, userId);

            ps.execute();
            rs = ps.getGeneratedKeys();
            if(rs.next()){
                personId = rs.getInt(1);
            }
            ps.close();
        }catch (SQLIntegrityConstraintViolationException e){
            throw new InsertingDuplicateException("Person already exists or FK fails", e);
        }
        catch (SQLException e){
            logger.error("Cannot insert new person with user_id = " + userId, e);
            DBManager.getInstance().rollbackAndClose(con);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return personId;
    }

    public Person getPersonByUserId(int userId){
        Person person = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_PERSON_BY_USER_ID);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if(rs.next()){
                person = new PersonMapper().mapRow(rs);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get person by user id = " + userId, e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return person;
    }

    public Person getPersonById(int personId){
        Person person = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_PERSON_BY_ID);
            ps.setInt(1, personId);
            rs = ps.executeQuery();

            if(rs.next()){
                person = new PersonMapper().mapRow(rs);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get person by person id = " + person, e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return person;
    }

    public Person getPersonByEmployeeId(int employeeId){
        Person person = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try{
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement(SQL__GET_PERSON_BY_EMPLOYEE_ID);
            ps.setInt(1, employeeId);
            rs = ps.executeQuery();

            if(rs.next()){
                person = new PersonMapper().mapRow(rs);
            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("Cannot get person by employee id = " + employeeId, e);
            return null;
        }
        DBManager.getInstance().commitAndClose(con);
        return person;
    }

    protected static class PersonMapper implements EntityMapper<Person> {
        @Override
        public Person mapRow(ResultSet rs) {
            try{
                Person person = new Person();
                person.setId(rs.getInt(Field.ID));
                person.setFirstName(rs.getString(Field.PERSONS__FNAME));
                person.setMiddleName(rs.getString(Field.PERSONS__MNAME));
                person.setLastName(rs.getString(Field.PERSONS__LNAME));
                person.setBirthDate(rs.getDate(Field.PERSONS__BIRTH_DATE));
                person.setUserId(rs.getInt(Field.PERSONS__USER_ID));
                return person;
            }catch (SQLException e){
                logger.error("Cannot map person with given ResultSet", e);
                return null;
            }
        }
    }
}

