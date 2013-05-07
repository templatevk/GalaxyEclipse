package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.AbstractTestNGServerTest;
import arch.galaxyeclipse.server.data.JedisSerializers.LocationObjectPacketSerializer;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Slf4j
public class RedisTest extends AbstractTestNGServerTest {
    private static final int OBJECT_X_ZSET = 1;
    private static final int OBJECT_Y_ZSET = 2;
    private static final int OBJECT_X_BUF_SET = 3;
    private static final int OBJECT_Y_BUF_SET = 4;
    private static final int OBJECT_RESULT_SET = 5;
    private static final int MIN_SCORE = 2;
    private static final int MAX_SCORE = 4;

    private LocationObjectPacket lo1;
    private LocationObjectPacket lo2;
    private LocationObjectPacket lo3;
    private LocationObjectPacket lo4;
    private LocationObjectPacket lo5;
    private List<LocationObjectPacket> locationObjects;

    @BeforeMethod
    public void setUp() throws Exception {
        lo1 = LocationObjectPacket.newBuilder().setPositionX(1).setPositionY(1).setNativeId(1).setObjectId(1).setObjectTypeId(1).setRotationAngle(1).build();
        lo2 = LocationObjectPacket.newBuilder().setPositionX(2).setPositionY(2).setNativeId(1).setObjectId(1).setObjectTypeId(1).setRotationAngle(1).build();
        lo3 = LocationObjectPacket.newBuilder().setPositionX(3).setPositionY(3).setNativeId(1).setObjectId(1).setObjectTypeId(1).setRotationAngle(1).build();
        lo4 = LocationObjectPacket.newBuilder().setPositionX(4).setPositionY(4).setNativeId(1).setObjectId(1).setObjectTypeId(1).setRotationAngle(1).build();
        lo5 = LocationObjectPacket.newBuilder().setPositionX(5).setPositionY(5).setNativeId(1).setObjectId(1).setObjectTypeId(1).setRotationAngle(1).build();
        locationObjects = Arrays.asList(lo1, lo2, lo3, lo4, lo5);
        ContextHolder.getBean(JedisConnectionFactory.class).getConnection().flushDb();
    }

    @Test
    public void testRedisConnection() throws Exception {
        byte[] xKey         = { (byte)OBJECT_X_ZSET };
        byte[] xBufKey      = { (byte)OBJECT_X_BUF_SET };
        byte[] yKey         = { (byte)OBJECT_Y_ZSET };
        byte[] yBufKey      = { (byte)OBJECT_Y_BUF_SET };
        byte[] resultKey    = { (byte)OBJECT_RESULT_SET };

        JedisConnectionFactory jedisConnectionFactory = ContextHolder.getBean(
                JedisConnectionFactory.class);
        LocationObjectPacketSerializer serializer = new LocationObjectPacketSerializer();
        JedisConnection connection = jedisConnectionFactory.getConnection();

        connection.openPipeline();
        long start = System.currentTimeMillis();
        for (LocationObjectPacket locationObject : locationObjects) {
            connection.zAdd(xKey, locationObject.getPositionX(),
                    serializer.serialize(locationObject));
            connection.zAdd(yKey, locationObject.getPositionX(),
                    serializer.serialize(locationObject));
        }
        connection.closePipeline();
        log.info("Serialized " + (System.currentTimeMillis() - start));

        Set<byte[]> objectsX = connection.zRangeByScore(xKey, MIN_SCORE, MAX_SCORE);
        Set<byte[]> objectsY = connection.zRangeByScore(yKey, MIN_SCORE, MAX_SCORE);
        connection.openPipeline();
        for (byte[] objectX : objectsX) {
            connection.sAdd(xBufKey, objectX);
        }
        for (byte[] objectY : objectsY) {
            connection.sAdd(yBufKey, objectY);
        }
        connection.zInterStore(resultKey, xBufKey, yBufKey);
        connection.closePipeline();

        for (byte[] resultObjectBytes : connection.zRange(resultKey, 0, -1)) {
             serializer.deserialize(resultObjectBytes);
        }
        log.info("Operation took exactly " + (System.currentTimeMillis() - start) +
                ". Damn, thats incredibly fast and awesome!");
    }

    @Test
    public void testRedisTemplate() throws Exception {
        final RedisTemplate<Integer, LocationObjectPacket> redisTemplate = ContextHolder
                .getBean(RedisTemplate.class);
        redisTemplate.setValueSerializer(new LocationObjectPacketSerializer());

        long start = System.currentTimeMillis();
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (LocationObjectPacket locationObject : locationObjects) {
                    redisTemplate.boundZSetOps(OBJECT_X_ZSET).add(locationObject,
                            locationObject.getPositionX());
                    redisTemplate.boundZSetOps(OBJECT_Y_ZSET).add(locationObject,
                            locationObject.getPositionY());
                }
                return connection.closePipeline();
            }
        });
        log.info("Serialized " + (System.currentTimeMillis() - start));

        Set<LocationObjectPacket> objectsX = redisTemplate.boundZSetOps(OBJECT_X_ZSET)
                .rangeByScore(MIN_SCORE, MAX_SCORE);
        for (LocationObjectPacket locationObject : objectsX) {
            redisTemplate.boundSetOps(OBJECT_X_BUF_SET).add(locationObject);
        }

        Set<LocationObjectPacket> objectsY = redisTemplate.boundZSetOps(OBJECT_Y_ZSET)
                .rangeByScore(MIN_SCORE, MAX_SCORE);
        for (LocationObjectPacket locationObject : objectsY) {
            redisTemplate.boundSetOps(OBJECT_Y_BUF_SET).add(locationObject);
        }
        log.info("Buf done " + (System.currentTimeMillis() - start));

        Set<LocationObjectPacket> resultObjects = redisTemplate.boundSetOps(OBJECT_Y_BUF_SET)
                .intersect(OBJECT_X_BUF_SET);
        log.info("Operation took exactly " + (System.currentTimeMillis() - start) +
                ". Damn, thats incredibly fast and awesome!");

        Assertions.assertThat(resultObjects)
                .hasSize(3)
                .containsOnly(lo2, lo3, lo4);
    }
}
