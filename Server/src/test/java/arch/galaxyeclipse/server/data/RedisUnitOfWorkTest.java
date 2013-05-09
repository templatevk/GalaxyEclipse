package arch.galaxyeclipse.server.data;

import org.testng.annotations.Test;

import java.util.Arrays;

/**
 *
 */
public class RedisUnitOfWorkTest {
    @Test
    public void testBitOperators() throws Exception {
        System.out.println(Arrays.toString(RedisStorage
                .getLocationObjectPacketHashKey(Integer.MAX_VALUE)));
    }
}
