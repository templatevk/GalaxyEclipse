package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.data.LocationInfoHolder;
import arch.galaxyeclipse.client.data.ShipStateInfoHolder;
import arch.galaxyeclipse.client.network.PacketProcessingListenerCommand;
import arch.galaxyeclipse.client.network.sender.DynamicObjectsRequestSender;
import arch.galaxyeclipse.client.network.sender.ShipStateRequestSender;
import arch.galaxyeclipse.client.ui.actor.IActorFactory;
import arch.galaxyeclipse.client.ui.actor.IGeActor;
import arch.galaxyeclipse.client.ui.view.AbstractGameStage;
import arch.galaxyeclipse.client.ui.view.FlightModeStage;
import arch.galaxyeclipse.shared.EnvType;
import arch.galaxyeclipse.shared.common.GePosition;
import arch.galaxyeclipse.shared.common.ICommand;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 */
@Slf4j
public class FlightModeController implements IStageProvider {
    private LocationInfoHolder locationInfoHolder;
    private ShipStateInfoHolder shipStateInfoHolder;
    private DynamicObjectsRequestSender dynamicObjectsRequestSender;
    private ShipStateRequestSender shipStateRequestSender;
    private IActorFactory actorFactory;
    private FlightModeStage view;

    public FlightModeController() {
        view = new FlightModeStage();
        actorFactory = ContextHolder.getBean(IActorFactory.class);
        locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);
        shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);

        if (EnvType.CURRENT != EnvType.DEV_UI) {
            initializeRequestSenders();
        }
    }

    private void initializeRequestSenders() {
        shipStateRequestSender = new ShipStateRequestSender();
        shipStateRequestSender.start();

        dynamicObjectsRequestSender = new DynamicObjectsRequestSender();
        dynamicObjectsRequestSender.addPacketListener(new PacketProcessingListenerCommand(
                GeProtocol.Packet.Type.DYNAMIC_OBJECTS_RESPONSE, new ICommand<GeProtocol.Packet>() {
            @Override
            public void perform(GeProtocol.Packet packet) {
                GePosition position = new GePosition(shipStateInfoHolder.getPositionX(),
                        shipStateInfoHolder.getPositionY());
                List<LocationObjectPacket> lopList =
                        locationInfoHolder.getObjectsForRadius(position);
                view.getModel().refresh(lopList);

                int locationId = locationInfoHolder.getLocationId();
                IGeActor background = actorFactory.createBackgroundActor(locationId);
                view.getModel().setBackground(background);


                if (FlightModeController.log.isDebugEnabled()) {
                    FlightModeController.log.debug("Added " + lopList.size()
                            + " actors to the flight stage");
                }
            }
        }));
        dynamicObjectsRequestSender.start();
    }

    @Override
    public AbstractGameStage getGameStage() {
        return view;
    }

    @Override
    public void detach() {
        if (EnvType.CURRENT != EnvType.DEV_UI) {
            dynamicObjectsRequestSender.interrupt();
            shipStateRequestSender.interrupt();
        }
    }
}
