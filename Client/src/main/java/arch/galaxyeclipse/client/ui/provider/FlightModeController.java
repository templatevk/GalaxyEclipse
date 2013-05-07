package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.data.GePosition;
import arch.galaxyeclipse.client.data.LocationInfoHolder;
import arch.galaxyeclipse.client.data.ShipStateInfoHolder;
import arch.galaxyeclipse.client.data.ShipStaticInfoHolder;
import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.network.PacketProcessingListenerCommand;
import arch.galaxyeclipse.client.network.sender.DynamicObjectsRequestSender;
import arch.galaxyeclipse.client.network.sender.ShipStateRequestSender;
import arch.galaxyeclipse.client.ui.actor.IActorFactory;
import arch.galaxyeclipse.client.ui.actor.IGeActor;
import arch.galaxyeclipse.client.ui.model.FlightModeModel;
import arch.galaxyeclipse.client.ui.view.AbstractGameStage;
import arch.galaxyeclipse.client.ui.view.FlightModeStage;
import arch.galaxyeclipse.client.window.IClientWindow;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.util.ICommand;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 *
 */
@Slf4j
public class FlightModeController implements IStageProvider {
    private IClientNetworkManager networkManager;
    private IClientWindow clientWindow;
    private ShipStaticInfoHolder shipStaticInfoHolder;
    private LocationInfoHolder locationInfoHolder;
    private ShipStateInfoHolder shipStateInfoHolder;
    private DynamicObjectsRequestSender dynamicObjectsRequestSender;
    private ShipStateRequestSender shipStateRequestSender;
    private IActorFactory actorFactory;
    private FlightModeStage view;

    public FlightModeController() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);
        clientWindow = ContextHolder.getBean(IClientWindow.class);
        actorFactory = ContextHolder.getBean(IActorFactory.class);
        locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);
        shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);
        shipStaticInfoHolder = ContextHolder.getBean(ShipStaticInfoHolder.class);

        initializeRequestSenders();

        view = new FlightModeStage(this);
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
                List<LocationObjectPacket> locationObjects =
                        locationInfoHolder.getObjectsForRadius(position);

                FlightModeModel model = new FlightModeModel(locationObjects.size());
                for (LocationObjectPacket locationObject : locationObjects) {
                    model.getGameActors().add(actorFactory.createLocationObjectActor(locationObject));
                }

                int locationId = locationInfoHolder.getLocationId();
                IGeActor background = actorFactory.createBackgroundActor(locationId);
                model.setBackground(background);

                Collections.sort(model.getGameActors());
                view.updateModel(model);

                if (FlightModeController.log.isDebugEnabled()) {
                    FlightModeController.log.debug("Added " + model.getGameActors().size()
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
        dynamicObjectsRequestSender.interrupt();
        shipStateRequestSender.interrupt();
    }
}
