package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.client.ui.provider.*;
import arch.galaxyeclipse.client.ui.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

import java.util.*;

public class MainMenuStage extends AbstractGameStage {
    private static final float TABLE_SPACING = 10;
    private static final float TEXTFIELD_WIDTH = 370;
    private static final float TEXTFIELD_HEIGHT = 100;
    public static final int INNER_TABLE_WIDTH = 400;
    public static final int INNER_TABLE_HEIGHT = 200;

    private MainMenuPresenter mainMenuPresenter;
    private Button connectBtn;
    private TextField usernameTxt;
    private TextField passwordTxt;
    private Table rootTable;
    private Table innerTable;

    public MainMenuStage(final MainMenuPresenter presenter) {
        IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);

        usernameTxt = StageUiFactory.createTextFieldBuilder()
                .setWidth(TEXTFIELD_WIDTH).setHeight(TEXTFIELD_HEIGHT)
                .setMessageText("Enter your username").build();
        passwordTxt = StageUiFactory.createTextFieldBuilder()
                .setWidth(TEXTFIELD_WIDTH).setHeight(TEXTFIELD_HEIGHT)
                .setMessageText("Enter your password").setPasswordMode(true)
                .setPasswordCharacter('*').build();
        connectBtn = StageUiFactory.createButtonBuilder().setText("Connect")
                .setClickCommand(presenter.getConnectButtonCommand()).build();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(new TextureRegionDrawable(resourceLoader.findRegion("ui/menu_login")));
        addActor(rootTable);

        innerTable = new Table();
        innerTable.setBounds(0, 0, INNER_TABLE_WIDTH, INNER_TABLE_HEIGHT);
        innerTable.setTransform(true);
        rootTable.setTransform(false);
        rootTable.add(innerTable);

        innerTable.add(usernameTxt).expand(true, false).space(TABLE_SPACING);
        innerTable.row();
        innerTable.add(passwordTxt).expand(true, false).space(TABLE_SPACING);
        innerTable.row();
        innerTable.add(connectBtn).expand(true, false).space(TABLE_SPACING);
        innerTable.setOrigin(innerTable.getPrefWidth() / 2,
                innerTable.getPrefHeight() / 2);

        if (EnvType.CURRENT == EnvType.DEV) {
            rootTable.debug();
            innerTable.debug();
        }

        StageUiFactory.applyTabOrder(Arrays.<Actor>asList(
                usernameTxt, passwordTxt, connectBtn), null);
        StageUiFactory.setDefaultButton(Arrays.<Actor>asList(
                usernameTxt, passwordTxt), connectBtn);
        setKeyboardFocus(usernameTxt);
    }

    @Override
    protected Group getScaleGroup() {
        return innerTable;
    }

    public String getUsername() {
        return usernameTxt.getText();
    }

    public String getPassword() {
        return passwordTxt.getText();
    }
}