package arch.galaxyeclipse.server.data;

import org.testng.annotations.Test;

import java.util.Arrays;

/**
 *
 */
public class JedisUnitOfWorkTest {
    @Test
    public void testBitOperators() throws Exception {
        System.out.println(Arrays.toString(JedisOperations
                .getLocationObjectKey(Integer.MAX_VALUE)));
    }
}
