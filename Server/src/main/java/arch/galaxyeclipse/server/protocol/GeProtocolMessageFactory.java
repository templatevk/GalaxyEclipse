package arch.galaxyeclipse.server.protocol;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.data.model.LocationObject;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.types.*;

import java.util.*;

/**
 * Convenience class to build protobuf messages
 */
public class GeProtocolMessageFactory {
    private DictionaryTypesMapper dictionaryTypesMapper;

    // Helper functions' builders
    private LocationInfo.CachedObjects.Builder getStaticObjectsBuilder;
    private LocationInfo.LocationObject.Builder getLocationObjectBuilder;

    private ShipStaticInfo.Item.Builder getItemBuilder;
    private ShipStaticInfo.Item.Engine.Builder getEngineBuilder;
    private ShipStaticInfo.Item.Bonus.Builder getBonusBuilder;
    private ShipStaticInfo.Item.Weapon.Builder getWeaponBuilder;

    public GeProtocolMessageFactory() {
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);

        getStaticObjectsBuilder = LocationInfo.CachedObjects.newBuilder();
        getLocationObjectBuilder = LocationInfo.LocationObject.newBuilder();

        getItemBuilder = ShipStaticInfo.Item.newBuilder();
        getEngineBuilder = ShipStaticInfo.Item.Engine.newBuilder();
        getBonusBuilder = ShipStaticInfo.Item.Bonus.newBuilder();
        getWeaponBuilder = ShipStaticInfo.Item.Weapon.newBuilder();
    }

    public ShipStaticInfo createShipStaticInfo(final Player player) {
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
                .setBonusSlotsCount(shipType.getBonusSlotsCount())
                .setShipEngine(getItem(shipConfig.getEngine()));

        // inventory items, bonuses, weapons and engine
        for (InventoryItem inventoryItem : player.getInventoryItems()) {
            shipStaticInfoBuilder.addInventoryItems(getItem(inventoryItem.getItem()));
        }

        for (ShipConfigBonusSlot shipConfigBonusSlot : shipConfig.getShipConfigBonusSlots()) {
            shipStaticInfoBuilder.addShipBonus(getItem(shipConfigBonusSlot.getItem()));
        }

        for (ShipConfigWeaponSlot shipConfigWeaponSlot : shipConfig.getShipConfigWeaponSlots()) {
            shipStaticInfoBuilder.addShipWeapons(getItem(shipConfigWeaponSlot.getItem()));
        }

        return shipStaticInfoBuilder.build();
    }

    public LocationInfo createLocationInfo(Location location,
            List<LocationObject> locationCachedObjects) {

        return LocationInfo.newBuilder().setLocationId(location.getLocationId())
                .setName(location.getLocationName())
                .setHeight(location.getLocationHeight())
                .setWidth(location.getLocationWidth())
                .setLocationCachedObjects(getCachedObjects(locationCachedObjects)).build();
    }

    public TypesMap createTypesMap() {
        TypesMap.Builder typesMapBuilder = TypesMap.newBuilder();
        TypesMap.Type.Builder typeBuilder = TypesMap.Type.newBuilder();

        for (ItemTypesMapperType itemTypesMapperType : ItemTypesMapperType.values()) {
            typesMapBuilder.addItemTypes(typeBuilder.setName(itemTypesMapperType.toString())
                    .setId(dictionaryTypesMapper.getIdByItemType(itemTypesMapperType)));
        }

        for (WeaponTypesMapperType weaponTypesMapperType : WeaponTypesMapperType.values()) {
            typesMapBuilder.addWeaponTypes(typeBuilder.setName(weaponTypesMapperType.toString())
                    .setId(dictionaryTypesMapper.getIdByWeaponType(weaponTypesMapperType)));
        }

        for (LocationObjectTypesMapperType locationObjectTypesMapperType :
                LocationObjectTypesMapperType.values()) {
            typesMapBuilder.addLocationObjectTypes(typeBuilder
                    .setName(locationObjectTypesMapperType.toString())
                    .setId(dictionaryTypesMapper.getIdByLocationObjectType(
                            locationObjectTypesMapperType)));
        }

        return typesMapBuilder.build();
    }

    public ShipStateResponse createShipStateResponse(ShipState shipState,
            LocationObject locationObject) {
        return ShipStateResponse.newBuilder()
                .setHp(shipState.getShipStateHp())
                .setArmorDurability(shipState.getShipStateArmorDurability())
                .setRotationSpeed(shipState.getShipStateRotationSpeed())
                .setRotationAngle(shipState.getShipStateRotationAngle())
                .setMoveSpeed(shipState.getShipStateMoveSpeed())
                .setPositionX(locationObject.getPositionX())
                .setPositionY(locationObject.getPositionY())
                .setLocationObjectId(locationObject.getLocationObjectId())
                .build();
    }

    public DynamicObjectsResponse createDynamicObjectsResponse(List<LocationObject> locationObjects) {
        DynamicObjectsResponse.Builder dynamicObjectsResponseBuilder =
                DynamicObjectsResponse.newBuilder();

        for (LocationObject locationObject : locationObjects) {
            dynamicObjectsResponseBuilder.addObjects(getLocationObject(locationObject));
        }

        return dynamicObjectsResponseBuilder.build();
    }

    private LocationInfo.CachedObjects getCachedObjects(List<LocationObject> locationObjects) {
        for (LocationObject locationObject : locationObjects) {
            getStaticObjectsBuilder.addObjects(getLocationObject(locationObject));
        }
        return getStaticObjectsBuilder.build();
    }

    private LocationInfo.LocationObject getLocationObject(LocationObject locationObject) {
        return getLocationObjectBuilder
                .setObjectId(locationObject.getLocationObjectId())
                .setPositionX(locationObject.getPositionX())
                .setPositionY(locationObject.getPositionY())
                .setObjectTypeId(locationObject.getLocationObjectTypeId())
                .setNativeId(locationObject.getObjectNativeId()).build();
    }

    private ShipStaticInfo.Item getItem(Item item) {
        getItemBuilder.setItemId(item.getItemId())
                .setName(item.getItemName())
                .setDescription(item.getItemDescription())
                .setPrice(item.getItemPrice())
                .setItemTypeId(item.getItemTypeId());

        switch (dictionaryTypesMapper.getItemTypeById(item.getItemTypeId())) {
            case ENGINE:
                Engine engine = (Engine) item;
                getEngineBuilder.clear();
                getItemBuilder.setEngine(getEngineBuilder
                        .setMoveAccelerationBonus(engine.getMoveAccelerationBonus())
                        .setMoveMaxSpeedBonus(engine.getMoveMaxSpeedBonus())
                        .setRotationAccelerationBonus(engine.getRotationAccelerationBonus())
                        .setRotationMaxSpeedBonus(engine.getRotationMaxSpeedBonus()));
                break;
            case WEAPON:
                Weapon weapon = (Weapon) item;
                getItemBuilder.clear();
                getItemBuilder.setWeapon(getWeaponBuilder
                        .setDamage(weapon.getDamage())
                        .setDelaySpeed(weapon.getDelaySpeed())
                        .setBulletSpeed(weapon.getBulletSpeed())
                        .setMaxDistance(weapon.getMaxDistance())
                        .setEnergyCost(weapon.getEnergyCost()));
                break;
            case BONUS:
                Bonus bonus = (Bonus) item;
                getBonusBuilder.clear();
                getItemBuilder.setBonus(getBonusBuilder
                        .setBonusValue(bonus.getBonusValue())
                        .setBonusTypeId(bonus.getBonusTypeId()));
                break;
        }

        return getItemBuilder.build();
    }
}
