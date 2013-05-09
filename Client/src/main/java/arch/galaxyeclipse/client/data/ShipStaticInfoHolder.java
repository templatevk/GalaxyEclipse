package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.network.ServerPacketListener;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket.BonusPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket.EnginePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket.ItemPacket.WeaponPacket;
import arch.galaxyeclipse.shared.protocol.ShipStaticInfoCommand;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
@Slf4j
public class ShipStaticInfoHolder extends ServerPacketListener {
    @Getter
    private GeProtocol.ShipStaticInfoPacket ShipStaticInfoPacket;
    private DictionaryTypesMapper dictionaryTypesMapper;

    ShipStaticInfoHolder() {
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);

        ContextHolder.getBean(IClientNetworkManager.class).addPacketListener(this);
    }

    @Override
    protected void onPacketReceivedImpl(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case SHIP_STATIC_INFO_COMMAND:
                ShipStaticInfoCommand command = (ShipStaticInfoCommand)
                        SerializationUtils.deserialize(packet.getGameInfoCommandHolder()
                                .getSerializedCommand().toByteArray());
                command.perform(ShipStaticInfoPacket);
                break;
        }
    }

    @Override
    public List<GeProtocol.Packet.Type> getPacketTypes() {
        return Arrays.asList(GeProtocol.Packet.Type.SHIP_STATIC_INFO_COMMAND);
    }

    public void setShipStaticInfo(GeProtocol.ShipStaticInfoPacket ShipStaticInfoPacket) {
        if (log.isInfoEnabled()) {
            log.info("Updating ship static info");
        }
        this.ShipStaticInfoPacket = ShipStaticInfoPacket;

        if (log.isDebugEnabled()) {
            log.debug("\tName " + ShipStaticInfoPacket.getName());
            log.debug("\tArmor " + ShipStaticInfoPacket.getArmor());
            log.debug("\tArmor durability " + ShipStaticInfoPacket.getArmorDurability());
            log.debug("\tEnergy max " + ShipStaticInfoPacket.getEnergyMax());
            log.debug("\tEnergy regen " + ShipStaticInfoPacket.getEnergyRegen());
            log.debug("\tHp max " + ShipStaticInfoPacket.getHpMax());
            log.debug("\tHp regen " + ShipStaticInfoPacket.getHpRegen());
            log.debug("\tMove max speed " + ShipStaticInfoPacket.getMoveMaxSpeed());
            log.debug("\tMove acceleration speed " + ShipStaticInfoPacket.getMoveAccelerationSpeed());
            log.debug("\tRotation max speed " + ShipStaticInfoPacket.getRotationMaxSpeed());
            log.debug("\tRotation acceleration speed " + ShipStaticInfoPacket.getRotationAcceleration());

            log.debug("\tEngine");
            outputItemInfo(ShipStaticInfoPacket.getShipEngine());

            log.debug("\tInventory items");
            for (GeProtocol.ShipStaticInfoPacket.ItemPacket item : ShipStaticInfoPacket
                    .getInventoryItemsList()) {
                outputItemInfo(item);
            }

            log.debug("\tBonus slots");
            for (GeProtocol.ShipStaticInfoPacket.ItemPacket item : ShipStaticInfoPacket
                    .getShipBonusList()) {
                outputItemInfo(item);
            }

            log.debug("\tWeapon slots");
            for (GeProtocol.ShipStaticInfoPacket.ItemPacket item : ShipStaticInfoPacket
                    .getShipWeaponsList()) {
                outputItemInfo(item);
            }
        }
    }

    private void outputItemInfo(GeProtocol.ShipStaticInfoPacket.ItemPacket item) {
        log.debug("\t\tName" + item.getName());
        log.debug("\t\tDescription" + item.getDescription());
        log.debug("\t\tPrice" + item.getPrice());

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
}
