package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.client.data.GePosition;
import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.network.sender.*;
import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.client.stage.render.*;
import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
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

//               TODO unstable
//                int locationId = locationInfoHolder.getLocationId();
//                GeActor background = actorFactory.createBackgroundActor(locationId);
//                FlightModeModel model = new FlightModeModel(locationObjects.size(), background);

                FlightModeModel model = new FlightModeModel(locationObjects.size());
                for (GeProtocol.LocationInfo.LocationObject locationObject : locationObjects) {
                    model.getGameActors().add(actorFactory.createLocationObjectActor(locationObject));
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
    public AbstractGameStage getGameStage() {
        return view;
    }

    @Override
    public void detach() {
        dynamicObjectsRequestSender.interrupt();
        shipStateRequestSender.interrupt();
    }

    private static class FlightModeStage extends AbstractGameStage {
        private ShipStaticInfoHolder shipStaticInfoHolder;
        private LocationInfoHolder locationInfoHolder;
        private ShipStateInfoHolder shipStateInfoHolder;

        private Group rootLayout;
        private Group gameActorsLayout;

        private FlightModeController controller;
        private FlightModeModel model;

        private FlightModeStage(FlightModeController controller) {
            this.controller = controller;
            this.model = new FlightModeModel();
            this.model.setBackground(new GeActor());

            locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);
            shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);
            shipStaticInfoHolder = ContextHolder.getBean(ShipStaticInfoHolder.class);

            gameActorsLayout = new Group();
            gameActorsLayout.setTransform(false);

            rootLayout = new Group();
            rootLayout.setTransform(false);
            rootLayout.addActor(gameActorsLayout);

            addActor(rootLayout);

            forceResize();
        }

        public void updateModel(FlightModeModel model) {
            this.model = model;
        }

        @Override
        public void resize(float viewportWidth, float viewportHeight) {
            super.resize(viewportWidth, viewportHeight);
            resizeLayout(viewportWidth, viewportHeight);
        }

        private void resizeLayout(float viewportWidth, float viewportHeight) {
            float rootLayoutX = 0;
            float rootLayoutY = 0;
            rootLayout.setSize(viewportWidth, viewportHeight);
            rootLayout.setOrigin(viewportWidth / 2f, viewportHeight / 2f);
            rootLayout.setPosition(rootLayoutX, rootLayoutY);

            float gameActorsLayoutHeight = viewportHeight / 2f;
            float gameActorsLayoutWidth = viewportWidth / 2f;
            float gameActorsLayoutX = (viewportWidth - gameActorsLayoutWidth) / 2f;
            float gameActorsLayoutY = (viewportHeight - gameActorsLayoutHeight) / 2f;
            gameActorsLayout.setSize(gameActorsLayoutWidth, gameActorsLayoutHeight);
            gameActorsLayout.setOrigin(gameActorsLayoutWidth / 2f, gameActorsLayoutHeight / 2f);
            gameActorsLayout.setPosition(gameActorsLayoutX, gameActorsLayoutY);
        }


        @Override
        public void draw() {
            gameActorsLayout.clear();
//            TODO unstable
//            drawBackground();
            drawActors();

            super.draw();
        }

        private void drawBackground() {
            GeActor background = model.getBackground();
            gameActorsLayout.addActor(background);
            background.setSize(gameActorsLayout.getWidth(), gameActorsLayout.getHeight());
            background.setPosition(0, 0);
        }

        private void drawActors() {
            StageInfo stageInfo = new StageInfo();
            stageInfo.setHeight(gameActorsLayout.getHeight());
            stageInfo.setWidth(gameActorsLayout.getWidth());
            stageInfo.setScaleY(getScaleY());
            stageInfo.setScaleX(getScaleX());

            for (GeActor gameActor : model.getGameActors()) {
                gameActorsLayout.addActor(gameActor);
                gameActor.adjust(stageInfo);
            }
        }

        @Override
        protected Group getScaleGroup() {
            return rootLayout;
        }
    }

    @Data
    private static class FlightModeModel {
        private static final int ACRORS_SIZE = 0;

        private List<GeActor> gameActors;
        private GeActor background;

        public FlightModeModel() {
            this(ACRORS_SIZE);
        }

        public FlightModeModel(int actorsCapacity) {
            this(actorsCapacity, null);
        }

        public FlightModeModel(int actorsCapacity, GeActor background) {
            gameActors = new ArrayList<>(actorsCapacity);
            this.background = background;
        }
    }
}
