package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.GePlayerInfoHolder;
import arch.galaxyeclipse.server.protocol.GeProtocolMessageFactory;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStateResponse;

/**
 *
 */
class GeShipStateRequestHandler extends GePacketHandlerDecorator {
    private GeProtocolMessageFactory messageFactory;

    GeShipStateRequestHandler(IGeChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        messageFactory = GeContextHolder.getBean(GeProtocolMessageFactory.class);
    }

    @Override
    protected boolean handleImp(GePacket packet) {
        switch (packet.getType()) {
            case SHIP_STATE_REQUEST:
                sendShipStateResponse();
                return true;
        }
        return false;
    }

    private void sendShipStateResponse() {
        GePlayerInfoHolder playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();
        GeShipStateResponse shipStateResponse = playerInfoHolder.getSsrBuilder().build();
        GePacket packet = GePacket.newBuilder()
                .setType(GePacket.Type.SHIP_STATE_RESPONSE)
                .setShipStateResponse(shipStateResponse)
                .build();
        getServerChannelHandler().sendPacket(packet);
    }
}
