package dao;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.EmployeeDAO;
import com.filiaiev.agency.database.exception.InsertingDuplicateException;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Employee;
import com.filiaiev.agency.entity.Person;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static util.DBManagerUtil.mockDBManager;
import static util.DBManagerUtil.resetDBManagerStaticMock;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeDAOTest {

    private static EmployeeDAO employeeDAO = new EmployeeDAO();

    @BeforeClass
    public static void prepare() throws SQLException{
        // Preparing mocked DBManager class
        mockDBManager();
    }

    @Test
    public void test1GetAllRepairersInfo(){
        Map<Integer, Person> mapActual = employeeDAO.getAllRepairersInfo();
        Map<Integer, Person> mapExpected = new HashMap<>();
        Person person = new Person();
        person.setId(3);
        person.setLastName("Максименко");
        person.setFirstName("Максим");
        person.setMiddleName("Максимович");
        person.setBirthDate(Date.valueOf("1988-11-05"));
        person.setUserId(3);
        mapExpected.put(2, person);
        Assert.assertEquals(mapExpected, mapActual);
    }

    @Test
    public void test2GetEmployeeByPersonId(){
        Employee employeeActual = employeeDAO.getEmployeeByPersonId(3);
        Employee employeeExpected = new Employee();
        employeeExpected.setId(2);
        employeeExpected.setPersonId(3);
        Assert.assertEquals(employeeExpected, employeeActual);
    }

    @AfterClass
    public static void clear(){
        resetDBManagerStaticMock();
    }
}
