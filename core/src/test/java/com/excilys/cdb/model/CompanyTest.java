package com.excilys.cdb.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.Assert;

public class CompanyTest {

    @Test
    public void testToString() {
        Company company = new Company(14, "Water Inc.");
        assertEquals(company.toString(), "id: 14\tname: Water Inc.");
    }

    @Test
    public void getId_Ok() {
        int expected=5;
        Company company= new Company(5,"test");
        assertEquals(5,company.getId());
    }

    @Test
    public void getId_NotOk() {
        int expected=6;
        Company company= new Company(5,"test");
        assertEquals(5,company.getId());
    }

    @Test
    public void getId_Empty() {
        Company company= new Company();
        Assert.assertEquals(0,company.getId());
    }

    @Test
    public void testHashCode_NoNames() {
    Company company=new Company(2,null);
    assertEquals(31*33,company.hashCode());
    }

    @Test
    public void testHashCode_OkA() {
        Company company=new Company(2,"a");
        assertEquals(31*33+97,company.hashCode());
    }

    @Test
    public void testHashCode_OkZ() {
        Company company=new Company(2,"z");
        assertEquals(31*33+122,company.hashCode());
    }

    @Test
    public void testHashCode_Null() {
        Company company=new Company();
        assertEquals(31*31,company.hashCode());
    }



    @Test
    public void testEquals_Null() {
        Company one = new Company();
        Company two = new Company();
        assertEquals(one,two);
    }

    @Test
    public void testEquals_Ok() {
        Company one = new Company(1,"test");
        Company two = new Company(1,"test");
        assertEquals(one,two);
    }

    @Test
    public void testEquals_IdNotOk() {
        Company one = new Company(1,"test");
        Company two = new Company(2,"test");
        assertNotEquals(one,two);
    }

    @Test
    public void testEquals_NameNotOk() {
        Company one = new Company(1,"test1");
        Company two = new Company(1,"test2");
        assertNotEquals(one,two);
    }

    @Test
    public void testEquals_ObjectNotOk() {
        Company one = new Company(1,"test1");
        Computer two = new Computer.Builder().withId(1).withName("test1").build();
        assertNotEquals(one,two);
    }

    @Test
    public void testEquals_ObjectNull() {
        Company one = new Company(1,"test1");
        assertNotEquals(one,null);
    }
}
