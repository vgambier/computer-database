package com.excilys.cdb.model;

import org.junit.Test;

// TODO: add more unit tests everywhere

public class ComputerPageTest {

    @Test(expected = ModelException.class)
    public void invalidNegativeIDConstructor() throws Exception {
        new ComputerPage(15, -1); // Assuming there are 15 entries
    }

    @Test(expected = ModelException.class)
    public void invalidBigIDConstructor() throws Exception {
        new ComputerPage(15, Integer.MAX_VALUE); // Assuming there are 15 entries
    }

    @Test(expected = ModelException.class)
    public void setMaxItemsPerPageTest_Zero() throws Exception{
            ComputerPage page = new ComputerPage(15, 1);
            page.setMaxItemsPerPage(0);
    }

    @Test(expected = ModelException.class)
    public void setMaxItemsPerPageTest_Negative() throws Exception{
        ComputerPage page = new ComputerPage(15, 1);
        page.setMaxItemsPerPage(-42);
    }

    @Test
    public void setMaxItemsPerPageTest_Ok() throws Exception{
        ComputerPage page = new ComputerPage(15, 1);
        page.setMaxItemsPerPage(1000);
    }
}
