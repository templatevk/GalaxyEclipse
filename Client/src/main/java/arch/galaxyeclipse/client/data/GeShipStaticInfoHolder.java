package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.network.GeServerPacketListener;
import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket.GeItemPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket.GeItemPacket.GeBonusPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket.GeItemPacket.GeEnginePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket.GeItemPacket.GeWeaponPacket;
import arch.galaxyeclipse.shared.protocol.GeShipStaticInfoCommand;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static arch.galaxyeclipse.shared.context.GeContextHolder.getBean;
import static java.util.Arrays.asList;
import static org.springframework.util.SerializationUtils.deserialize;

/**
 *
 */
@Slf4j
public class GeShipStaticInfoHolder extends GeServerPacketListener {

    private GeShipStaticInfoPacket ssiPacket = GeShipStaticInfoPacket.getDefaultInstance();
    private GeDictionaryTypesMapper dictionaryTypesMapper;

    GeShipStaticInfoHolder() {
        dictionaryTypesMapper = getBean(GeDictionaryTypesMapper.class);

        getBean(IGeClientNetworkManager.class).addPacketListener(this);
    }

    @Override
    protected void onPacketReceivedImpl(GePacket packet) {
        switch (packet.getType()) {
            case SHIP_STATIC_INFO_COMMAND:
                GeShipStaticInfoCommand command = (GeShipStaticInfoCommand)
                        deserialize(packet.getGameInfoCommandHolder()
                                .getSerializedCommand().toByteArray());
                command.perform(ssiPacket);
                break;
        }
    }

    @Override
    public List<GePacket.Type> getPacketTypes() {
        return asList(GePacket.Type.SHIP_STATIC_INFO_COMMAND);
    }

    public void setShipStaticInfo(GeShipStaticInfoPacket ssiPacket) {
        if (GeShipStaticInfoHolder.log.isInfoEnabled()) {
            GeShipStaticInfoHolder.log.info("Updating ship static info");
        }
        this.ssiPacket = ssiPacket;

        if (GeShipStaticInfoHolder.log.isDebugEnabled()) {
            GeShipStaticInfoHolder.log.debug("\tName " + ssiPacket.getName());
            GeShipStaticInfoHolder.log.debug("\tArmor " + ssiPacket.getArmor());
            GeShipStaticInfoHolder.log.debug("\tArmor durability " + ssiPacket.getArmorDurability());
            GeShipStaticInfoHolder.log.debug("\tEnergy max " + ssiPacket.getEnergyMax());
            GeShipStaticInfoHolder.log.debug("\tEnergy regen " + ssiPacket.getEnergyRegen());
            GeShipStaticInfoHolder.log.debug("\tHp max " + ssiPacket.getHpMax());
            GeShipStaticInfoHolder.log.debug("\tHp regen " + ssiPacket.getHpRegen());
            GeShipStaticInfoHolder.log.debug("\tMove max speed " + ssiPacket.getMoveMaxSpeed());
            GeShipStaticInfoHolder.log.debug("\tMove acceleration speed " + ssiPacket.getMoveAccelerationSpeed());
            GeShipStaticInfoHolder.log.debug("\tRotation max speed " + ssiPacket.getRotationMaxSpeed());
            GeShipStaticInfoHolder.log.debug("\tRotation acceleration speed " + ssiPacket.getRotationAcceleration());

            GeShipStaticInfoHolder.log.debug("\tEngine");
            outputItemInfo(ssiPacket.getShipEngine());

            GeShipStaticInfoHolder.log.debug("\tInventory items");
            for (GeShipStaticInfoPacket.GeItemPacket item : ssiPacket
                    .getInventoryItemsList()) {
                outputItemInfo(item);
            }

            GeShipStaticInfoHolder.log.debug("\tBonus slots");
            for (GeShipStaticInfoPacket.GeItemPacket item : ssiPacket
                    .getShipBonusList()) {
                outputItemInfo(item);
            }

            GeShipStaticInfoHolder.log.debug("\tWeapon slots");
            for (GeShipStaticInfoPacket.GeItemPacket item : ssiPacket
                    .getShipWeaponsList()) {
                outputItemInfo(item);
            }
        }
    }

