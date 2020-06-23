package model;

import org.junit.Test;

public class ComputerPageTest {

    // TODO: Warning, this queries the live database. Should use DBUnit

    @Test(expected = ModelException.class)
    public void invalidNegativeIDConstructor() throws Exception {
        new ComputerPage(-1);
    }

    @Test(expected = ModelException.class)
    public void invalidBigIDConstructor() throws Exception {
        new ComputerPage(Integer.MAX_VALUE);
    }

}
