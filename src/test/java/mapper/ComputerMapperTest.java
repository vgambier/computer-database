package mapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.ResultSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class ComputerMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void testGetInstance() {
        ComputerMapper computerMapper = null;
        computerMapper = ComputerMapper.getInstance();
        assertNotNull(computerMapper);
    }

    @Test
    void testToModelResultSet() {
        fail("Not yet implemented");
    }

    @Test
    void testToModelListResultSet() {
        fail("Not yet implemented");
    }

}
