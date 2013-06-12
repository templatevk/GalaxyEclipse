package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.client.ui.GeStageUiFactory;
import arch.galaxyeclipse.client.ui.IGeButtonBuilder;
import arch.galaxyeclipse.client.ui.IGeButtonClickCommand;
import arch.galaxyeclipse.client.ui.actor.GeActor;
import arch.galaxyeclipse.client.ui.actor.GeLocationObjectActor;
import arch.galaxyeclipse.client.ui.actor.GeStageInfo;
import arch.galaxyeclipse.client.ui.model.GeFlightModeModel;
import arch.galaxyeclipse.client.ui.provider.GeStageProviderFactory;
import arch.galaxyeclipse.client.ui.provider.GeStageProviderType;
import arch.galaxyeclipse.client.ui.provider.IGeStageProvider;
import arch.galaxyeclipse.client.ui.widget.*;
import arch.galaxyeclipse.shared.common.GeStubCallback;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.GeLocationObjectTypesMapperType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class GeFlightModeStage extends GeAbstractGameStage {

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

    private @Getter GeFlightModeModel model;

    private GeDictionaryTypesMapper dictionaryTypesMapper;

    private Group rootLayout;
    private Group gameActorsLayout;

    private GeBottomPanelWidget bottomPanelWidget;
    private GeTopPanelWidget topPanelWidget;
    private GeChatWidget chatWidget;
    private GeMiniMapWidget miniMapWidget;
    private GeStateWidget stateWidget;
    private Table chatBtnTable;
    private Button chatBtn;
    private Table miniMapBtnTable;
    private Button miniMapBtn;
    private Table stateBtnTable;
    private Button stateBtn;
    private Table mainMenuBtnTable;
    private TextButton mainMenuBtn;

    private Set<Integer> minimapObjectTypeIds;

    public GeFlightModeStage() {
        dictionaryTypesMapper = GeContextHolder.getBean(GeDictionaryTypesMapper.class);

        minimapObjectTypeIds = new HashSet<>();
        minimapObjectTypeIds.add(dictionaryTypesMapper.getIdByLocationObjectType(
                GeLocationObjectTypesMapperType.PLAYER));

        model = new GeFlightModeModel();
        model.setBackground(GeActor.newStub());

        gameActorsLayout = new Group();
        gameActorsLayout.setTransform(false);

        rootLayout = new Group();
        rootLayout.setTransform(false);
        rootLayout.addActor(gameActorsLayout);
        addActor(rootLayout);

        chatWidget = new GeChatWidget();
        rootLayout.addActor(chatWidget);


        bottomPanelWidget = new GeBottomPanelWidget();
        rootLayout.addActor(bottomPanelWidget);

        topPanelWidget = new GeTopPanelWidget();
        rootLayout.addActor(topPanelWidget);

        chatBtn = GeStageUiFactory.createButtonBuilder().setText("CHAT")
                .setType(IGeButtonBuilder.ButtonType.GAME_CHAT_HIDE_BUTTON)
                .setClickCommand(new IGeButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        chatWidget.setVisible(!chatWidget.isVisible());
                    }
                }).build();
        chatBtnTable = new Table();
        chatBtnTable.setSize(chatBtn.getWidth(), chatBtn.getHeight());
        chatBtnTable.setTransform(true);
        chatBtnTable.row();
        chatBtnTable.add(chatBtn);
        rootLayout.addActor(chatBtnTable);


        miniMapWidget = new GeMiniMapWidget();
        miniMapWidget.setX(CHAT_PADDING_LEFT);
        miniMapWidget.setY(CHAT_PADDING_BOTTOM);
        rootLayout.addActor(miniMapWidget);

        miniMapBtn = GeStageUiFactory.createButtonBuilder().setText("MINIMAP")
                .setType(IGeButtonBuilder.ButtonType.GAME_MINIMAP_HIDE_BUTTON)
                .setClickCommand(new IGeButtonClickCommand() {
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

        stateWidget = new GeStateWidget();
        rootLayout.addActor(stateWidget);

        stateBtn = GeStageUiFactory.createButtonBuilder().setText("STATE")
                .setType(IGeButtonBuilder.ButtonType.GAME_STATE_HIDE_BUTTON)
                .setClickCommand(new IGeButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        stateWidget.setVisible(!stateWidget.isVisible());
                    }
                }).build();
        stateBtnTable = new Table();
        stateBtnTable.setSize(stateBtn.getWidth(), stateBtn.getHeight());
        stateBtnTable.setTransform(true);
        stateBtnTable.row();
        stateBtnTable.add(stateBtn);
        rootLayout.addActor(stateBtnTable);

        mainMenuBtn = GeStageUiFactory.createButtonBuilder()
                .setText("MENU")
                .setType(IGeButtonBuilder.ButtonType.GAME_MAINMENU_HIDE_BUTTON)
                .setClickCommand(new IGeButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        GeContextHolder.getBean(IGeClientNetworkManager.class).disconnect(
                                new GeStubCallback<Boolean>());
                        IGeStageProvider provider = GeStageProviderFactory.createStageProvider(
                                GeStageProviderType.MAIN_MENU);
                        getClientWindow().setStageProvider(provider);
                        provider.getGameStage().forceResize();
                    }
                }).build();

        mainMenuBtnTable = new Table();
        mainMenuBtnTable.setSize(mainMenuBtn.getWidth(), mainMenuBtn.getHeight());
        mainMenuBtnTable.setTransform(true);
        mainMenuBtnTable.row();
        mainMenuBtnTable.add(mainMenuBtn);
        rootLayout.addActor(mainMenuBtnTable);

        forceResize();

        final GeClientActionListener clientActionListener = new GeClientActionListener();
        addListener(clientActionListener);
        addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (focused && actor == chatWidget.getTextField()) {
                    clientActionListener.setEnabled(false);
                } else {
                    clientActionListener.setEnabled(true);
                }
            }
        });
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
        bottomPanelWidget.setSize(bottomPanelWidgetWidth, bottomPanelWidgetHeight);
        bottomPanelWidget.setX(bottomPanelWidgetX);
        bottomPanelWidget.setY(bottomPanelWidgetY);

        float topPanelWidgetWidth = topPanelWidget.getPrefWidth() * getScaleX();
        float topPanelWidgetHeight = topPanelWidget.getPrefHeight() * getScaleY();
        float topPanelWidgetX = (viewportWidth / 2f) - (topPanelWidgetWidth / 2f);
        float topPanelWidgetY = viewportHeight - topPanelWidgetHeight - TOPPANEL_PADDING_TOP * getScaleY();
        topPanelWidget.setSize(topPanelWidgetWidth, topPanelWidgetHeight);
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
        miniMapWidget.setSize(miniMapWidgetWidth, miniMapWidgetHeight);
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
        float stateWidgetY = viewportHeight - stateWidgetHeight - (STATE_PADDING_TOP * getScaleY());
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
        GeStageInfo stageInfo = new GeStageInfo();
        stageInfo.setHeight(gameActorsLayout.getHeight());
        stageInfo.setWidth(gameActorsLayout.getWidth());
        stageInfo.setScaleY(getScaleY());
        stageInfo.setScaleX(getScaleX());

        gameActorsLayout.clear();

        GeActor background = model.getBackground();
        gameActorsLayout.addActor(background);
        background.adjust(stageInfo);

        for (GeActor actor : model.getSortedActors()) {
            gameActorsLayout.addActor(actor);
            actor.adjust(stageInfo);
        }

        miniMapWidget.setMinimapActors(Collections2.filter(model.getSortedActors(),
                new Predicate<GeLocationObjectActor>() {
                    @Override
                    public boolean apply(GeLocationObjectActor actor) {
                        return minimapObjectTypeIds.contains(actor.getLop().getObjectId());
                    }
                }));

        super.draw();
    }

    @Override
    protected boolean isManualScaling() {
        return true;
    }
}