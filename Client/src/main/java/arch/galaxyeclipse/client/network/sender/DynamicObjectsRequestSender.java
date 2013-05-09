package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.data.LocationInfoHolder;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;

import java.util.List;

import static arch.galaxyeclipse.shared.GeConstants.CLIENT_DYNAMIC_OBJECTS_REQUEST_INTERVAL_MILLISECONDS;

/**
 *
 */
public class DynamicObjectsRequestSender extends RepeatablePacketSender {
    private LocationInfoHolder locationInfoHolder;

    public DynamicObjectsRequestSender() {
        super(Packet.Type.DYNAMIC_OBJECTS_RESPONSE,
                CLIENT_DYNAMIC_OBJECTS_REQUEST_INTERVAL_MILLISECONDS);
        locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);
        getClientNetworkManager().addPacketListener(this);
    }

    @Override
    protected void sendRequest() {
        Packet packet = Packet.newBuilder()
                .setType(Packet.Type.DYNAMIC_OBJECTS_REQUEST)
                .build();
        getClientNetworkManager().sendPacket(packet);
    }

    @Override
    protected void onPacketReceivedImpl(Packet packet) {
        List<LocationObjectPacket> objectsList = packet
                .getDynamicObjectsResponse().getObjectsList();
        locationInfoHolder.setDynamicObjects(objectsList);
    }
}