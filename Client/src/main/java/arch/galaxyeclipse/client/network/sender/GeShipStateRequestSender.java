package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.data.GeShipStateInfoHolder;
import arch.galaxyeclipse.shared.GeConstants;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStateResponse;

/**
 *
 */
public class GeShipStateRequestSender extends GeRepeatablePacketSender {

    private GeShipStateInfoHolder shipStateInfoHolder;

    public GeShipStateRequestSender() {
        super(GePacket.Type.SHIP_STATE_RESPONSE,
                GeConstants.DELAY_SHIP_STATE_REQUEST);
        shipStateInfoHolder = GeContextHolder.getBean(GeShipStateInfoHolder.class);
        getClientNetworkManager().addPacketListener(this);
    }

    @Override
    protected void onPacketReceivedImpl(GePacket packet) {
        GeShipStateResponse shipStateResponse = packet.getShipStateResponse();
        shipStateInfoHolder.setShipState(shipStateResponse);
    }

    @Override
    protected void sendRequest() {
        GePacket packet = GePacket.newBuilder()
                .setType(GePacket.Type.SHIP_STATE_REQUEST)
                .build();
        getClientNetworkManager().sendPacket(packet);
    }
}
