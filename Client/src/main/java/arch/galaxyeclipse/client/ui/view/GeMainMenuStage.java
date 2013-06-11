package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.resource.IGeResourceLoader;
import arch.galaxyeclipse.client.ui.GeStageUiFactory;
import arch.galaxyeclipse.client.ui.IGeButtonBuilder;
import arch.galaxyeclipse.client.ui.provider.GeMainMenuPresenter;
import arch.galaxyeclipse.shared.GeEnvType;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.Arrays;

import static arch.galaxyeclipse.client.ui.GeStageUiFactory.createButtonBuilder;
import static arch.galaxyeclipse.client.ui.GeStageUiFactory.createTextFieldBuilder;

public class GeMainMenuStage extends GeAbstractGameStage {

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

    public GeMainMenuStage(final GeMainMenuPresenter presenter) {
        IGeResourceLoader resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);

        usernameTxt = createTextFieldBuilder()
                .setWidth(TEXTFIELD_WIDTH).setHeight(TEXTFIELD_HEIGHT)
                .setMessageText("Your username").build();
        passwordTxt = createTextFieldBuilder()
                .setWidth(TEXTFIELD_WIDTH).setHeight(TEXTFIELD_HEIGHT)
                .setMessageText("Your password").setPasswordMode(true)
                .setPasswordCharacter('*').build();
        connectBtn = createButtonBuilder().setText("Connect")
                .setType(IGeButtonBuilder.ButtonType.MAIN_MENU_BUTTON)
                .setClickCommand(presenter.getConnectButtonCommand()).build();

        rootTable = new Table();
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

        if (GeEnvType.CURRENT == GeEnvType.DEV) {
            usernameTxt.setText(TEST_PLAYER_LOGIN_PASSWORD);
            passwordTxt.setText(TEST_PLAYER_LOGIN_PASSWORD);
        }

        GeStageUiFactory.applyTabOrder(Arrays.<Actor>asList(
                usernameTxt, passwordTxt, connectBtn), this);
        GeStageUiFactory.setDefaultButton(Arrays.<Actor>asList(
                usernameTxt, passwordTxt), connectBtn);
        setKeyboardFocus(usernameTxt);

        forceResize();
    }

    @Override
    public void resize(float viewportWidth, float viewportHeight) {
        float rootLayoutX = (getClientWindow().getWidth() - viewportWidth) / 2f;
        float rootLayoutY = (getClientWindow().getHeight() - viewportHeight) / 2f;
        rootTable.setSize(viewportWidth, viewportHeight);
        rootTable.setPosition(rootLayoutX, rootLayoutY);
        innerTable.setPosition((viewportWidth / 2f) - (innerTable.getWidth() / 2f), (viewportHeight / 2f) - (innerTable.getHeight() / 2f));
        super.resize(viewportWidth, viewportHeight);
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