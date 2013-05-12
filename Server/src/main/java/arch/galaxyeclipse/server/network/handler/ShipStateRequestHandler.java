package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.server.protocol.GeProtocolMessageFactory;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStateResponse;

/**
 *
 */
class ShipStateRequestHandler extends PacketHandlerDecorator {
    private GeProtocolMessageFactory messageFactory;

    ShipStateRequestHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        messageFactory = ContextHolder.getBean(GeProtocolMessageFactory.class);
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
        PlayerInfoHolder playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();
        ShipStateResponse shipStateResponse = playerInfoHolder.getSsrBuilder().build();
        GeProtocol.Packet packet = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.SHIP_STATE_RESPONSE)
                .setShipStateResponse(shipStateResponse)
                .build();
        getServerChannelHandler().sendPacket(packet);
    }
}
