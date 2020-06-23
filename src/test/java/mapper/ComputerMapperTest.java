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
public class ComputerMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    public void testGetInstance() {
        ComputerMapper computerMapper = null;
        computerMapper = ComputerMapper.getInstance();
        assertNotNull(computerMapper);
    }

    @Test
    public void testToModelResultSet() throws SQLException, MapperException {

        // ResultSet with a full entry
        Mockito.when(resultSet.getInt("id")).thenReturn(12);
        Mockito.when(resultSet.getString("name")).thenReturn("testName");
        Mockito.when(resultSet.getDate("introduced")).thenReturn(Date.valueOf("2020-01-01"));
        Mockito.when(resultSet.getDate("discontinued")).thenReturn(Date.valueOf("2021-01-01"));
        Mockito.when(resultSet.getInt("company_id")).thenReturn(34);

        Computer computer = ComputerMapper.getInstance().toModel(resultSet);

        Assert.assertNotNull(computer);
        Assert.assertEquals(12, computer.getId());
        Assert.assertEquals("testName", computer.getName());
        Assert.assertEquals(LocalDate.of(2020, 01, 01), computer.getIntroduced());
        Assert.assertEquals(LocalDate.of(2021, 01, 01), computer.getDiscontinued());
        Assert.assertEquals(34, computer.getCompanyID());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = MapperException.class)
    public void testToModelEmptyResultSet() throws SQLException, MapperException {

        Mockito.when(resultSet.getInt("id")).thenThrow(SQLException.class);
        Mockito.when(resultSet.getString("name")).thenThrow(SQLException.class);
        Mockito.when(resultSet.getDate("introduced")).thenThrow(SQLException.class);
        Mockito.when(resultSet.getDate("discontinued")).thenThrow(SQLException.class);
        Mockito.when(resultSet.getInt("company_id")).thenThrow(SQLException.class);

        ComputerMapper.getInstance().toModel(resultSet);

    }
}