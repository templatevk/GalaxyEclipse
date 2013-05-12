package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.DynamicObjectsHolder.LocationObjectsHolder;
import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.DynamicObjectsResponse;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket.Builder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;

import java.util.Collection;

/**
 *
 */
class DynamicObjectsRequestHandler extends PacketHandlerDecorator {
    private PlayerInfoHolder playerInfoHolder;

    public DynamicObjectsRequestHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);
        this.playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();
    }

    @Override
    protected boolean handleImp(Packet packet) {
        switch (packet.getType()) {
            case DYNAMIC_OBJECTS_REQUEST:
                processDynamicObjectsRequest();
                return true;
        }
        return false;
    }

    private void processDynamicObjectsRequest() {
        LocationObjectPacket.Builder lopBuilder = playerInfoHolder.getLopBuilder();
        LocationObjectsHolder locationObjectsHolder = playerInfoHolder.getLocationObjectsHolder();
        Collection<Builder> matchingObjects = locationObjectsHolder.getMatchingObjects(lopBuilder);

        DynamicObjectsResponse.Builder dorBuilder = DynamicObjectsResponse.newBuilder();
        for (Builder matchingObject : matchingObjects) {
            dorBuilder.addObjects(matchingObject);
        }

        Packet packet = Packet.newBuilder()
                .setType(Packet.Type.DYNAMIC_OBJECTS_RESPONSE)
                .setDynamicObjectsResponse(dorBuilder.build())
                .build();
        getServerChannelHandler().sendPacket(packet);
    }
}
