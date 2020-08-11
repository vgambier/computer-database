package com.excilys.cdb.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDate;

import org.junit.Test;

public class ComputerTest {

    @Test
    public void testToString() {
        Computer computer = new Computer(81, "My Laptop", LocalDate.of(2015, 12, 25),
                LocalDate.of(2017, 11, 30), new Company(1, "Samsung"));
        assertEquals(computer.toString(),
                "id: 81\tname: My Laptop\tintroduced: 2015-12-25\tdiscontinued: 2017-11-30\tcompany: Samsung");
    }

    @Test
    public void testHashCode_OnlyId() {
        Computer test = new Computer.Builder().withId(1).build();
        assertEquals((31*31*31+1)*31*31,test.hashCode());
    }

    @Test
    public void testHashCode_Null() {
        Computer test = new Computer.Builder().build();
        assertEquals(31*31*31*31*31,test.hashCode());
    }


    @Test
    public void testEquals_Null() {
        Computer one = new Computer();
        Computer two = new Computer();
        assertEquals(one,two);
    }

    @Test
    public void testEquals_Ok() {
        Computer one = new Computer.Builder().withId(1).withName("test").build();
        Computer two = new Computer.Builder().withId(1).withName("test").build();
        assertEquals(one,two);
    }

    @Test
    public void testEquals_IdNotOk() {
        Computer one = new Computer.Builder().withId(1).withName("test").build();
        Computer two = new Computer.Builder().withId(2).withName("test").build();
        assertNotEquals(one,two);
    }

    @Test
    public void testEquals_NameNotOk() {
        Computer one = new Computer.Builder().withId(1).withName("test1").build();
        Computer two = new Computer.Builder().withId(2).withName("test2").build();
        assertNotEquals(one,two);
    }

    @Test
    public void testEquals_ObjectNotOk() {
        Computer one = new Computer.Builder().withId(1).withName("test1").build();
        Company two = new Company(1,"test1");
        assertNotEquals(one,two);
    }

    @Test
    public void testEquals_ObjectNull() {
        Computer one = new Computer.Builder().withId(1).withName("test1").build();
        assertNotEquals(one,null);
    }

}
