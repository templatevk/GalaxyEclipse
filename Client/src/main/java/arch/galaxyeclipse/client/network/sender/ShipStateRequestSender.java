package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;

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

        shipStateInfoHolder.setArmorDurability(shipStateResponse.getArmorDurability());
        shipStateInfoHolder.setHp(shipStateResponse.getHp());
        shipStateInfoHolder.setMoveSpeed(shipStateResponse.getMoveSpeed());
        shipStateInfoHolder.setPositionX(shipStateResponse.getPositionX());
        shipStateInfoHolder.setPositionY(shipStateResponse.getPositionY());
        shipStateInfoHolder.setRotationAngle(shipStateResponse.getRotationAngle());
        shipStateInfoHolder.setRotationSpeed(shipStateResponse.getRotationSpeed());
    }

    @Override
    protected void sendRequest() {
        GeProtocol.Packet packet = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.SHIP_STATE_REQUEST)
                .build();
        getClientNetworkManager().sendPacket(packet);
    }
}
