package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.client.data.GePosition;
import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.network.sender.*;
import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.client.stage.render.*;
import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
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
        actorFactory = ContextHolder.getBean(IActorFactory.class);
        locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);
        shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);
        shipStaticInfoHolder = ContextHolder.getBean(ShipStaticInfoHolder.class);

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
                GePosition position = new GePosition(shipStateInfoHolder.getPositionX(),
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
        // TODO
        private Table actorsBackground;
        private Table rootBackground;

        private FlightModeStage(FlightModeController controller) {
            this.controller = controller;
            this.model = new FlightModeModel();

            locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);
            shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);
            shipStaticInfoHolder = ContextHolder.getBean(ShipStaticInfoHolder.class);

            gameActorsLayout = new Group();
            gameActorsLayout.setTransform(true);

            rootLayout = new Group();
            rootLayout.setTransform(false);

            // TODO
            IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);

            actorsBackground = new Table();
            actorsBackground.setBackground(new TextureRegionDrawable(
                    resourceLoader.findRegion("ui/menu_login")));

            rootBackground = new Table();
            rootBackground.setBackground(new TextureRegionDrawable(
                    resourceLoader.findRegion("ui/menu_login")));

            rootLayout.addActor(rootBackground);






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

            // TODO
            actorsBackground.setSize(gameActorsLayoutWidth, gameActorsLayoutHeight);
            actorsBackground.setPosition(0, 0);
            rootBackground.setSize(viewportWidth, viewportHeight);
            rootBackground.setPosition(0, 0);
        }


        @Override
        public void draw() {
            gameActorsLayout.clear();

            // TODO
            if (actorsBackground != null) {
                gameActorsLayout.addActor(actorsBackground);
            }




            for (GameActor gameActor : model.getGameActors()) {
                gameActorsLayout.addActor(adjustActor(gameActor));
            }

            super.draw();
        }

        private GameActor adjustActor(GameActor gameActor) {
            switch (gameActor.getActorType()) {
                case ROCKET:
                    break;
                case ASTEROID:
                    break;
                case STATION:
                    break;
                case PLAYER:
                    boolean self = gameActor.getLocationObject().getObjectId() ==
                            shipStateInfoHolder.getLocationObjectId();
                    if (self) {
                        float x = (gameActorsLayout.getWidth() - gameActor.getWidth()) / 2f;
                        float y = (gameActorsLayout.getHeight() - gameActor.getHeight()) / 2f;
                        gameActor.setPosition(x, y);
                    } else {

                    }
                    break;
            }

            gameActor.setScale(getScaleX(), getScaleY());

            return gameActor;
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
