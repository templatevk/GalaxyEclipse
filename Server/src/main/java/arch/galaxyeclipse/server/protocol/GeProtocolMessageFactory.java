package arch.galaxyeclipse.server.protocol;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeCachedObjectsPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket.GeItemPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket.GeItemPacket.GeBonusPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket.GeItemPacket.GeEnginePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket.GeItemPacket.GeWeaponPacket;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.GeItemTypesMapperType;
import arch.galaxyeclipse.shared.types.GeLocationObjectTypesMapperType;
import arch.galaxyeclipse.shared.types.GeWeaponTypesMapperType;

import java.util.List;

/**
 * Convenience class to build protobuf messages
 */
public class GeProtocolMessageFactory {
    private GeDictionaryTypesMapper dictionaryTypesMapper;

    // Helper functions' builders
    private GeCachedObjectsPacket.Builder getStaticObjectsBuilder;
    private GeLocationObjectPacket.Builder getLocationObjectBuilder;

    private GeItemPacket.Builder getItemBuilder;
    private GeEnginePacket.Builder getEngineBuilder;
    private GeBonusPacket.Builder getBonusBuilder;
    private GeWeaponPacket.Builder getWeaponBuilder;

    public GeProtocolMessageFactory() {
        dictionaryTypesMapper = GeContextHolder.getBean(GeDictionaryTypesMapper.class);

        getStaticObjectsBuilder = GeCachedObjectsPacket.newBuilder();
        getLocationObjectBuilder = GeLocationObjectPacket.newBuilder();

        getItemBuilder = GeItemPacket.newBuilder();
        getEngineBuilder = GeEnginePacket.newBuilder();
        getBonusBuilder = GeBonusPacket.newBuilder();
        getWeaponBuilder = GeWeaponPacket.newBuilder();
    }

    public GeShipStaticInfoPacket createShipStaticInfo(final GePlayer player) {
        GeShipConfig shipConfig = player.getShipConfig();
        GeShipType shipType = shipConfig.getShipType();

        GeShipStaticInfoPacket.Builder shipStaticInfoBuilder = GeShipStaticInfoPacket.newBuilder();
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
        for (GeInventoryItem inventoryItem : player.getInventoryItems()) {
            shipStaticInfoBuilder.addInventoryItems(getItem(inventoryItem.getItem()));
        }

        for (GeShipConfigBonusSlot shipConfigBonusSlot : shipConfig.getShipConfigBonusSlots()) {
            shipStaticInfoBuilder.addShipBonus(getItem(shipConfigBonusSlot.getItem()));
        }

        for (GeShipConfigWeaponSlot shipConfigWeaponSlot : shipConfig.getShipConfigWeaponSlots()) {
            shipStaticInfoBuilder.addShipWeapons(getItem(shipConfigWeaponSlot.getItem()));
        }

        return shipStaticInfoBuilder.build();
    }

    public GeLocationInfoPacket createLocationInfo(GeLocation location,
            List<GeLocationObject> locationCachedObjects) {

        return GeLocationInfoPacket.newBuilder().setLocationId(location.getLocationId())
                .setName(location.getLocationName())
                .setHeight(location.getLocationHeight())
                .setWidth(location.getLocationWidth())
                .setLocationCachedObjects(getCachedObjects(locationCachedObjects)).build();
    }

    public GeTypesMapPacket createTypesMap() {
        GeTypesMapPacket.Builder typesMapBuilder = GeTypesMapPacket.newBuilder();
        GeTypesMapPacket.Type.Builder typeBuilder = GeTypesMapPacket.Type.newBuilder();

        for (GeItemTypesMapperType itemTypesMapperType : GeItemTypesMapperType.values()) {
            typesMapBuilder.addItemTypes(typeBuilder.setName(itemTypesMapperType.toString())
                    .setId(dictionaryTypesMapper.getIdByItemType(itemTypesMapperType)));
        }

        for (GeWeaponTypesMapperType weaponTypesMapperType : GeWeaponTypesMapperType.values()) {
            typesMapBuilder.addWeaponTypes(typeBuilder.setName(weaponTypesMapperType.toString())
                    .setId(dictionaryTypesMapper.getIdByWeaponType(weaponTypesMapperType)));
        }

        for (GeLocationObjectTypesMapperType locationObjectTypesMapperType :
                GeLocationObjectTypesMapperType.values()) {
            typesMapBuilder.addLocationObjectTypes(typeBuilder
                    .setName(locationObjectTypesMapperType.toString())
                    .setId(dictionaryTypesMapper.getIdByLocationObjectType(
                            locationObjectTypesMapperType)));
        }

        return typesMapBuilder.build();
    }

