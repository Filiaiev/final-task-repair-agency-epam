package dao;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.exception.InsertingDuplicateException;
import com.filiaiev.agency.entity.Client;
import org.junit.*;
import org.junit.runners.MethodSorters;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import static util.DBManagerUtil.mockDBManager;
import static util.DBManagerUtil.resetDBManagerStaticMock;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientDAOTest {

    private static Client clientExpected;
    private static ClientDAO clientDAO = new ClientDAO();

    @BeforeClass
    public static void prepare() throws SQLException{
        // Expected client for tests
        clientExpected = new Client();
        clientExpected.setId(1);
        clientExpected.setPersonId(1);
        clientExpected.setCash(BigDecimal.valueOf(30).setScale(2, RoundingMode.CEILING));
        clientExpected.setPreferredLocale("uk");

        // Preparing mocked DBManager class
        mockDBManager();
    }

    @Test
    public void test2GetClientById(){
        Client clientActual = clientDAO.getClientById(1);
        Assert.assertEquals(clientExpected, clientActual);
    }

    @Test
    public void test3GetClientByPersonId(){
        Client clientActual = clientDAO.getClientByPersonId(1);
        Assert.assertEquals(clientExpected, clientActual);
    }

    @Test
    public void test4GetClientCashByClientId(){
        BigDecimal cashActual = clientDAO.getClientCashByClientId(1);
        Assert.assertEquals(BigDecimal.valueOf(30)
                .setScale(2, RoundingMode.CEILING), cashActual);
    }

    @Test
    public void test5SetClientCashById(){
        clientDAO.setClientCashById(BigDecimal.valueOf(200.5), 1);
        BigDecimal cashActual = clientDAO.getClientCashByClientId(1);
        clientExpected.setCash(cashActual);
        Assert.assertEquals(BigDecimal.valueOf(200.5)
                .setScale(2, RoundingMode.CEILING), cashActual);
    }

    @Test
    public void test6UpdatePreferredLocaleById(){
        clientDAO.updatePreferredLocaleById(1, "en");
        clientExpected.setPreferredLocale("en");
        Client clientActual = clientDAO.getClientById(1);
        Assert.assertEquals(clientExpected, clientActual);
    }

    @AfterClass
    public static void resetCash(){
        clientDAO.setClientCashById(BigDecimal.valueOf(30), 1);
        clientDAO.updatePreferredLocaleById(1, "uk");
        resetDBManagerStaticMock();
    }
}
