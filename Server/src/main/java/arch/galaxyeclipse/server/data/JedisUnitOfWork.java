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
 * Database structure:
 *      *** ShipStateResponse and LocationObjectPacket - stored in hash, key and field are the same,
 *      the value is the object itself
 *      *** LocationObjectPacket are stored in two sorted sets where scores are x and y coords
 */
@Slf4j
public abstract class JedisUnitOfWork<T> {
    @Setter(AccessLevel.PROTECTED)
    private T result;

    public JedisUnitOfWork() {

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
                log.error("Error closing Jedis connection ", e);
            }
        }

        return result;
    }

    private static class JedisConnectionFactoryHolder {
        private static JedisConnectionFactory jedisConnectionFactory =
                ContextHolder.getBean(JedisConnectionFactory.class);
    }
}
