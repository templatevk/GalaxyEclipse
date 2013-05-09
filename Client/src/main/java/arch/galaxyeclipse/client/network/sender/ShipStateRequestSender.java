package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.data.ShipStateInfoHolder;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;

/**
 *
 */
public class ShipStateRequestSender extends RepeatablePacketSender {
    private static final int SLEEP_MILLISECONDS = 5;

    private ShipStateInfoHolder shipStateInfoHolder;

    public ShipStateRequestSender() {
        super(GeProtocol.Packet.Type.SHIP_STATE_RESPONSE, SLEEP_MILLISECONDS);

        shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);

        getClientNetworkManager().addPacketListener(this);
    }

    @Override
    protected void processPacket(GeProtocol.Packet packet) {
        GeProtocol.ShipStateResponse shipStateResponse = packet.getShipStateResponse();
        shipStateInfoHolder.setShipState(shipStateResponse);
    }

    @Override
    protected void sendRequest() {
        GeProtocol.Packet packet = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.SHIP_STATE_REQUEST)
                .build();
        getClientNetworkManager().sendPacket(packet);
    }
}
