package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.resources.*;
import arch.galaxyeclipse.client.stage.ui.*;
import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;
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
    private IClientWindow clientWindow;

    private MainMenuStage view;
    private MainMenuModel model;
    private IButtonClickCommand connectButtonCommand;

    public MainMenuPresenter() {
        view = new MainMenuStage(this);
        model = new MainMenuModel();
        clientWindow = ContextHolder.INSTANCE.getBean(IClientWindow.class);
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
                            AuthRequest request = AuthRequest.newBuilder()
                                    .setUsername(view.getUsernameTxt().getText())
                                    .setPassword(view.getPasswordTxt().getText()).build();

                            Packet authRequest = Packet.newBuilder()
                                    .setType(Packet.Type.AUTH_REQUEST)
                                    .setAuthRequest(request).build();

                            networkManager.sendPacket(authRequest);
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
    public void onPacketReceived(Packet packet) {
        if (log.isInfoEnabled()) {
            log.info(LogUtils.getObjectInfo(this) + " received packet " + packet.getType());
        }

        switch (packet.getType()) {
            case AUTH_RESPONSE:
                boolean success = packet.getAuthResponse().getIsSuccess();
                log.info("Authentication result = " + success);

                if (success) {
                    clientWindow.setStage(new LoadingStage());
                }
                break;
            case GAME_INFO:
                GameInfo gameInfo = packet.getGameInfo();
                GameInfo.TypesMap typesMap = gameInfo.getTypesMap();
            //    typesMap.getItemTypesList().get(0).
                break;
        }
    }

    @Override
    public List<Packet.Type> getPacketTypes() {
        return Arrays.asList(Packet.Type.AUTH_RESPONSE,
                Packet.Type.GAME_INFO);
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
