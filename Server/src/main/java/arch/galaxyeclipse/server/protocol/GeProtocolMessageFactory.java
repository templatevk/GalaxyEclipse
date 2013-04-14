package arch.galaxyeclipse.server.protocol;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.types.*;
import org.hibernate.*;
import org.hibernate.criterion.*;

import java.util.*;

/**
 *
 */
public class GeProtocolMessageFactory {
    private DictionaryTypesMapper dictionaryTypesMapper;

    public GeProtocolMessageFactory() {
        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(DictionaryTypesMapper.class);
    }

    public ShipStaticInfo getShipStaticInfo(final Player player) {
        return new UnitOfWork<ShipStaticInfo>() {
            @Override
            protected void doWork(Session session) {

            }
        }.execute();
    }

    public LocationInfo getLocationInfo(final int locationId) {
        return new UnitOfWork<LocationInfo>() {
            @Override
            protected void doWork(Session session) {
                LocationInfo.Builder locationInfoBuilder = LocationInfo.newBuilder();
                locationInfoBuilder.setLocationId(locationId);

                int idStatic = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                                LocationObjectBehaviorTypesMapperType.STATIC);
                List<LocationObject> locationStaticObjects =
                        session.createCriteria(LocationObject.class)
                        .add(Restrictions.eq("locationObjectBehaviorTypeId", idStatic))
                        .add(Restrictions.eq("locationId", locationId)).list();
                locationInfoBuilder.setLocationStaticObjects(getStaticObjects(
                        locationStaticObjects, idStatic));

                int idDrawable = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        LocationObjectBehaviorTypesMapperType.DRAWABLE);
                List<LocationObject> locationDrawableObjects =
                        session.createCriteria(LocationObject.class)
                                .add(Restrictions.eq("locationObjectBehaviorTypeId", idDrawable))
                                .add(Restrictions.eq("locationId", locationId)).list();
                locationInfoBuilder.setLocationDrawableObject(getStaticObjects(
                        locationDrawableObjects, idDrawable));
            }
        }.execute();
    }

    private LocationInfo.StaticObjects getStaticObjects(List<LocationObject> locationObjects,
            int locationObjectBehaviorTypeId) {
        LocationInfo.StaticObjects.Builder staticObjectsBuilder
                = LocationInfo.StaticObjects.newBuilder();
        LocationInfo.StaticObjects.StaticObject.Builder staticObjectBuilder
                = LocationInfo.StaticObjects.StaticObject.newBuilder();

        for (LocationObject locationObject : locationObjects) {
            staticObjectBuilder.clear();

            staticObjectsBuilder.addObjects(staticObjectBuilder
                    .setObjectId(locationObject.getLocationObjectId())
                    .setPositionX(locationObject.getPositionX())
                    .setPositionY(locationObject.getPositionY())
                    .setObjectTypeId(locationObject.getLocationObjectTypeId()));
        }
        return staticObjectsBuilder.build();
    }
}
