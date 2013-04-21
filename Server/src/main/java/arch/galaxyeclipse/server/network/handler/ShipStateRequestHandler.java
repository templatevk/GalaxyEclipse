package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.protocol.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import org.hibernate.*;
import org.hibernate.criterion.*;

/**
 *
 */
class ShipStateRequestHandler extends PacketHandlerDecorator {
    private GeProtocolMessageFactory geProtocolMessageFactory;

    ShipStateRequestHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
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
        ShipState shipState = new UnitOfWork<ShipState>() {
            @Override
            protected void doWork(Session session) {
                int shipStateId = getServerChannelHandler().getPlayerInfoHolder()
                        .getPlayer().getShipStateId();

                setResult((ShipState) session
                        .createCriteria(ShipState.class)
                        .add(Restrictions.eq("shipStateId", shipStateId))
                        .uniqueResult());
            }
        }.execute();

        LocationObject locationObject = getServerChannelHandler()
                .getPlayerInfoHolder().getLocationObject();
        GeProtocol.ShipStateResponse shipStateResponse = geProtocolMessageFactory
                .createShipStateResponse(shipState, locationObject);

        GeProtocol.Packet packet = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.SHIP_STATE_RESPONSE)
                .setShipStateResponse(shipStateResponse)
                .build();
        getServerChannelHandler().sendPacket(packet);
    }
}