    public GeShipStateResponse createShipStateResponse(GeShipState shipState,
            GeLocationObject locationObject) {
        return GeShipStateResponse.newBuilder()
                .setHp(shipState.getShipStateHp())
                .setArmorDurability(shipState.getShipStateArmorDurability())
                .setRotationSpeed(shipState.getShipStateRotationSpeed())
                .setMoveSpeed(shipState.getShipStateMoveSpeed())
                .setPositionX(locationObject.getPositionX())
                .setPositionY(locationObject.getPositionY())
                .setRotationAngle(locationObject.getRotationAngle())
                .setLocationObjectId(locationObject.getLocationObjectId())
                .setEnergy(shipState.getShipStateEnergy())
                .build();
    }

    public GeDynamicObjectsResponse createDynamicObjectsResponse(List<GeLocationObject> locationObjects) {
        GeDynamicObjectsResponse.Builder dynamicObjectsResponseBuilder =
                GeDynamicObjectsResponse.newBuilder();

        for (GeLocationObject locationObject : locationObjects) {
            dynamicObjectsResponseBuilder.addObjects(getLocationObject(locationObject));
        }

        return dynamicObjectsResponseBuilder.build();
    }

    public GeLocationObjectPacket getLocationObject(GeLocationObject locationObject) {
        return getLocationObjectBuilder
                .setObjectId(locationObject.getLocationObjectId())
                .setPositionX(locationObject.getPositionX())
                .setPositionY(locationObject.getPositionY())
                .setRotationAngle(locationObject.getRotationAngle())
                .setObjectTypeId(locationObject.getLocationObjectTypeId())
                .setNativeId(locationObject.getObjectNativeId()).build();
    }

    private GeCachedObjectsPacket getCachedObjects(List<GeLocationObject> locationObjects) {
        for (GeLocationObject locationObject : locationObjects) {
            getStaticObjectsBuilder.addObjects(getLocationObject(locationObject));
        }
        return getStaticObjectsBuilder.build();
    }

    private GeItemPacket getItem(GeItem item) {
        getItemBuilder.setItemId(item.getItemId())
                .setName(item.getItemName())
                .setDescription(item.getItemDescription())
                .setPrice(item.getItemPrice())
                .setItemTypeId(item.getItemTypeId());

        switch (dictionaryTypesMapper.getItemTypeById(item.getItemTypeId())) {
            case ENGINE:
                GeEngine engine = (GeEngine) item;
                getEngineBuilder.clear();
                getItemBuilder.setEngine(getEngineBuilder
                        .setMoveAccelerationBonus(engine.getMoveAccelerationBonus())
                        .setMoveMaxSpeedBonus(engine.getMoveMaxSpeedBonus())
                        .setRotationAccelerationBonus(engine.getRotationAccelerationBonus())
                        .setRotationMaxSpeedBonus(engine.getRotationMaxSpeedBonus()));
                break;
            case WEAPON:
                GeWeapon weapon = (GeWeapon) item;
                getItemBuilder.clear();
                getItemBuilder.setWeapon(getWeaponBuilder
                        .setDamage(weapon.getDamage())
                        .setDelaySpeed(weapon.getDelaySpeed())
                        .setBulletSpeed(weapon.getBulletSpeed())
                        .setMaxDistance(weapon.getMaxDistance())
                        .setEnergyCost(weapon.getEnergyCost()));
                break;
            case BONUS:
                GeBonus bonus = (GeBonus) item;
                getBonusBuilder.clear();
                getItemBuilder.setBonus(getBonusBuilder
                        .setBonusValue(bonus.getBonusValue())
                        .setBonusTypeId(bonus.getBonusTypeId()));
                break;
        }

        return getItemBuilder.build();
    }
}
