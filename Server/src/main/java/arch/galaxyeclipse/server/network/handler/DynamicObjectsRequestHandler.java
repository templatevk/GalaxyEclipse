package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.JedisSerializers.LocationObjectPacketSerializer;
import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.server.data.RedisUnitOfWork;
import arch.galaxyeclipse.server.protocol.GeProtocolMessageFactory;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import org.springframework.data.redis.connection.jedis.JedisConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static arch.galaxyeclipse.shared.SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS;
import static arch.galaxyeclipse.shared.protocol.GeProtocol.DynamicObjectsResponse;

/**
 *
 */
class DynamicObjectsRequestHandler extends PacketHandlerDecorator {
    private final DictionaryTypesMapper dictionaryTypesMapper;
    private GeProtocolMessageFactory geProtocolMessageFactory;

    public DynamicObjectsRequestHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        geProtocolMessageFactory = ContextHolder.getBean(GeProtocolMessageFactory.class);
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);
    }

    @Override
    protected boolean handleImp(Packet packet) {
        switch (packet.getType()) {
            case DYNAMIC_OBJECTS_REQUEST:
                processDynamicObjectsRequest();
                return true;
        }
        return false;
    }

    private void processDynamicObjectsRequest() {
        List<LocationObjectPacket> lopList = new RedisUnitOfWork<List<LocationObjectPacket>>() {
            @Override
            protected void doWork(JedisConnection connection) {
                PlayerInfoHolder playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();
                LocationObjectPacketSerializer lopSerializer = new LocationObjectPacketSerializer();
                byte[] lopHashKey = playerInfoHolder.getLocationObjectPacketHashKey();
                byte[] lopSortedSetXKey = playerInfoHolder.getLocationObjectPacketSortedSetXKey();
                byte[] lopSortedSetYKey = playerInfoHolder.getLocationObjectPacketSortedSetYKey();
                byte[] lopBufSetXKey = playerInfoHolder.getLocationObjectPacketBufSetXKey();
                byte[] lopBufSetYKey = playerInfoHolder.getLocationObjectPacketBufSetYKey();

                byte[] lopBytes = connection.hGet(lopHashKey, lopHashKey);
                LocationObjectPacket lop = lopSerializer.deserialize(lopBytes);
                float positionX = lop.getPositionX();
                float positionY = lop.getPositionY();
                float x1 = positionX - DYNAMIC_OBJECT_QUERY_RADIUS;
                float x2 = positionX + DYNAMIC_OBJECT_QUERY_RADIUS;
                float y1 = positionY - DYNAMIC_OBJECT_QUERY_RADIUS;
                float y2 = positionY + DYNAMIC_OBJECT_QUERY_RADIUS;

                Set<byte[]> xMatchingLops = connection.zRangeByScore(lopSortedSetXKey, x1, x2);
                Set<byte[]> yMatchingLops = connection.zRangeByScore(lopSortedSetYKey, y1, y2);

                connection.openPipeline();
                connection.del(lopBufSetXKey);
                for (byte[] xMatchingLop : xMatchingLops) {
                    connection.sAdd(lopBufSetXKey, xMatchingLop);
                }
                connection.del(lopBufSetYKey);
                for (byte[] yMatchingLop : yMatchingLops) {
                    connection.sAdd(lopBufSetYKey, yMatchingLop);
                }
                connection.closePipeline();

                List<LocationObjectPacket> resultLops = new ArrayList<>();
                Set<byte[]> resultLopHashKeys = connection.sInter(lopBufSetXKey, lopBufSetYKey);
                for (byte[] resultLopHashKey : resultLopHashKeys) {
                    byte[] resultLopBytes = connection.hGet(resultLopHashKey, resultLopHashKey);
                    LocationObjectPacket resultLop = lopSerializer.deserialize(resultLopBytes);
                    resultLops.add(resultLop);
                }
                setResult(resultLops);
            }
        }.execute();

        DynamicObjectsResponse dynamicObjectsResponse = DynamicObjectsResponse
                .newBuilder().addAllObjects(lopList).build();
        Packet packet = Packet.newBuilder()
                .setType(Packet.Type.DYNAMIC_OBJECTS_RESPONSE)
                .setDynamicObjectsResponse(dynamicObjectsResponse)
                .build();
        getServerChannelHandler().sendPacket(packet);
    }
}
