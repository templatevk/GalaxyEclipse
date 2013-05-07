package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.client.ui.Chat;
import arch.galaxyeclipse.client.ui.IButtonBuilder;
import arch.galaxyeclipse.client.ui.IButtonClickCommand;
import arch.galaxyeclipse.client.ui.StageUiFactory;
import arch.galaxyeclipse.client.ui.provider.*;
import arch.galaxyeclipse.client.ui.actor.*;
import arch.galaxyeclipse.client.ui.model.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 *
 */
public class FlightModeStage extends AbstractGameStage {
    private ShipStaticInfoHolder shipStaticInfoHolder;
    private LocationInfoHolder locationInfoHolder;
    private ShipStateInfoHolder shipStateInfoHolder;

    private Group rootLayout;
    private Group gameActorsLayout;

    private Chat chat;
    private Table chatBtnTable;
    private Button chatBtn;

    private final float CHAT_BUTTON_PADDING_BOTTOM = 10;
    private final float CHAT_BUTTON_PADDING_LEFT = 10;
    private final float CHAT_PADDING_BOTTOM = 65;
    private final float CHAT_PADDING_LEFT = 10;

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

        chat = new Chat();
        chat.setX(CHAT_PADDING_LEFT);
        chat.setY(CHAT_PADDING_BOTTOM);
        rootLayout.addActor(chat);

        chatBtn = StageUiFactory.createButtonBuilder().setText("CHAT")
                .setType(IButtonBuilder.ButtonType.GameChatHideButton)
                .setClickCommand(new IButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        chat.setVisible(!chat.isVisible());
                    }
                }).build();

        chatBtnTable = new Table();
        chatBtnTable.setBounds(CHAT_BUTTON_PADDING_LEFT, CHAT_BUTTON_PADDING_BOTTOM,
                chatBtn.getWidth(), chatBtn.getHeight());
        chatBtnTable.setTransform(true);
        rootLayout.addActor(chatBtnTable);

        chatBtnTable.row();
        chatBtnTable.add(chatBtn);

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
        chat.setSize(chat.getPrefWidth() * getScaleX(), chat.getPrefHeight() * getScaleY());
        chat.setX(CHAT_PADDING_LEFT * getScaleX());
        chat.setY(CHAT_PADDING_BOTTOM * getScaleX());

        // TODO on chat focus remove input processing from stage
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