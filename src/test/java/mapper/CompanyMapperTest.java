package mapper;

import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import model.Computer;

@RunWith(MockitoJUnitRunner.class)
public class CompanyMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    public void testGetInstance() {
        CompanyMapper companyMapper = null;
        companyMapper = CompanyMapper.getInstance();
        assertNotNull(companyMapper);
    }

    @Test
    public void testToModelResultSet() throws SQLException, MapperException {
        // ResultSet with a full entry
        Mockito.when(resultSet.getInt("computer_id")).thenReturn(12);
        Mockito.when(resultSet.getString("computer_name")).thenReturn("testName");
        Mockito.when(resultSet.getDate("introduced")).thenReturn(Date.valueOf("2020-01-01"));
        Mockito.when(resultSet.getDate("discontinued")).thenReturn(Date.valueOf("2021-01-01"));
        Mockito.when(resultSet.getString("company_name")).thenReturn("Samsung");
        Mockito.when(resultSet.getInt("company_id")).thenReturn(1);

        Computer computer = ComputerMapper.getInstance().toModel(resultSet);
        Computer computer2 = new Computer(12, "testName", LocalDate.of(2020, 01, 01),
                LocalDate.of(2021, 01, 01), "Samsung", 1);

        Assert.assertEquals(computer, computer2);
    }

}
