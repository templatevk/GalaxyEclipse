package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.resources.*;
import arch.galaxyeclipse.client.stage.ui.*;
import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.types.*;
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
public class MainMenuPresenter extends ServerPacketListener implements IStagePresenter {
    private IClientNetworkManager networkManager;
    private IClientWindow clientWindow;

    private MainMenuStage view;
    private MainMenuModel model;
    private IButtonClickCommand connectButtonCommand;

    public MainMenuPresenter() {
        clientWindow = ContextHolder.INSTANCE.getBean(IClientWindow.class);
        networkManager = ContextHolder.INSTANCE.getBean(IClientNetworkManager.class);

        connectButtonCommand = new IButtonClickCommand() {
            @Override
            public void execute(InputEvent e, float x, float y) {
                ICallback<Boolean> connectionCallback = new ICallback<Boolean>() {
                    @Override
                    public void onOperationComplete(Boolean isConnected) {
                        if (log.isInfoEnabled()) {
                            log.info(LogUtils.getObjectInfo(this) + " connection callback"
                                    + ", result = " + isConnected);
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

        networkManager.addPacketListener(this);

        view = new MainMenuStage(this);
        model = new MainMenuModel();
    }

    @Override
    public AbstractGameStage getGameStage() {
        return view;
    }

    @Override
    public void detach() {
        networkManager.removePacketListener(this);
}

    @Override
    protected void onPacketReceivedImpl(Packet packet) {

        switch (packet.getType()) {
            case AUTH_RESPONSE:
                processAuthResponse(packet.getAuthResponse());
                break;
            case STARTUP_INFO:
                processStartupInfo(packet.getStartupInfo());
                break;
        }
    }

    private void processStartupInfo(StartupInfo startupInfo) {
        ContextHolder.INSTANCE.getBean(IShipStaticInfoHolder.class)
                .setShipStaticInfo(startupInfo.getShipStaticInfo());
        ContextHolder.INSTANCE.getBean(ILocationInfoHolder.class)
                .setLocationInfo(startupInfo.getLocationInfo());

        processTypesMap(startupInfo.getTypesMap());
    }

    private void processTypesMap(TypesMap typesMap) {
        if (log.isDebugEnabled()) {
            log.debug("Processing types map");
        }

        DictionaryTypesMapper dictionaryTypesMapper = ContextHolder.INSTANCE
                .getBean(DictionaryTypesMapper.class);

        Map<Integer, String> itemTypes = new HashMap<>();
        for (TypesMap.ItemType itemType : typesMap.getItemTypesList()) {
            itemTypes.put(itemType.getId(), itemType.getName());
        }
        dictionaryTypesMapper.fillItemTypes(itemTypes);

        Map<Integer, String> weaponTypes = new HashMap<>();
        for (TypesMap.WeaponType weaponType : typesMap.getWeaponTypesList()) {
            weaponTypes.put(weaponType.getId(), weaponType.getName());
        }
        dictionaryTypesMapper.fillWeaponTypes(weaponTypes);

        Map<Integer, String> locationObjectTypes = new HashMap<>();
        for (TypesMap.LocationObjectType locationObjectType : typesMap
                .getLocationObjectTypesList()) {
            locationObjectTypes.put(locationObjectType.getId(), locationObjectType.getName());
        }
        dictionaryTypesMapper.fillLocationObjectTypes(locationObjectTypes);
    }

    private void processAuthResponse(AuthResponse authResponse) {
        boolean success = authResponse.getIsSuccess();
        if (log.isInfoEnabled()) {
            log.info("Authentication result = " + success);
        }

        if (success) {
            if (log.isDebugEnabled()) {
                log.debug("Switching to loading stage");
            }
            // TODO uncomment when stable
            // clientWindow.setStage(new LoadingStage());
        }
    }

    @Override
    public List<Packet.Type> getPacketTypes() {
        return Arrays.asList(Packet.Type.AUTH_RESPONSE,
                Packet.Type.STARTUP_INFO);
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


            // TODO test if we can click/process scaled actors
            // add(new Image())
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
