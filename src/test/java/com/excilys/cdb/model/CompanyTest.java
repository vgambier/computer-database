package com.excilys.cdb.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompanyTest {

    @Test
    public void testToString() {
        Company company = new Company(14, "Water Inc.");
        assertEquals(company.toString(), "id: 14\tname: Water Inc.");
    }

}
