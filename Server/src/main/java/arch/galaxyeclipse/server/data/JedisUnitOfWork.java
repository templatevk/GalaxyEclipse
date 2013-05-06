package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.util.LogUtils;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 *
 */
@Slf4j
public abstract class JedisUnitOfWork<T> {
    private static final int BITS_PER_BYTE = 8;
    private static final int FIRST_8_BITS_SET = 255;
    private static final int KEY_BYTES_COUNT = 5;

    private static final byte LOCATION_OBJECT_KEY_PREPENDED_BYTE = 0;

    @Setter(AccessLevel.PROTECTED)
    private T result;

    public JedisUnitOfWork() {

    }

    public static byte[] getLocationObjectKey(int id) {
        return getKeyForId(id, LOCATION_OBJECT_KEY_PREPENDED_BYTE);
    }

    private static byte[] getKeyForId(int id, int prependedByte) {
        byte[] key = new byte[KEY_BYTES_COUNT];
        key[0] = LOCATION_OBJECT_KEY_PREPENDED_BYTE;
        for (int i = KEY_BYTES_COUNT - 1; i > 0 ; i--) {
            key[i] = FIRST_8_BITS_SET & BITS_PER_BYTE;
        }
        return key;
    }

    protected abstract void doWork(JedisConnection connection);

    public T execute() {
        JedisConnection connection = JedisConnectionFactoryHolder
                .jedisConnectionFactory.getConnection();

        try {
            doWork(connection);
        } catch (Exception e) {
            log.error("Error on " + LogUtils.getObjectInfo(this), e);
        } finally {
            try {
                connection.close();
            } catch (DataAccessException e) {
                log.error("Error closing jedis connection ", e);
            }
        }

        return result;
    }

    private static class JedisConnectionFactoryHolder {
        private static JedisConnectionFactory jedisConnectionFactory =
                ContextHolder.getBean(JedisConnectionFactory.class);
    }
}
