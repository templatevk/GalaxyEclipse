package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.protocol.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.types.*;
import org.hibernate.*;
import org.hibernate.criterion.*;

import java.util.*;

/**
 *
 */
class DynamicObjectsRequestHandler extends PacketHandlerDecorator {
    private final DictionaryTypesMapper dictionaryTypesMapper;
    private GeProtocolMessageFactory geProtocolMessageFactory;

    public DynamicObjectsRequestHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        geProtocolMessageFactory = ContextHolder.getBean(GeProtocolMessageFactory.class);
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);
    }

    @Override
    protected boolean handleImp(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case DYNAMIC_OBJECTS_REQUEST:
                processDynamicObjectsRequest();
                return true;
        }
        return false;
    }

    private void processDynamicObjectsRequest() {
        List<LocationObject> locationObjects = new HibernateUnitOfWork<List<LocationObject>>() {
            @Override
            protected void doWork(Session session) {
                LocationObject locationObject = getServerChannelHandler()
                        .getPlayerInfoHolder().getLocationObject();

                float x1 = locationObject.getPositionX() - SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS;
                float x2 = locationObject.getPositionX() + SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS;
                float y1 = locationObject.getPositionY() - SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS;
                float y2 = locationObject.getPositionY() + SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS;
                int dynamicBehaviorId = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        LocationObjectBehaviorTypesMapperType.DYNAMIC);
                List<LocationObject> locationObjects = session
                        .createCriteria(LocationObject.class)
                        .add(Restrictions.between("positionX", x1, x2))
                        .add(Restrictions.between("positionY", y1, y2))
                        .add(Restrictions.eq("locationObjectBehaviorTypeId", dynamicBehaviorId))
                        .list();

                setResult(locationObjects);
            }
        }.execute();

        GeProtocol.DynamicObjectsResponse dynamicObjectsResponse =
                geProtocolMessageFactory.createDynamicObjectsResponse(locationObjects);
        GeProtocol.Packet packet = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.DYNAMIC_OBJECTS_RESPONSE)
                .setDynamicObjectsResponse(dynamicObjectsResponse)
                .build();
        getServerChannelHandler().sendPacket(packet);
    }
}
