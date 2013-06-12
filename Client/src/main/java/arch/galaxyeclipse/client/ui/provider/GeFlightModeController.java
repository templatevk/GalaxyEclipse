package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.data.GeLocationInfoHolder;
import arch.galaxyeclipse.client.data.GeShipStateInfoHolder;
import arch.galaxyeclipse.client.network.GePacketProcessingListenerCommand;
import arch.galaxyeclipse.client.network.sender.GeDynamicObjectsRequestSender;
import arch.galaxyeclipse.client.network.sender.GeShipStateRequestSender;
import arch.galaxyeclipse.client.ui.actor.GeActor;
import arch.galaxyeclipse.client.ui.actor.IGeActorFactory;
import arch.galaxyeclipse.client.ui.view.GeAbstractGameStage;
import arch.galaxyeclipse.client.ui.view.GeFlightModeStage;
import arch.galaxyeclipse.shared.common.GePosition;
import arch.galaxyeclipse.shared.common.IGeCommand;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 */
@Slf4j
public class GeFlightModeController implements IGeStageProvider {

    private GeLocationInfoHolder locationInfoHolder;
    private GeShipStateInfoHolder shipStateInfoHolder;
    private GeDynamicObjectsRequestSender dynamicObjectsRequestSender;
    private GeShipStateRequestSender shipStateRequestSender;
    private IGeActorFactory actorFactory;
    private GeFlightModeStage view;

    public GeFlightModeController() {
        view = new GeFlightModeStage();
        actorFactory = GeContextHolder.getBean(IGeActorFactory.class);
        locationInfoHolder = GeContextHolder.getBean(GeLocationInfoHolder.class);
        shipStateInfoHolder = GeContextHolder.getBean(GeShipStateInfoHolder.class);

        initializeRequestSenders();
    }

    private void initializeRequestSenders() {
        shipStateRequestSender = new GeShipStateRequestSender();
        shipStateRequestSender.start();

        dynamicObjectsRequestSender = new GeDynamicObjectsRequestSender();
        dynamicObjectsRequestSender.addPacketListener(new GePacketProcessingListenerCommand(
                GePacket.Type.DYNAMIC_OBJECTS_RESPONSE, new IGeCommand<GePacket>() {
            @Override
            public void perform(GePacket packet) {
                GePosition position = new GePosition(shipStateInfoHolder.getPositionX(),
                        shipStateInfoHolder.getPositionY());
                List<GeLocationObjectPacket> lopList =
                        locationInfoHolder.getObjectsForRadius(position);
                view.getModel().refreshActors(lopList);

                int locationId = locationInfoHolder.getLocationId();
                GeActor background = actorFactory.createBackgroundActor(locationId);
                view.getModel().setBackground(background);

                if (GeFlightModeController.log.isDebugEnabled()) {
                    GeFlightModeController.log.debug("Added " + lopList.size()
                            + " actors to the flight stage");
                }
            }
        }));
        dynamicObjectsRequestSender.start();
    }

    @Override
    public GeStageProviderType getType() {
        return GeStageProviderType.FLIGHT;
    }

    @Override
    public GeAbstractGameStage getGameStage() {
        return view;
    }

    @Override
    public void detach() {
        dynamicObjectsRequestSender.interrupt();
        shipStateRequestSender.interrupt();
    }
}
