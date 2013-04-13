package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.resources.*;
import arch.galaxyeclipse.client.stage.ui.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;
import lombok.extern.slf4j.*;

import java.net.*;
import java.util.*;
import java.util.List;

/**
 *
 */
@Slf4j
public class MainMenuPresenter implements IStagePresenter, IServerPacketListener {
    private IClientNetworkManager networkManager;
    private MainMenuStage view;
    private MainMenuModel model;
    private IButtonClickCommand connectButtonCommand;

    public MainMenuPresenter() {
        view = new MainMenuStage(this);
        model = new MainMenuModel();
        networkManager = ContextHolder.INSTANCE.getBean(IClientNetworkManager.class);

        connectButtonCommand = new IButtonClickCommand() {
            @Override
            public void execute(InputEvent e, float x, float y) {
                ICallback<Boolean> connectionCallback = new ICallback<Boolean>() {
                    @Override
                    public void onOperationComplete(Boolean isConnected) {
                        if (log.isInfoEnabled()) {
                            log.info(LogUtils.getObjectInfo(this) + " connection callback "
                                    + " result = " + isConnected);
                        }

                        if (isConnected) {
                            GalaxyEclipseProtocol.AuthRequest request = GalaxyEclipseProtocol.AuthRequest.newBuilder()
                                    .setUsername(view.getUsernameTxt().getText())
                                    .setPassword(view.getPasswordTxt().getText()).build();
                            GalaxyEclipseProtocol.Packet packet = GalaxyEclipseProtocol.Packet.newBuilder()
                                    .setType(GalaxyEclipseProtocol.Packet.Type.AUTH_REQUEST)
                                    .setAuthRequest(request).build();
                            networkManager.sendPacket(packet);
                        }
                    }
                };
                networkManager.connect(new InetSocketAddress(
                        SharedInfo.HOST, SharedInfo.PORT), connectionCallback);
            }
        };

        networkManager.addListener(this);
    }

    @Override
    public AbstractGameStage getGameStage() {
        return view;
    }

    @Override
    public void detach() {
        networkManager.removeListener(this);
    }

    @Override
    public void onPacketReceived(GalaxyEclipseProtocol.Packet packet) {
        if (log.isInfoEnabled()) {
            log.info(LogUtils.getObjectInfo(this) + " received packet " + packet.getType());
        }

        switch (packet.getType()) {
            case AUTH_RESPONSE:
                boolean success = packet.getAuthResponse().getIsSuccess();
                log.info("Authentication result = " + success);

                if (success) {
                /*
                TODO:
                   Change to loading stage, fill dictionary types
                   and obtain the necessary data, change to appropriate
                   stage and unsubscribe from network manager
               */
                }
                break;
        }
    }

    @Override
    public List<GalaxyEclipseProtocol.Packet.Type> getPacketTypes() {
        return Arrays.asList(GalaxyEclipseProtocol.Packet.Type.AUTH_RESPONSE);
    }

    @Data
    private static class MainMenuStage extends AbstractGameStage {
        private static final float TABLE_SPACING = 10;
        private static final float TEXTFIELD_WIDTH = 370;
        private static final float TEXTFIELD_HEIGHT = 100;

        private MainMenuPresenter mainMenuPresenter;
        private Button connectBtn;
        private TextField usernameTxt;
        private TextField passwordTxt;
        private Table rootTable;
        private Table innerTable;

        public MainMenuStage(final MainMenuPresenter presenter) {
            IStageUiFactory stageUiFactory = ContextHolder.INSTANCE.getBean(IStageUiFactory.class);
            IResourceLoader resourceLoader = ContextHolder.INSTANCE
                    .getBean(IResourceLoaderFactory.class).createResourceLoader();

            usernameTxt = stageUiFactory.createTextFieldBuilder()
                    .setWidth(TEXTFIELD_WIDTH).setHeight(TEXTFIELD_HEIGHT)
                    .setMessageText("Enter your username").build();
            passwordTxt = stageUiFactory.createTextFieldBuilder()
                    .setWidth(TEXTFIELD_WIDTH).setHeight(TEXTFIELD_HEIGHT)
                    .setMessageText("Enter your password").setPasswordMode(true)
                    .setPasswordCharacter('*').build();

            connectBtn = stageUiFactory.createButtonBuilder().setText("Connect")
                    .setClickCommand(presenter.connectButtonCommand).build();


            rootTable = new Table();
            rootTable.setFillParent(true);
            rootTable.setBackground(new TextureRegionDrawable(resourceLoader.findRegion("ui/menu_login")));
            addActor(rootTable);

            innerTable = new Table();
            innerTable.setBounds(0, 0, 400, 200);
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
        }

        @Override
        protected Group getScaleGroup() {
            return innerTable;
        }
    }

    @Data
    private static class MainMenuModel {

    }
}
