package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.data.LocationInfoHolder;
import arch.galaxyeclipse.client.data.ShipStateInfoHolder;
import arch.galaxyeclipse.client.data.ShipStaticInfoHolder;
import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.ui.ChatWidget;
import arch.galaxyeclipse.client.ui.IButtonBuilder;
import arch.galaxyeclipse.client.ui.IButtonClickCommand;
import arch.galaxyeclipse.client.ui.StageUiFactory;
import arch.galaxyeclipse.client.ui.*;
import arch.galaxyeclipse.client.ui.actor.GeActor;
import arch.galaxyeclipse.client.ui.actor.IGeActor;
import arch.galaxyeclipse.client.ui.actor.StageInfo;
import arch.galaxyeclipse.client.ui.model.FlightModeModel;
import arch.galaxyeclipse.client.ui.provider.FlightModeController;
import arch.galaxyeclipse.client.ui.provider.IStageProvider;
import arch.galaxyeclipse.client.ui.provider.StageProviderFactory;
import arch.galaxyeclipse.shared.EnvType;
import arch.galaxyeclipse.shared.common.StubCallback;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
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
    
    // DEV MODE
    private TextButton mainMenuBtn;

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

        chatWidget = new ChatWidget();
        rootLayout.addActor(chatWidget);

        chatBtn = StageUiFactory.createButtonBuilder().setText("CHAT")
                .setType(IButtonBuilder.ButtonType.GAME_CHAT_HIDE_BUTTON)
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
        miniMapWidget.setX(CHAT_PADDING_LEFT);
        miniMapWidget.setY(CHAT_PADDING_BOTTOM);
        rootLayout.addActor(miniMapWidget);

        miniMapBtn = StageUiFactory.createButtonBuilder().setText("MINIMAP")
                .setType(IButtonBuilder.ButtonType.GAME_MINIMAP_HIDE_BUTTON)
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

        if (EnvType.CURRENT == EnvType.DEV) {
            mainMenuBtn = StageUiFactory.createButtonBuilder()
                    .setText("MM")
                    .setType(IButtonBuilder.ButtonType.MAIN_MENU_BUTTON)
                    .setClickCommand(new IButtonClickCommand() {
                        @Override
                        public void execute(InputEvent e, float x, float y) {
                            ContextHolder.getBean(IClientNetworkManager.class).disconnect(
                                    new StubCallback<Boolean>());
                            IStageProvider provider = StageProviderFactory.createStageProvider(
                                    StageProviderFactory.StagePresenterType.MAIN_MENU);
                            getClientWindow().setStageProvider(provider);
                            provider.getGameStage().forceResize();
                        }
                    }).build();

            rootLayout.addActor(mainMenuBtn);
        }

        forceResize();
        addListener(new ClientActionListener());
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
        float rootLayoutX = (getClientWindow().getWidth() - viewportWidth) / 2f;
        float rootLayoutY = (getClientWindow().getHeight() - viewportHeight) / 2f;
        rootLayout.setSize(viewportWidth, viewportHeight);
        rootLayout.setOrigin(viewportWidth / 2f, viewportHeight / 2f);
        rootLayout.setPosition(rootLayoutX, rootLayoutY);

        float gameActorsLayoutHeight = viewportHeight;
        float gameActorsLayoutWidth = viewportWidth;
        float gameActorsLayoutX = (viewportWidth - gameActorsLayoutWidth) / 2f;
        float gameActorsLayoutY = (viewportHeight - gameActorsLayoutHeight) / 2f;
        gameActorsLayout.setSize(gameActorsLayoutWidth, gameActorsLayoutHeight);
        gameActorsLayout.setOrigin(gameActorsLayoutWidth / 2f, gameActorsLayoutHeight / 2f);
        gameActorsLayout.setPosition(gameActorsLayoutX, gameActorsLayoutY);

        float chatWidgetWidth = chatWidget.getPrefWidth() * getScaleX();
        float chatWidgetHeight = chatWidget.getPrefHeight() * getScaleY();
        float chatWidgetX = CHAT_PADDING_LEFT * getScaleX();
        float chatWidgetY = CHAT_PADDING_BOTTOM * getScaleX();
        chatWidget.setSize(chatWidgetWidth, chatWidgetHeight);
        chatWidget.setX(chatWidgetX);
        chatWidget.setY(chatWidgetY);

        float chatBtnTableX = CHAT_BUTTON_PADDING_LEFT * getScaleX();
        float chatBtnTableY = CHAT_BUTTON_PADDING_BOTTOM * getScaleY();
        chatBtnTable.setScale(getScaleX(), getScaleY());
        chatBtnTable.setX(chatBtnTableX);
        chatBtnTable.setY(chatBtnTableY);

        float miniMapWidgetWidth = miniMapWidget.getPrefWidth() * getScaleX();
        float miniMapWidgetHeight = miniMapWidget.getPrefHeight() * getScaleY();
        float miniMapWidgetX = viewportWidth - miniMapWidgetWidth - (MINIMAP_PADDING_RIGHT * getScaleX());
        float miniMapWidgetY = MINIMAP_PADDING_BOTTOM * getScaleX();
        miniMapWidget.setSize(miniMapWidgetWidth , miniMapWidgetHeight);
        miniMapWidget.setX(miniMapWidgetX);
        miniMapWidget.setY(miniMapWidgetY);

        float miniMapBtnTableX = viewportWidth - (miniMapBtnTable.getWidth() * getScaleX()) - (MINIMAP_BUTTON_PADDING_RIGHT * getScaleX());
        float miniMapBtnTableY = MINIMAP_BUTTON_PADDING_BOTTOM * getScaleY();
        miniMapBtnTable.setScale(getScaleX(), getScaleY());
        miniMapBtnTable.setX(miniMapBtnTableX);
        miniMapBtnTable.setY(miniMapBtnTableY);

        if (EnvType.CURRENT == EnvType.DEV) {
            final float MAIN_MENU_BTN_COEF = 0.1f;
            mainMenuBtn.setWidth(viewportWidth * MAIN_MENU_BTN_COEF);
            mainMenuBtn.setHeight(viewportWidth * MAIN_MENU_BTN_COEF);

            float mainMenuBtnX = rootLayout.getWidth() - mainMenuBtn.getWidth();
            float mainMenuBtnY = rootLayout.getHeight() - mainMenuBtn.getHeight();
            mainMenuBtn.setX(mainMenuBtnX);
            mainMenuBtn.setY(mainMenuBtnY);
        }
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