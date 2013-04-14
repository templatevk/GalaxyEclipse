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
 * Convenience class to build protobuf messages
 *
 * build* methods simply access the fields of the entity provided
 * load* methods retrieve data using {@see UnitOfWork}
 */
public class GeProtocolMessageFactory {
    private DictionaryTypesMapper dictionaryTypesMapper;

    public GeProtocolMessageFactory() {
        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(DictionaryTypesMapper.class);
    }

    public ShipStaticInfo buildShipStaticInfo(final Player player) {
        ShipConfig shipConfig = player.getShipConfig();
        ShipType shipType = shipConfig.getShipType();

        ShipStaticInfo.Builder shipStaticInfoBuilder = ShipStaticInfo.newBuilder();
        shipStaticInfoBuilder
                // ship config values
                .setMoveMaxSpeed(shipConfig.getShipConfigMoveMaxSpeed())
                .setRotationMaxSpeed(shipConfig.getShipConfigRotationMaxSpeed())
                .setMoveAccelerationSpeed(shipConfig.getShipConfigMoveAcceleration())
                .setRotationAcceleration(shipConfig.getShipConfigRotationAcceleration())
                .setArmor(shipConfig.getShipConfigArmor())
                .setEnergyMax(shipConfig.getShipConfigEnergyMax())
                .setHpMax(shipConfig.getShipConfigHpMax())
                .setEnergyRegen(shipConfig.getShipConfigEnergyRegen())
                .setHpRegen(shipConfig.getShipConfigHpRegen())
                // ship types values
                .setName(shipType.getShipTypeName())
                .setArmorDurability(shipType.getShipTypeArmorDurability())
                .setWeaponSlotsCount(shipType.getWeaponSlotsCount())
                .setBonusSlotsCount(shipType.getBonusSlotsCount());

        // inventory items, bonuses, weapons and engine
        ShipStaticInfo.Item.Builder itemBuilder = ShipStaticInfo.Item.newBuilder();
        for (InventoryItem inventoryItem : player.getInventoryItems()) {
            Item item = inventoryItem.getItem();


//            itemBuilder.setItemId(inventoryItem.getItemId())
//                    .setName(item.getItemName())
//                    .setDescription(item.getItemDescription())
//                    .setPrice(item.getItemPrice())
//                    .setItemId(item.getItemTypeId())

            shipStaticInfoBuilder.addInventoryItems(itemBuilder);
            itemBuilder.clear();
        }

        return shipStaticInfoBuilder.build();
    }

    public LocationInfo loadLocationInfo(final int locationId) {
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
