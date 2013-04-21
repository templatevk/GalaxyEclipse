package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.network.sender.*;
import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.client.stage.render.*;
import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.extern.slf4j.*;

import java.util.*;
import java.util.List;

/**
 *
 */
@Slf4j
public class FlightModePresenter implements IStagePresenter {
    private IClientNetworkManager networkManager;
    private IClientWindow clientWindow;

    private FlightModeStage view;
    private ShipStaticInfoHolder shipStaticInfoHolder;
    private LocationInfoHolder locationInfoHolder;
    private ShipStateInfoHolder shipStateInfoHolder;
    private DynamicObjectsRequestSender dynamicObjectsRequestSender;
    private ShipStateRequestSender shipStateRequestSender;
    private IActorFactory actorFactory;

    public FlightModePresenter() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);
        clientWindow = ContextHolder.getBean(IClientWindow.class);
        locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);
        shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);
        shipStaticInfoHolder = ContextHolder.getBean(ShipStaticInfoHolder.class);
        actorFactory = ContextHolder.getBean(IActorFactory.class);

        initializeRequestSenders();
    }

    private void initializeRequestSenders() {
        shipStateRequestSender = new ShipStateRequestSender();
        shipStateRequestSender.start();

        dynamicObjectsRequestSender = new DynamicObjectsRequestSender();
        dynamicObjectsRequestSender.addPacketListener(new CommandPacketProcessingListener(
                GeProtocol.Packet.Type.DYNAMIC_OBJECTS_RESPONSE, new ICommand<GeProtocol.Packet>() {
            @Override
            public void perform(GeProtocol.Packet packet) {
                Position position = new Position(shipStateInfoHolder.getPositionX(),
                        shipStateInfoHolder.getPositionY());
                Set<GeProtocol.LocationInfo.LocationObject> locationObjects =
                        locationInfoHolder.getObjectsForRadius(position);

                List<GameActor> gameActors = new ArrayList<>(locationObjects.size());
                for (GeProtocol.LocationInfo.LocationObject locationObject : locationObjects) {
                    gameActors.add(actorFactory.createActor(locationObject));
                }
                view.setGameActors(gameActors);

                if (log.isDebugEnabled()) {
                    log.debug("Added " + gameActors.size() + " actors to the flight stage");
                }
            }
        }));
        dynamicObjectsRequestSender.start();

        view = new FlightModeStage(this);
    }

    @Override
    public GameStage getGameStage() {
        return view;
    }

    @Override
    public void detach() {
        dynamicObjectsRequestSender.interrupt();
    }

    private static class FlightModeStage extends GameStage {
        public static final int INNER_TABLE_WIDTH = 400;
        public static final int INNER_TABLE_HEIGHT = 200;

        private IClientWindow clientWindow;

        private Table rootTable;
        private Group gameActorsLayout;
        private List<GameActor> gameActors;
        private FlightModePresenter presenter;

        private FlightModeStage(FlightModePresenter presenter) {
            this.presenter = presenter;

            clientWindow = ContextHolder.getBean(IClientWindow.class);
            gameActors = new ArrayList<>();

            gameActorsLayout = new Group();
            gameActorsLayout.setTransform(true);

            rootTable = new Table();
            rootTable.setFillParent(true);
            rootTable.add(gameActorsLayout).width(clientWindow.getViewportWidth() / 2f)
                    .height(clientWindow.getViewportHeight() / 2f);
            rootTable.setTransform(false);
            addActor(rootTable);

            if (EnvType.CURRENT == EnvType.DEV) {
                rootTable.debug();
            }
        }

        public void setGameActors(List<GameActor> gameActors) {
            this.gameActors = gameActors;
        }

        @Override
        public void resize(int width, int height) {
            super.resize(width, height);

            gameActorsLayout.setBounds(0, 0, clientWindow.getViewportWidth(),
                    clientWindow.getViewportHeight());
        }

        @Override
        public void draw() {
            gameActorsLayout.clear();
            for (GameActor actor : gameActors) {
                switch (actor.getActorType()) {
                    case ROCKET:
                        break;
                    case ASTEROID:
                        break;
                    case STATION:
                        break;
                    case PLAYER:
                        actor.setPosition(gameActorsLayout.getWidth() / 2f,
                                gameActorsLayout.getHeight() / 2f);
                        break;
                }
                gameActorsLayout.addActor(actor);
            }

            super.draw();
        }

        @Override
        protected Group getScaleGroup() {
            return gameActorsLayout;
        }
    }
}
