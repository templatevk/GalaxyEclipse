package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.client.ui.provider.*;
import arch.galaxyeclipse.client.ui.actor.*;
import arch.galaxyeclipse.client.ui.model.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.scenes.scene2d.*;

/**
 *
 */
public class FlightModeStage extends AbstractGameStage {
    private ShipStaticInfoHolder shipStaticInfoHolder;
    private LocationInfoHolder locationInfoHolder;
    private ShipStateInfoHolder shipStateInfoHolder;

    private Group rootLayout;
    private Group gameActorsLayout;

    private FlightModeController controller;
    private FlightModeModel model;

    public FlightModeStage(FlightModeController controller) {
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
        addListener(new ClientActionListener());

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

        StageInfo stageInfo = new StageInfo();
        stageInfo.setHeight(gameActorsLayout.getHeight());
        stageInfo.setWidth(gameActorsLayout.getWidth());
        stageInfo.setScaleY(getScaleY());
        stageInfo.setScaleX(getScaleX());

        IGeActor background = model.getBackground();
        gameActorsLayout.addActor(background.toActor());
        background.adjust(stageInfo);

        for (IGeActor gameActor : model.getGameActors()) {
            gameActorsLayout.addActor(gameActor.toActor());
            gameActor.adjust(stageInfo);
        }

        super.draw();
    }

    @Override
    protected boolean isManualScaling() {
        return true;
    }
}