package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.data.LocationInfoHolder;
import arch.galaxyeclipse.client.data.ShipStateInfoHolder;
import arch.galaxyeclipse.client.data.ShipStaticInfoHolder;
import arch.galaxyeclipse.client.ui.*;
import arch.galaxyeclipse.client.ui.actor.GeActor;
import arch.galaxyeclipse.client.ui.actor.IGeActor;
import arch.galaxyeclipse.client.ui.actor.StageInfo;
import arch.galaxyeclipse.client.ui.model.FlightModeModel;
import arch.galaxyeclipse.client.ui.provider.FlightModeController;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 *
 */
public class FlightModeStage extends AbstractGameStage {
    private ShipStaticInfoHolder shipStaticInfoHolder;
    private LocationInfoHolder locationInfoHolder;
    private ShipStateInfoHolder shipStateInfoHolder;

    private Group rootLayout;
    private Group gameActorsLayout;

    private ChatWidget chatWidget;
    private MiniMapWidget miniMapWidget;
    private Table chatBtnTable;
    private Button chatBtn;
    private Table miniMapBtnTable;
    private Button miniMapBtn;

    private final float CHAT_BUTTON_PADDING_BOTTOM = 10;
    private final float CHAT_BUTTON_PADDING_LEFT = 10;
    private final float CHAT_PADDING_BOTTOM = 65;
    private final float CHAT_PADDING_LEFT = 10;

    private final float MINIMAP_BUTTON_PADDING_BOTTOM = 10;
    private final float MINIMAP_BUTTON_PADDING_RIGHT = 10;
    private final float MINIMAP_PADDING_BOTTOM = 65;
    private final float MINIMAP_PADDING_RIGHT = 10;

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

        chatWidget = new ChatWidget();
        chatWidget.setX(CHAT_PADDING_LEFT);
        chatWidget.setY(CHAT_PADDING_BOTTOM);
        rootLayout.addActor(chatWidget);

        // TODO on chatWidget focus remove input processing from stage

        chatBtn = StageUiFactory.createButtonBuilder().setText("CHAT")
                .setType(IButtonBuilder.ButtonType.GameChatHideButton)
                .setClickCommand(new IButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        chatWidget.setVisible(!chatWidget.isVisible());
                    }
                }).build();
        chatBtnTable = new Table();
        chatBtnTable.setSize(chatBtn.getWidth() ,chatBtn.getHeight());
        chatBtnTable.setTransform(true);
        chatBtnTable.row();
        chatBtnTable.add(chatBtn);
        rootLayout.addActor(chatBtnTable);

        miniMapWidget = new MiniMapWidget();
        rootLayout.addActor(miniMapWidget);

        miniMapBtn = StageUiFactory.createButtonBuilder().setText("MINIMAP")
                .setType(IButtonBuilder.ButtonType.GameMiniMapHideButton)
                .setClickCommand(new IButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        miniMapWidget.setVisible(!miniMapWidget.isVisible());
                    }
                }).build();
        miniMapBtnTable = new Table();
        miniMapBtnTable.setSize(miniMapBtn.getWidth(), miniMapBtn.getHeight());
        miniMapBtnTable.setTransform(true);
        miniMapBtnTable.row();
        miniMapBtnTable.add(miniMapBtn);
        rootLayout.addActor(miniMapBtnTable);

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

        chatBtnTable.setScale(getScaleX(), getScaleY());
        chatBtnTable.setX(CHAT_BUTTON_PADDING_LEFT * getScaleX());
        chatBtnTable.setY(CHAT_BUTTON_PADDING_BOTTOM * getScaleY());
        chatWidget.setSize(chatWidget.getPrefWidth() * getScaleX(), chatWidget.getPrefHeight() * getScaleY());
        chatWidget.setX(CHAT_PADDING_LEFT * getScaleX());
        chatWidget.setY(CHAT_PADDING_BOTTOM * getScaleX());
        miniMapWidget.setSize(miniMapWidget.getPrefWidth() * getScaleX(), miniMapWidget.getPrefHeight() * getScaleY());
        miniMapWidget.setX(viewportWidth - miniMapWidget.getWidth() - (MINIMAP_PADDING_RIGHT * getScaleX()));
        miniMapWidget.setY(MINIMAP_PADDING_BOTTOM * getScaleX());
        miniMapBtnTable.setScale(getScaleX(), getScaleY());
        miniMapBtnTable.setX(viewportWidth - (miniMapBtnTable.getWidth() * getScaleX()) - (MINIMAP_BUTTON_PADDING_RIGHT * getScaleX()));
        miniMapBtnTable.setY(MINIMAP_BUTTON_PADDING_BOTTOM * getScaleY());
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