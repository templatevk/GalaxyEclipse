package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.data.IResourceLoader;
import arch.galaxyeclipse.client.ui.IButtonBuilder;
import arch.galaxyeclipse.client.ui.StageUiFactory;
import arch.galaxyeclipse.client.ui.provider.MainMenuPresenter;
import arch.galaxyeclipse.shared.EnvType;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.Arrays;

public class MainMenuStage extends AbstractGameStage {
    private static final float TABLE_SPACING = 10;
    private static final float TEXTFIELD_WIDTH = 370;
    private static final float TEXTFIELD_HEIGHT = 100;
    public static final int INNER_TABLE_WIDTH = 400;
    public static final int INNER_TABLE_HEIGHT = 200;
    public static final String TEST_PLAYER_LOGIN_PASSWORD = "test1";

    private Button connectBtn;
    private TextField usernameTxt;
    private TextField passwordTxt;
    private Table rootTable;
    private Table innerTable;

    public MainMenuStage(final MainMenuPresenter presenter) {
        IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);

        usernameTxt = StageUiFactory.createTextFieldBuilder()
                .setWidth(TEXTFIELD_WIDTH).setHeight(TEXTFIELD_HEIGHT)
                .setMessageText("Your username").build();
        passwordTxt = StageUiFactory.createTextFieldBuilder()
                .setWidth(TEXTFIELD_WIDTH).setHeight(TEXTFIELD_HEIGHT)
                .setMessageText("Your password").setPasswordMode(true)
                .setPasswordCharacter('*').build();
        connectBtn = StageUiFactory.createButtonBuilder().setText("Connect")
                .setType(IButtonBuilder.ButtonType.MAIN_MENU_BUTTON)
                .setClickCommand(presenter.getConnectButtonCommand()).build();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(resourceLoader.createDrawable("ui/menu_login"));
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

        if (EnvType.CURRENT == EnvType.DEV || EnvType.CURRENT == EnvType.DEV_UI) {
            rootTable.debug();
            innerTable.debug();

            usernameTxt.setText(TEST_PLAYER_LOGIN_PASSWORD);
            passwordTxt.setText(TEST_PLAYER_LOGIN_PASSWORD);
        }

        StageUiFactory.applyTabOrder(Arrays.<Actor>asList(
                usernameTxt, passwordTxt, connectBtn), this);
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