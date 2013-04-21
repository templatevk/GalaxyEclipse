package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.client.data.Position;
import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.network.sender.*;
import arch.galaxyeclipse.client.stage.render.*;
import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.*;
import lombok.*;
import lombok.extern.slf4j.*;

import java.util.*;
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
    private FlightModeModel model;

    public FlightModeController() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);
        clientWindow = ContextHolder.getBean(IClientWindow.class);
        locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);
        shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);
        shipStaticInfoHolder = ContextHolder.getBean(ShipStaticInfoHolder.class);
        actorFactory = ContextHolder.getBean(IActorFactory.class);

        initializeRequestSenders();

        model = new FlightModeModel();
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
                Position position = new Position(shipStateInfoHolder.getPositionX(),
                        shipStateInfoHolder.getPositionY());
                Set<GeProtocol.LocationInfo.LocationObject> locationObjects =
                        locationInfoHolder.getObjectsForRadius(position);

                FlightModeModel model = new FlightModeModel(locationObjects.size());
                for (GeProtocol.LocationInfo.LocationObject locationObject : locationObjects) {
                    model.getGameActors().add(actorFactory.createActor(locationObject));
                }
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
    public GameStage getGameStage() {
        return view;
    }

    @Override
    public void detach() {
        dynamicObjectsRequestSender.interrupt();
        shipStateRequestSender.interrupt();
    }

    private static class FlightModeStage extends GameStage {
        private IClientWindow clientWindow;

        private Group rootLayout;
        private Group gameActorsLayout;

        private FlightModeController controller;
        private FlightModeModel model;

        private FlightModeStage(FlightModeController controller) {
            this.controller = controller;
            clientWindow = ContextHolder.getBean(IClientWindow.class);
            model = new FlightModeModel();

            gameActorsLayout = new Group();
            gameActorsLayout.setTransform(true);

            rootLayout = new Group();
            rootLayout.addActor(gameActorsLayout);
            rootLayout.setTransform(true);
            addActor(rootLayout);

            forceResize();
        }

        public void updateModel(FlightModeModel model) {
            this.model = model;
        }

        @Override
        public void resize(int width, int height) {
            super.resize(width, height);
            resize();
        }

        private void resize() {
            float width = clientWindow.getViewportWidth();
            float height = clientWindow.getViewportHeight();

            rootLayout.setSize(width, height);
            rootLayout.setOrigin(width / 2f, height / 2f);

            gameActorsLayout.setSize(width / 2f, height / 2f);
            gameActorsLayout.setOrigin(width / 2f, height / 2f);
        }


        @Override
        public void draw() {
            gameActorsLayout.clear();
            for (GameActor gameActor : model.getGameActors()) {
                gameActorsLayout.addActor(gameActor);

                switch (gameActor.getActorType()) {
                    case ROCKET:
                        break;
                    case ASTEROID:
                        break;
                    case STATION:
                        break;
                    case PLAYER:
                        gameActor.setPosition(gameActorsLayout.getWidth() / 2f,
                                gameActorsLayout.getHeight() / 2f);
                        break;
                }
            }

            super.draw();
        }

        @Override
        protected Group getScaleGroup() {
            return rootLayout;
        }
    }

    @Data
    static class FlightModeModel {
        private List<GameActor> gameActors = new ArrayList<>();

        public FlightModeModel() {
            gameActors = new ArrayList<>();
        }

        public FlightModeModel(int actorsCapacity) {
            gameActors = new ArrayList<>(actorsCapacity);
        }
    }
}
