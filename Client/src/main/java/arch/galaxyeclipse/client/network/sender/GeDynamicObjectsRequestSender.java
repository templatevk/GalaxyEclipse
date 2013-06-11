package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.data.GeLocationInfoHolder;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

import java.util.List;

import static arch.galaxyeclipse.shared.GeConstants.CLIENT_REQUEST_DYNAMIC_OBJECTS_INTERVAL_MILLISECONDS;

/**
 *
 */
public class GeDynamicObjectsRequestSender extends GeRepeatablePacketSender {

    private GeLocationInfoHolder locationInfoHolder;

    public GeDynamicObjectsRequestSender() {
        super(GePacket.Type.DYNAMIC_OBJECTS_RESPONSE,
                CLIENT_REQUEST_DYNAMIC_OBJECTS_INTERVAL_MILLISECONDS);
        locationInfoHolder = GeContextHolder.getBean(GeLocationInfoHolder.class);
        getClientNetworkManager().addPacketListener(this);
    }

    @Override
    protected void sendRequest() {
        GePacket packet = GePacket.newBuilder()
                .setType(GePacket.Type.DYNAMIC_OBJECTS_REQUEST)
                .build();
        getClientNetworkManager().sendPacket(packet);
    }

    @Override
    protected void onPacketReceivedImpl(GePacket packet) {
        List<GeLocationObjectPacket> objectsList = packet
                .getDynamicObjectsResponse().getObjectsList();
        locationInfoHolder.setDynamicObjects(objectsList);
    }
}