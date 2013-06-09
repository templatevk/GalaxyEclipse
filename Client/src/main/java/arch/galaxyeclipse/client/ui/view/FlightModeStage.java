package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.ui.*;
import arch.galaxyeclipse.client.ui.actor.GeActor;
import arch.galaxyeclipse.client.ui.actor.IGeActor;
import arch.galaxyeclipse.client.ui.actor.StageInfo;
import arch.galaxyeclipse.client.ui.model.FlightModeModel;
import arch.galaxyeclipse.client.ui.provider.IStageProvider;
import arch.galaxyeclipse.client.ui.provider.StageProviderFactory;
import arch.galaxyeclipse.client.ui.widget.*;
import arch.galaxyeclipse.shared.common.StubCallback;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlightModeStage extends AbstractGameStage {

    private static final float HUD_PADDING_OFFSET = 55;
    private static final float HUD_PADDING = 15;

    private static final float CHAT_BUTTON_PADDING_BOTTOM = HUD_PADDING;
    private static final float CHAT_BUTTON_PADDING_LEFT = HUD_PADDING;
    private static final float CHAT_PADDING_BOTTOM = HUD_PADDING + HUD_PADDING_OFFSET;
    private static final float CHAT_PADDING_LEFT = HUD_PADDING;

    private static final float MINIMAP_BUTTON_PADDING_BOTTOM = HUD_PADDING;
    private static final float MINIMAP_BUTTON_PADDING_RIGHT = HUD_PADDING;
    private static final float MINIMAP_PADDING_BOTTOM = HUD_PADDING + HUD_PADDING_OFFSET;
    private static final float MINIMAP_PADDING_RIGHT = HUD_PADDING;

    private static final float STATE_BUTTON_PADDING_TOP = HUD_PADDING;
    private static final float STATE_BUTTON_PADDING_LEFT = HUD_PADDING;
    private static final float STATE_PADDING_TOP = HUD_PADDING + HUD_PADDING_OFFSET;
    private static final float STATE_PADDING_LEFT = HUD_PADDING;

    private static final float MAINMENU_BUTTON_PADDING_TOP = HUD_PADDING;
    private static final float MAINMENU_BUTTON_PADDING_RIGHT = HUD_PADDING;

    private static final float BOTTOMPANEL_PADDING_BOTTOM = 0;
    private static final float TOPPANEL_PADDING_TOP = 0;

    private @Getter FlightModeModel model;

    private Group rootLayout;
    private Group gameActorsLayout;

    private BottomPanelWidget bottomPanelWidget;
    private TopPanelWidget topPanelWidget;
    private ChatWidget chatWidget;
    private MiniMapWidget miniMapWidget;
    private StateWidget stateWidget;
    private Table chatBtnTable;
    private Button chatBtn;
    private Table miniMapBtnTable;
    private Button miniMapBtn;
    private Table stateBtnTable;
    private Button stateBtn;
    private Table mainMenuBtnTable;
    private TextButton mainMenuBtn;

    public FlightModeStage() {
        model = new FlightModeModel();
        model.setBackground(GeActor.newStub());

        gameActorsLayout = new Group();
        gameActorsLayout.setTransform(false);

        rootLayout = new Group();
        rootLayout.setTransform(false);
        rootLayout.addActor(gameActorsLayout);
        addActor(rootLayout);

        chatWidget = new ChatWidget();
        rootLayout.addActor(chatWidget);


        bottomPanelWidget = new BottomPanelWidget();
        rootLayout.addActor(bottomPanelWidget);

        topPanelWidget = new TopPanelWidget();
        rootLayout.addActor(topPanelWidget);

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

        stateWidget = new StateWidget();
        rootLayout.addActor(stateWidget);

        stateBtn = StageUiFactory.createButtonBuilder().setText("STATE")
                .setType(IButtonBuilder.ButtonType.GAME_STATE_HIDE_BUTTON)
                .setClickCommand(new IButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        stateWidget.setVisible(!stateWidget.isVisible());
                    }
                }).build();
        stateBtnTable = new Table();
        stateBtnTable.setSize(stateBtn.getWidth() ,stateBtn.getHeight());
        stateBtnTable.setTransform(true);
        stateBtnTable.row();
        stateBtnTable.add(stateBtn);
        rootLayout.addActor(stateBtnTable);

        mainMenuBtn = StageUiFactory.createButtonBuilder()
                .setText("MENU")
                .setType(IButtonBuilder.ButtonType.GAME_MAINMENU_HIDE_BUTTON)
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

        mainMenuBtnTable = new Table();
        mainMenuBtnTable.setSize(mainMenuBtn.getWidth() ,mainMenuBtn.getHeight());
        mainMenuBtnTable.setTransform(true);
        mainMenuBtnTable.row();
        mainMenuBtnTable.add(mainMenuBtn);
        rootLayout.addActor(mainMenuBtnTable);

        forceResize();
        addListener(new ClientActionListener());
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

        float bottomPanelWidgetWidth = bottomPanelWidget.getPrefWidth() * getScaleX();
        float bottomPanelWidgetHeight = bottomPanelWidget.getPrefHeight() * getScaleY();
        float bottomPanelWidgetX = (viewportWidth / 2f) - (bottomPanelWidgetWidth / 2f);
        float bottomPanelWidgetY = BOTTOMPANEL_PADDING_BOTTOM * getScaleY();
        bottomPanelWidget.setSize(bottomPanelWidgetWidth , bottomPanelWidgetHeight);
        bottomPanelWidget.setX(bottomPanelWidgetX);
        bottomPanelWidget.setY(bottomPanelWidgetY);

        float topPanelWidgetWidth = topPanelWidget.getPrefWidth() * getScaleX();
        float topPanelWidgetHeight = topPanelWidget.getPrefHeight() * getScaleY();
        float topPanelWidgetX = (viewportWidth / 2f) - (topPanelWidgetWidth / 2f);
        float topPanelWidgetY = viewportHeight - topPanelWidgetHeight - TOPPANEL_PADDING_TOP * getScaleY();
        topPanelWidget.setSize(topPanelWidgetWidth , topPanelWidgetHeight);
        topPanelWidget.setX(topPanelWidgetX);
        topPanelWidget.setY(topPanelWidgetY);

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

        float stateWidgetWidth = stateWidget.getPrefWidth() * getScaleX();
        float stateWidgetHeight = stateWidget.getPrefHeight() * getScaleY();
        float stateWidgetX = STATE_PADDING_LEFT * getScaleX();
        float stateWidgetY =  viewportHeight - stateWidgetHeight - (STATE_PADDING_TOP * getScaleY());
        stateWidget.setSize(stateWidgetWidth, stateWidgetHeight);
        stateWidget.setX(stateWidgetX);
        stateWidget.setY(stateWidgetY);

        float stateBtnTableX = STATE_BUTTON_PADDING_LEFT * getScaleX();
        float stateBtnTableY = viewportHeight - (stateBtnTable.getHeight() * getScaleY()) - (STATE_BUTTON_PADDING_TOP * getScaleY());
        stateBtnTable.setScale(getScaleX(), getScaleY());
        stateBtnTable.setX(stateBtnTableX);
        stateBtnTable.setY(stateBtnTableY);

        float mainMenuBtnTableX = viewportWidth
                - (mainMenuBtnTable.getWidth() * getScaleX())
                - (MAINMENU_BUTTON_PADDING_RIGHT * getScaleX());
        float mainMenuBtnTableY = viewportHeight
                - (mainMenuBtnTable.getHeight() * getScaleY())
                - (MAINMENU_BUTTON_PADDING_TOP * getScaleY());
        mainMenuBtnTable.setScale(getScaleX(), getScaleY());
        mainMenuBtnTable.setX(mainMenuBtnTableX);
        mainMenuBtnTable.setY(mainMenuBtnTableY);
    }

    @Override
    public void draw() {
        StageInfo stageInfo = new StageInfo();
        stageInfo.setHeight(gameActorsLayout.getHeight());
        stageInfo.setWidth(gameActorsLayout.getWidth());
        stageInfo.setScaleY(getScaleY());
        stageInfo.setScaleX(getScaleX());

        gameActorsLayout.clear();

        IGeActor background = model.getBackground();
        gameActorsLayout.addActor(background.toActor());
        background.adjust(stageInfo);

        for (IGeActor gameActor : model.getSortedActors()) {
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