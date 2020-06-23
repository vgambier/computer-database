package model;

import org.junit.Test;

public class ComputerPageTest {

    @Test(expected = ModelException.class)
    public void invalidNegativeIDConstructor() throws Exception {
        new ComputerPage(-1);
    }

    @Test(expected = ModelException.class)
    public void invalidBigIDConstructor() throws Exception {
        new ComputerPage(Integer.MAX_VALUE);
    }

}
