package dao;

import com.filiaiev.agency.database.dao.EmployeeDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Person;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.sql.Date;
import java.sql.SQLException;
import static util.DBManagerUtil.mockDBManager;
import static util.DBManagerUtil.resetDBManagerStaticMock;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonDAOTest {

    private static Person personExpected;
    private static PersonDAO personDAO = new PersonDAO();

    @BeforeClass
    public static void prepare() throws SQLException {
        // Expected person for tests
        personExpected = new Person();
        personExpected.setUserId(2);
        personExpected.setLastName("Петренко");
        personExpected.setFirstName("Петро");
        personExpected.setMiddleName("Петрович");
        personExpected.setBirthDate(Date.valueOf("1990-03-17"));
        personExpected.setUserId(2);

        // Preparing mocked DBManager class
        mockDBManager();
    }

    @Test
    public void test1GetPersonByPersonId(){
        Person personActual = personDAO.getPersonById(2);
        Assert.assertEquals(personExpected, personActual);
    }

    @Test
    public void test2GetPersonByEmployeeId(){
        Person personActual = personDAO.getPersonByEmployeeId(1);
        Assert.assertEquals(personExpected, personActual);
    }

    @Test
    public void test3GetPersonByUserId(){
        Person personActual = personDAO.getPersonByUserId(2);
        Assert.assertEquals(personExpected, personActual);
    }

    @AfterClass
    public static void clear(){
        resetDBManagerStaticMock();
    }
}
