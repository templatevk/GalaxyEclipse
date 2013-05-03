package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.network.sender.*;
import arch.galaxyeclipse.client.ui.actor.*;
import arch.galaxyeclipse.client.ui.model.*;
import arch.galaxyeclipse.client.ui.view.*;
import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;

import java.util.*;

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
                Set<GeProtocol.LocationInfo.LocationObject> locationObjects =
                        locationInfoHolder.getObjectsForRadius(position);

                FlightModeModel model = new FlightModeModel(locationObjects.size());
                for (GeProtocol.LocationInfo.LocationObject locationObject : locationObjects) {
                    model.getGameActors().add(actorFactory.createLocationObjectActor(locationObject));
                }

                int locationId = locationInfoHolder.getLocationId();
                IGeActor background = actorFactory.createBackgroundActor(locationId);
                model.setBackground(background);

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
