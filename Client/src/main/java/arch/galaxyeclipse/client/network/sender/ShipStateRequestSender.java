package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.data.ShipStateInfoHolder;
import arch.galaxyeclipse.shared.GeConstants;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;

/**
 *
 */
public class ShipStateRequestSender extends RepeatablePacketSender {
    private ShipStateInfoHolder shipStateInfoHolder;

    public ShipStateRequestSender() {
        super(Packet.Type.SHIP_STATE_RESPONSE,
                GeConstants.CLIENT_SHIP_STATE_REQUEST_INTERVAL_MILLISECONDS);
        shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);
        getClientNetworkManager().addPacketListener(this);
    }

    @Override
    protected void onPacketReceivedImpl(Packet packet) {
        GeProtocol.ShipStateResponse shipStateResponse = packet.getShipStateResponse();
        shipStateInfoHolder.setShipState(shipStateResponse);
    }

    @Override
    protected void sendRequest() {
        Packet packet = Packet.newBuilder()
                .setType(Packet.Type.SHIP_STATE_REQUEST)
                .build();
        getClientNetworkManager().sendPacket(packet);
    }
}
