package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.GeDynamicObjectsHolder.GeLocationObjectsHolder;
import arch.galaxyeclipse.server.data.GeHibernateUnitOfWork;
import arch.galaxyeclipse.server.data.GePlayerInfoHolder;
import arch.galaxyeclipse.server.data.model.GeShipConfig;
import arch.galaxyeclipse.server.network.IGeServerChannelHandler;
import arch.galaxyeclipse.shared.GeConstants;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket.Builder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStateResponse;
import arch.galaxyeclipse.shared.thread.GeTaskRunnablePair;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.GeLocationObjectBehaviorTypesMapperType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

/**
 * Handles packets of authenticated players.
 */
@Slf4j
class GeFlightPacketHandler implements IGeChannelAwarePacketHandler {

    private IGeServerChannelHandler serverChannelHandler;
    private GeDictionaryTypesMapper dictionaryTypesMapper;

    private GePlayerInfoHolder playerInfoHolder;
    private GeShipConfig shipConfig;
    private GeShipStateResponse.Builder ssrBuilder;

    private GeFlightPacketHandler.HpRegenHandler hpRegenHandler;
    private GeFlightPacketHandler.EnergyRegenHandler energyRegenHandler;

    public GeFlightPacketHandler(IGeServerChannelHandler serverChannelHandler) {
        this.serverChannelHandler = serverChannelHandler;

        dictionaryTypesMapper = GeContextHolder.getBean(GeDictionaryTypesMapper.class);
        playerInfoHolder = serverChannelHandler.getPlayerInfoHolder();
        ssrBuilder = playerInfoHolder.getSsrBuilder();
        shipConfig = playerInfoHolder.getShipConfig();

        hpRegenHandler = new HpRegenHandler();
        hpRegenHandler.start();
        energyRegenHandler = new EnergyRegenHandler();
        energyRegenHandler.start();
    }

    @Override
    public boolean handle(GePacket packet) {
        return false;
    }

    @Override
    public void onChannelClosed() {
        if (GeFlightPacketHandler.log.isDebugEnabled()) {
            GeFlightPacketHandler.log.debug("Channel closed during flight mode, hibernating player");
        }

        Builder lopBuilder = playerInfoHolder.getLopBuilder();
        GeLocationObjectsHolder locationObjectsHolder = playerInfoHolder.getLocationObjectsHolder();
        locationObjectsHolder.removeLopBuilder(lopBuilder);

        hibernatePlayer();
    }

    @Override
    public IGeServerChannelHandler getServerChannelHandler() {
        return serverChannelHandler;
    }

    private void hibernatePlayer() {
        new GeHibernateUnitOfWork() {
            @Override
            protected void doWork(Session session) {
                // Stop the ship
                playerInfoHolder.getShipState().setShipStateMoveSpeed(0);
                playerInfoHolder.getShipState().setShipStateRotationSpeed(0);

                // Indicate player is offline
                int idIgnored = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        GeLocationObjectBehaviorTypesMapperType.IGNORED);
                playerInfoHolder.getLocationObject().setLocationObjectBehaviorTypeId(idIgnored);

                session.merge(playerInfoHolder.getShipState());
                session.merge(playerInfoHolder.getLocationObject());
            }
        }.execute();
    }

    private class EnergyRegenHandler extends GeTaskRunnablePair<Runnable> implements Runnable {

        private EnergyRegenHandler() {
            super(GeConstants.DELAY_ENERGY_REGEN);
            setRunnable(this);
        }

        @Override
        public void run() {
            int energy = ssrBuilder.getEnergy();
            int energyRegen = shipConfig.getShipConfigEnergyRegen();
            int energyMax = shipConfig.getShipConfigEnergyMax();

            if (energy < energyMax) {
                energy = (energy + energyRegen > energyMax ? energyMax : energy + energyRegen);
                ssrBuilder.setEnergy(energy);
            }
        }
    }

    private class HpRegenHandler extends GeTaskRunnablePair<Runnable> implements Runnable {

        private HpRegenHandler() {
            super(GeConstants.DELAY_HP_REGEN);
            setRunnable(this);
        }

        @Override
        public void run() {
            int hp = ssrBuilder.getHp();
            int hpRegen = shipConfig.getShipConfigHpRegen();
            int hpMax = shipConfig.getShipConfigHpMax();

            if (hp < hpMax) {
                hp = (hp + hpRegen > hpMax ? hpMax : hp + hpRegen);
                ssrBuilder.setHp(hp);
            }
        }
    }
}
