package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.types.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.util.*;

import java.util.*;

/**
 *
 */
@Slf4j
public class ShipStaticInfoHolder extends ServerPacketListener {
    @Getter
    private GeProtocol.ShipStaticInfo shipStaticInfo;
    private DictionaryTypesMapper dictionaryTypesMapper;

    ShipStaticInfoHolder() {
        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(DictionaryTypesMapper.class);
    }

    @Override
    protected void onPacketReceivedImpl(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case SHIP_STATIC_INFO_COMMAND:
                ShipStaticInfoCommand command = (ShipStaticInfoCommand)SerializationUtils.deserialize(
                        packet.getGameInfoCommandHolder().getSerializedCommand().toByteArray());
                command.perform(shipStaticInfo);
                break;
        }
    }

    @Override
    public List<GeProtocol.Packet.Type> getPacketTypes() {
        return Arrays.asList(GeProtocol.Packet.Type.SHIP_STATIC_INFO_COMMAND);
    }

    public void setShipStaticInfo(GeProtocol.ShipStaticInfo shipStaticInfo) {
        if (log.isInfoEnabled()) {
            log.info("Updating ship static info");
        }
        this.shipStaticInfo = shipStaticInfo;

        if (log.isDebugEnabled()) {
            log.debug("\tName " + shipStaticInfo.getName());
            log.debug("\tArmor " + shipStaticInfo.getArmor());
            log.debug("\tArmor durability " + shipStaticInfo.getArmorDurability());
            log.debug("\tEnergy max " + shipStaticInfo.getEnergyMax());
            log.debug("\tEnergy regen " + shipStaticInfo.getEnergyRegen());
            log.debug("\tHp max " + shipStaticInfo.getHpMax());
            log.debug("\tHp regen " + shipStaticInfo.getHpRegen());
            log.debug("\tMove max speed " + shipStaticInfo.getMoveMaxSpeed());
            log.debug("\tMove acceleration speed " + shipStaticInfo.getMoveAccelerationSpeed());
            log.debug("\tRotation max speed " + shipStaticInfo.getRotationMaxSpeed());
            log.debug("\tRotation acceleration speed " + shipStaticInfo.getRotationAcceleration());

            log.debug("\tEngine");
            outputItemInfo(shipStaticInfo.getShipEngine());

            log.debug("\tInventory items");
            for (GeProtocol.ShipStaticInfo.Item item : shipStaticInfo.getInventoryItemsList()) {
                outputItemInfo(item);
            }

            log.debug("\tBonus slots");
            for (GeProtocol.ShipStaticInfo.Item item : shipStaticInfo.getShipBonusList()) {
                outputItemInfo(item);
            }

            log.debug("\tWeapon slots");
            for (GeProtocol.ShipStaticInfo.Item item : shipStaticInfo.getShipWeaponsList()) {
                outputItemInfo(item);
            }
        }
    }

    private void outputItemInfo(GeProtocol.ShipStaticInfo.Item item) {
        log.debug("\t\tName" + item.getName());
        log.debug("\t\tDescription" + item.getDescription());
        log.debug("\t\tPrice" + item.getPrice());

        switch (dictionaryTypesMapper.getItemTypeById(item.getItemTypeId())) {
            case ENGINE:
                GeProtocol.ShipStaticInfo.Item.Engine engine = item.getEngine();
                log.debug("\t\tMove acceleration bonus " + engine.getMoveAccelerationBonus());
                log.debug("\t\tMove max speed bonus " + engine.getMoveMaxSpeedBonus());
                log.debug("\t\tRotation acceleration bonus " + engine.getRotationAccelerationBonus());
                log.debug("\t\tRotation max speed bonus " + engine.getRotationMaxSpeedBonus());
                break;
            case WEAPON:
                GeProtocol.ShipStaticInfo.Item.Weapon weapon = item.getWeapon();
                log.debug("\t\tBullet speed " + weapon.getBulletSpeed());
                log.debug("\t\tDamage " + weapon.getDamage());
                log.debug("\t\tDelay speed " + weapon.getDelaySpeed());
                log.debug("\t\tEnergy cost " + weapon.getEnergyCost());
                log.debug("\t\tMax distance " + weapon.getMaxDistance());
                break;
            case BONUS:
                GeProtocol.ShipStaticInfo.Item.Bonus bonus = item.getBonus();
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
}