    private void outputItemInfo(GeShipStaticInfoPacket.GeItemPacket item) {
        GeShipStaticInfoHolder.log.debug("\t\tName " + item.getName());
        GeShipStaticInfoHolder.log.debug("\t\tDescription " + item.getDescription());
        GeShipStaticInfoHolder.log.debug("\t\tPrice " + item.getPrice());

        switch (dictionaryTypesMapper.getItemTypeById(item.getItemTypeId())) {
            case ENGINE:
                GeEnginePacket engine = item.getEngine();
                GeShipStaticInfoHolder.log.debug("\t\tMove acceleration bonus " + engine.getMoveAccelerationBonus());
                GeShipStaticInfoHolder.log.debug("\t\tMove max speed bonus " + engine.getMoveMaxSpeedBonus());
                GeShipStaticInfoHolder.log.debug("\t\tRotation acceleration bonus " + engine.getRotationAccelerationBonus());
                GeShipStaticInfoHolder.log.debug("\t\tRotation max speed bonus " + engine.getRotationMaxSpeedBonus());
                break;
            case WEAPON:
                GeWeaponPacket weapon = item.getWeapon();
                GeShipStaticInfoHolder.log.debug("\t\tBullet speed " + weapon.getBulletSpeed());
                GeShipStaticInfoHolder.log.debug("\t\tDamage " + weapon.getDamage());
                GeShipStaticInfoHolder.log.debug("\t\tDelay speed " + weapon.getDelaySpeed());
                GeShipStaticInfoHolder.log.debug("\t\tEnergy cost " + weapon.getEnergyCost());
                GeShipStaticInfoHolder.log.debug("\t\tMax distance " + weapon.getMaxDistance());
                break;
            case BONUS:
                GeBonusPacket bonus = item.getBonus();
                GeShipStaticInfoHolder.log.debug("\t\tValue " + bonus.getBonusValue());
                GeShipStaticInfoHolder.log.debug("\t\tType " + dictionaryTypesMapper.getBonusTypeById(
                        bonus.getBonusTypeId()));
                break;
            case SALE:
                break;
            case MONEY:
                break;
        }
    }

    public float getMoveMaxSpeed() {
        return ssiPacket.getMoveMaxSpeed();
    }

    public float getRotationMaxSpeed() {
        return ssiPacket.getRotationMaxSpeed();
    }

    public float getMoveAccelerationSpeed() {
        return ssiPacket.getMoveAccelerationSpeed();
    }

    public float getRotationAcceleration() {
        return ssiPacket.getRotationAcceleration();
    }

    public int getArmor() {
        return ssiPacket.getArmor();
    }

    public int getEnergyMax() {
        return ssiPacket.getEnergyMax();
    }

    public int getHpMax() {
        return ssiPacket.getHpMax();
    }

    public int getEnergyRegen() {
        return ssiPacket.getEnergyRegen();
    }

    public int getHpRegen() {
        return ssiPacket.getHpRegen();
    }

    public int getArmorDurability() {
        return ssiPacket.getArmorDurability();
    }

    public int getWeaponSlotsCount() {
        return ssiPacket.getWeaponSlotsCount();
    }

    public int getBonusSlotsCount() {
        return ssiPacket.getBonusSlotsCount();
    }

    public List<GeItemPacket> getInventoryItemsList() {
        return ssiPacket.getInventoryItemsList();
    }

    public List<GeItemPacket> getShipBonusList() {
        return ssiPacket.getShipBonusList();
    }

    public List<GeItemPacket> getShipWeaponsList() {
        return ssiPacket.getShipWeaponsList();
    }

    public GeItemPacket getShipEngine() {
        return ssiPacket.getShipEngine();
    }
}
