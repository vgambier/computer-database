package mapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

class CompanyMapperTest {

    @Test
    void testGetInstance() {
        CompanyMapper companyMapper = null;
        companyMapper = CompanyMapper.getInstance();
        assertNotNull(companyMapper);
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
