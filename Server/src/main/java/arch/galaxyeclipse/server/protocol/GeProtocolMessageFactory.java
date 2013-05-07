package arch.galaxyeclipse.server.protocol;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.CachedObjectsPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket.BonusPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket.EnginePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket.WeaponPacket;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.ItemTypesMapperType;
import arch.galaxyeclipse.shared.types.LocationObjectTypesMapperType;
import arch.galaxyeclipse.shared.types.WeaponTypesMapperType;

import java.util.List;

/**
 * Convenience class to build protobuf messages
 */
public class GeProtocolMessageFactory {
    private DictionaryTypesMapper dictionaryTypesMapper;

    // Helper functions' builders
    private CachedObjectsPacket.Builder getStaticObjectsBuilder;
    private LocationObjectPacket.Builder getLocationObjectBuilder;

    private ItemPacket.Builder getItemBuilder;
    private EnginePacket.Builder getEngineBuilder;
    private BonusPacket.Builder getBonusBuilder;
    private WeaponPacket.Builder getWeaponBuilder;

    public GeProtocolMessageFactory() {
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);

        getStaticObjectsBuilder = CachedObjectsPacket.newBuilder();
        getLocationObjectBuilder = LocationObjectPacket.newBuilder();

        getItemBuilder = ItemPacket.newBuilder();
        getEngineBuilder = EnginePacket.newBuilder();
        getBonusBuilder = BonusPacket.newBuilder();
        getWeaponBuilder = WeaponPacket.newBuilder();
    }

    public ShipStaticInfoPacket createShipStaticInfo(final Player player) {
        ShipConfig shipConfig = player.getShipConfig();
        ShipType shipType = shipConfig.getShipType();

        ShipStaticInfoPacket.Builder shipStaticInfoBuilder = ShipStaticInfoPacket.newBuilder();
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

    public LocationInfoPacket createLocationInfo(Location location,
            List<LocationObject> locationCachedObjects) {

        return LocationInfoPacket.newBuilder().setLocationId(location.getLocationId())
                .setName(location.getLocationName())
                .setHeight(location.getLocationHeight())
                .setWidth(location.getLocationWidth())
                .setLocationCachedObjects(getCachedObjects(locationCachedObjects)).build();
    }

    public TypesMapPacket createTypesMap() {
        TypesMapPacket.Builder typesMapBuilder = TypesMapPacket.newBuilder();
        TypesMapPacket.Type.Builder typeBuilder = TypesMapPacket.Type.newBuilder();

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
                .setMoveSpeed(shipState.getShipStateMoveSpeed())
                .setPositionX(locationObject.getPositionX())
                .setPositionY(locationObject.getPositionY())
                .setRotationAngle(locationObject.getRotationAngle())
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

    public LocationObjectPacket getLocationObject(LocationObject locationObject) {
        return getLocationObjectBuilder
                .setObjectId(locationObject.getLocationObjectId())
                .setPositionX(locationObject.getPositionX())
                .setPositionY(locationObject.getPositionY())
                .setRotationAngle(locationObject.getRotationAngle())
                .setObjectTypeId(locationObject.getLocationObjectTypeId())
                .setNativeId(locationObject.getObjectNativeId()).build();
    }

    private CachedObjectsPacket getCachedObjects(List<LocationObject> locationObjects) {
        for (LocationObject locationObject : locationObjects) {
            getStaticObjectsBuilder.addObjects(getLocationObject(locationObject));
        }
        return getStaticObjectsBuilder.build();
    }

    private ItemPacket getItem(Item item) {
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
