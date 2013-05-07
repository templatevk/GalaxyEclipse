package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.JedisSerializers.ShipStateResponseSerializer;
import arch.galaxyeclipse.server.data.JedisUnitOfWork;
import arch.galaxyeclipse.server.protocol.GeProtocolMessageFactory;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStateResponse;
import org.springframework.data.redis.connection.jedis.JedisConnection;

/**
 *
 */
class ChatMessageHandler extends PacketHandlerDecorator {
    private GeProtocolMessageFactory geProtocolMessageFactory;

    ChatMessageHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        geProtocolMessageFactory = ContextHolder.getBean(GeProtocolMessageFactory.class);
    }

    @Override
    protected boolean handleImp(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case SHIP_STATE_REQUEST:
                sendShipStateResponse();
                return true;
        }
        return false;
    }

    private void sendShipStateResponse() {
        ShipStateResponse shipStateResponse = new JedisUnitOfWork<ShipStateResponse>() {
            @Override
            protected void doWork(JedisConnection connection) {
                ShipStateResponseSerializer serializer = new ShipStateResponseSerializer();
                byte[] key = getServerChannelHandler().getPlayerInfoHolder().getShipStateResponseKey();
                byte[] shipStateResponseBytes = connection.hGet(key, key);
                setResult(serializer.deserialize(shipStateResponseBytes));
            }
        }.execute();

        GeProtocol.Packet packet = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.SHIP_STATE_RESPONSE)
                .setShipStateResponse(shipStateResponse)
                .build();
        getServerChannelHandler().sendPacket(packet);
    }
}
