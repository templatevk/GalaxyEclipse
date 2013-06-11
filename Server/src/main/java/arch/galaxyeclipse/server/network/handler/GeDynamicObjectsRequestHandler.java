package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.GeDynamicObjectsHolder.GeLocationObjectsHolder;
import arch.galaxyeclipse.server.data.GePlayerInfoHolder;
import arch.galaxyeclipse.shared.GeConstants;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeDynamicObjectsResponse;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

import java.util.Collection;

/**
 *
 */
class GeDynamicObjectsRequestHandler extends GePacketHandlerDecorator {

    private GePlayerInfoHolder playerInfoHolder;

    public GeDynamicObjectsRequestHandler(IGeChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);
        playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();
    }

    @Override
    protected boolean handleImp(GePacket packet) {
        switch (packet.getType()) {
            case DYNAMIC_OBJECTS_REQUEST:
                processDynamicObjectsRequest();
                return true;
        }
        return false;
    }

    private void processDynamicObjectsRequest() {
        GeLocationObjectPacket.Builder lopBuilder = playerInfoHolder.getLopBuilder();
        GeLocationObjectsHolder locationObjectsHolder = playerInfoHolder.getLocationObjectsHolder();

        Collection<GeLocationObjectPacket.Builder> matchingObjects = locationObjectsHolder.getNearbyObjects(
                lopBuilder, GeConstants.RADIUS_DYNAMIC_OBJECT_QUERY);

        GeDynamicObjectsResponse.Builder dorBuilder = GeDynamicObjectsResponse.newBuilder();
        for (GeLocationObjectPacket.Builder matchingObject : matchingObjects) {
            dorBuilder.addObjects(matchingObject);
        }

        GePacket packet = GePacket.newBuilder()
                .setType(GePacket.Type.DYNAMIC_OBJECTS_RESPONSE)
                .setDynamicObjectsResponse(dorBuilder.build())
                .build();
        getServerChannelHandler().sendPacket(packet);
    }
}
