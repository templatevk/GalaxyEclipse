package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;

import java.util.*;

/**
 *
 */
public class DynamicObjectsRequestSender extends RepeatablePacketSender {
    private static final int SLEEP_MILLISECONDS = 5;

    private LocationInfoHolder locationInfoHolder;

    public DynamicObjectsRequestSender() {
        super(GeProtocol.Packet.Type.DYNAMIC_OBJECTS_RESPONSE, SLEEP_MILLISECONDS);

        locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);

        getClientNetworkManager().addPacketListener(this);
    }

    @Override
    protected void sendRequest() {
        GeProtocol.Packet packet = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.DYNAMIC_OBJECTS_REQUEST)
                .build();
        getClientNetworkManager().sendPacket(packet);
    }

    @Override
    protected void processPacket(GeProtocol.Packet packet) {
        List<GeProtocol.LocationInfo.LocationObject> objectsList = packet
                .getDynamicObjectsResponse().getObjectsList();
        locationInfoHolder.setDynamicObjects(objectsList);
    }
}