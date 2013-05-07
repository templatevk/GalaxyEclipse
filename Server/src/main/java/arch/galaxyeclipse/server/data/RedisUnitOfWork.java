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
public abstract class RedisUnitOfWork<T> {
    @Setter(AccessLevel.PROTECTED)
    private T result;

    public RedisUnitOfWork() {

    }

    protected abstract void doWork(JedisConnection connection);

    public T execute() {
        JedisConnection connection = JedisConnectionFactoryHolder
                .jedisConnectionFactory.getConnection();

        try {
            doWork(connection);
        } catch (Exception e) {
            RedisUnitOfWork.log.error("Error on " + LogUtils.getObjectInfo(this), e);
        } finally {
            try {
                connection.close();
            } catch (DataAccessException e) {
                RedisUnitOfWork.log.error("Error closing Jedis connection ", e);
            }
        }

        return result;
    }

    private static class JedisConnectionFactoryHolder {
        private static JedisConnectionFactory jedisConnectionFactory =
                ContextHolder.getBean(JedisConnectionFactory.class);
    }
}
