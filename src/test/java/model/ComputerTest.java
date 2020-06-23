package model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

public class ComputerTest {

    @Test
    public void testToString() {
        Computer computer = new Computer(81, "My Laptop", LocalDate.of(2015, 12, 25),
                LocalDate.of(2017, 11, 30), 4);
        assertEquals(computer.toString(),
                "id: 81\tname: My Laptop\tintroduced: 2015-12-25\tdiscontinued: 2017-11-30\tcompanyID: 4");
    }

}
