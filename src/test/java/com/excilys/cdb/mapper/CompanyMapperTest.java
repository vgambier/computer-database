package com.excilys.cdb.mapper;

import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

// Note: this test class no longer does anything useful. It is kept solely as a reference as it is currently one of only two test classes that uses Mockito
//TODO: make a different Mockito test and delete this one

@RunWith(MockitoJUnitRunner.class)
public class CompanyMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    public void testGetInstance() {
        CompanyDTOMapper companyMapper = null;
        companyMapper = CompanyDTOMapper.getInstance();
        assertNotNull(companyMapper);
    }

    // @Test
    public void testToModelResultSet() throws SQLException {
        // ResultSet with a full entry
        Mockito.when(resultSet.getInt("computer_id")).thenReturn(12);
        Mockito.when(resultSet.getString("computer_name")).thenReturn("testName");
        Mockito.when(resultSet.getDate("introduced")).thenReturn(Date.valueOf("2020-01-01"));
        Mockito.when(resultSet.getDate("discontinued")).thenReturn(Date.valueOf("2021-01-01"));
        Mockito.when(resultSet.getString("company_name")).thenReturn("Samsung");
        Mockito.when(resultSet.getInt("company_id")).thenReturn(1);

        // Computer computer = ComputerDTOMapper.getInstance().mapRow(resultSet, 1);
        // Computer computer2 = new Computer(12, "testName", LocalDate.of(2020, 01, 01),
        // LocalDate.of(2021, 01, 01), "Samsung", 1);

        // Assert.assertEquals(computer, computer2);
    }

}
