package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.network.ServerPacketListener;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket.BonusPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket.EnginePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket.WeaponPacket;
import arch.galaxyeclipse.shared.protocol.ShipStaticInfoCommand;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static arch.galaxyeclipse.shared.context.ContextHolder.getBean;
import static arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import static arch.galaxyeclipse.shared.protocol.GeProtocol.Packet.Type.SHIP_STATIC_INFO_COMMAND;
import static java.util.Arrays.asList;
import static org.springframework.util.SerializationUtils.deserialize;

/**
 *
 */
@Slf4j
public class ShipStaticInfoHolder extends ServerPacketListener {

    private ShipStaticInfoPacket ssiPacket = ShipStaticInfoPacket.getDefaultInstance();
    private DictionaryTypesMapper dictionaryTypesMapper;

    ShipStaticInfoHolder() {
        dictionaryTypesMapper = getBean(DictionaryTypesMapper.class);

        getBean(IClientNetworkManager.class).addPacketListener(this);
    }

    @Override
    protected void onPacketReceivedImpl(Packet packet) {
        switch (packet.getType()) {
            case SHIP_STATIC_INFO_COMMAND:
                ShipStaticInfoCommand command = (ShipStaticInfoCommand)
                        deserialize(packet.getGameInfoCommandHolder()
                                .getSerializedCommand().toByteArray());
                command.perform(ssiPacket);
                break;
        }
    }

    @Override
    public List<Packet.Type> getPacketTypes() {
        return asList(SHIP_STATIC_INFO_COMMAND);
    }

    public void setShipStaticInfo(ShipStaticInfoPacket ssiPacket) {
        if (log.isInfoEnabled()) {
            log.info("Updating ship static info");
        }
        this.ssiPacket = ssiPacket;

        if (log.isDebugEnabled()) {
            log.debug("\tName " + ssiPacket.getName());
            log.debug("\tArmor " + ssiPacket.getArmor());
            log.debug("\tArmor durability " + ssiPacket.getArmorDurability());
            log.debug("\tEnergy max " + ssiPacket.getEnergyMax());
            log.debug("\tEnergy regen " + ssiPacket.getEnergyRegen());
            log.debug("\tHp max " + ssiPacket.getHpMax());
            log.debug("\tHp regen " + ssiPacket.getHpRegen());
            log.debug("\tMove max speed " + ssiPacket.getMoveMaxSpeed());
            log.debug("\tMove acceleration speed " + ssiPacket.getMoveAccelerationSpeed());
            log.debug("\tRotation max speed " + ssiPacket.getRotationMaxSpeed());
            log.debug("\tRotation acceleration speed " + ssiPacket.getRotationAcceleration());

            log.debug("\tEngine");
            outputItemInfo(ssiPacket.getShipEngine());

            log.debug("\tInventory items");
            for (GeProtocol.ShipStaticInfoPacket.ItemPacket item : ssiPacket
                    .getInventoryItemsList()) {
                outputItemInfo(item);
            }

            log.debug("\tBonus slots");
            for (GeProtocol.ShipStaticInfoPacket.ItemPacket item : ssiPacket
                    .getShipBonusList()) {
                outputItemInfo(item);
            }

            log.debug("\tWeapon slots");
            for (GeProtocol.ShipStaticInfoPacket.ItemPacket item : ssiPacket
                    .getShipWeaponsList()) {
                outputItemInfo(item);
            }
        }
    }

    private void outputItemInfo(GeProtocol.ShipStaticInfoPacket.ItemPacket item) {
        log.debug("\t\tName " + item.getName());
        log.debug("\t\tDescription " + item.getDescription());
        log.debug("\t\tPrice " + item.getPrice());

        switch (dictionaryTypesMapper.getItemTypeById(item.getItemTypeId())) {
            case ENGINE:
                EnginePacket engine = item.getEngine();
                log.debug("\t\tMove acceleration bonus " + engine.getMoveAccelerationBonus());
                log.debug("\t\tMove max speed bonus " + engine.getMoveMaxSpeedBonus());
                log.debug("\t\tRotation acceleration bonus " + engine.getRotationAccelerationBonus());
                log.debug("\t\tRotation max speed bonus " + engine.getRotationMaxSpeedBonus());
                break;
            case WEAPON:
                WeaponPacket weapon = item.getWeapon();
                log.debug("\t\tBullet speed " + weapon.getBulletSpeed());
                log.debug("\t\tDamage " + weapon.getDamage());
                log.debug("\t\tDelay speed " + weapon.getDelaySpeed());
                log.debug("\t\tEnergy cost " + weapon.getEnergyCost());
                log.debug("\t\tMax distance " + weapon.getMaxDistance());
                break;
            case BONUS:
                BonusPacket bonus = item.getBonus();
                log.debug("\t\tValue " + bonus.getBonusValue());
                log.debug("\t\tType " + dictionaryTypesMapper.getBonusTypeById(
                        bonus.getBonusTypeId()));
                break;
            case SALE:
                break;
            case MONEY:
                break;
        }
    }

    public int getMoveMaxSpeed() {
        return ssiPacket.getMoveMaxSpeed();
    }

    public float getRotationMaxSpeed() {
        return ssiPacket.getRotationMaxSpeed();
    }

    public int getMoveAccelerationSpeed() {
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

    public List<ItemPacket> getInventoryItemsList() {
        return ssiPacket.getInventoryItemsList();
    }

    public List<ItemPacket> getShipBonusList() {
        return ssiPacket.getShipBonusList();
    }

    public List<ItemPacket> getShipWeaponsList() {
        return ssiPacket.getShipWeaponsList();
    }

    public ItemPacket getShipEngine() {
        return ssiPacket.getShipEngine();
    }
}
