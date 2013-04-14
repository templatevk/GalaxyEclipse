package arch.galaxyeclipse.server.protocol;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.types.*;

import java.util.*;

/**
 * Convenience class to build protobuf messages
 * <p/>
 * build* methods simply access the fields of the entity provided
 * load* methods retrieve data using {@see UnitOfWork}
 */
public class GeProtocolMessageFactory {
    private DictionaryTypesMapper dictionaryTypesMapper;

    // Helper functions' builders
    private LocationInfo.CachedObjects.Builder getStaticObjectsBuilder;
    private LocationInfo.CachedObjects.CachedObject.Builder getStaticObjectBuilder;

    private ShipStaticInfo.Item.Builder getItemBuilder;
    private ShipStaticInfo.Item.Engine.Builder getEngineBuilder;
    private ShipStaticInfo.Item.Bonus.Builder getBonusBuilder;
    private ShipStaticInfo.Item.Weapon.Builder getWeaponBuilder;

    public GeProtocolMessageFactory() {
        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(DictionaryTypesMapper.class);

        getStaticObjectsBuilder = LocationInfo.CachedObjects.newBuilder();
        getStaticObjectBuilder = LocationInfo.CachedObjects.CachedObject.newBuilder();

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
                .setBonusSlotsCount(shipType.getBonusSlotsCount());

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

        TypesMap.ItemType.Builder itemTypeBuilder = TypesMap.ItemType.newBuilder();
        for (ItemTypesMapperType itemTypesMapperType : ItemTypesMapperType.values()) {
            typesMapBuilder.addItemTypes(itemTypeBuilder.setName(itemTypesMapperType.toString())
                    .setId(dictionaryTypesMapper.getIdByItemType(itemTypesMapperType)));
        }

        TypesMap.WeaponType.Builder weaponTypeBuilder = TypesMap.WeaponType.newBuilder();
        for (WeaponTypesMapperType weaponTypesMapperType : WeaponTypesMapperType.values()) {
            typesMapBuilder.addWeaponTypes(weaponTypeBuilder.setName(weaponTypesMapperType.toString())
                    .setId(dictionaryTypesMapper.getIdByWeaponType(weaponTypesMapperType)));
        }

        TypesMap.LocationObjectType.Builder locationObjectTypeBuilder =
                TypesMap.LocationObjectType.newBuilder();
        for (LocationObjectTypesMapperType locationObjectTypesMapperType :
                LocationObjectTypesMapperType.values()) {
            typesMapBuilder.addLocationObjectTypes(locationObjectTypeBuilder
                    .setName(locationObjectTypesMapperType.toString())
                    .setId(dictionaryTypesMapper.getIdByLocationObjectType(
                            locationObjectTypesMapperType)));
        }

        return typesMapBuilder.build();
    }

    private LocationInfo.CachedObjects getCachedObjects(List<LocationObject> locationObjects) {
        getStaticObjectsBuilder.clear();
        getStaticObjectBuilder.clear();

        for (LocationObject locationObject : locationObjects) {
            getStaticObjectsBuilder.addObjects(getStaticObjectBuilder
                    .setObjectId(locationObject.getLocationObjectId())
                    .setPositionX(locationObject.getPositionX())
                    .setPositionY(locationObject.getPositionY())
                    .setObjectTypeId(locationObject.getLocationObjectTypeId()));
            getStaticObjectBuilder.clear();
        }
        return getStaticObjectsBuilder.build();
    }

    private ShipStaticInfo.Item getItem(Item item) {
        getItemBuilder.clear();
        getItemBuilder.setItemId(item.getItemId())
                .setName(item.getItemName())
                .setDescription(item.getItemDescription())
                .setPrice(item.getItemPrice())
                .setItemId(item.getItemTypeId());

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
