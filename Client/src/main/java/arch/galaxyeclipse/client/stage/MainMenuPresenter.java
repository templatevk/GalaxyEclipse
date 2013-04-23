package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.resource.*;
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
public class MainMenuPresenter extends ServerPacketListener implements IStageProvider {
    private IClientNetworkManager networkManager;
    private IClientWindow clientWindow;

    private MainMenuStage view;
    private IButtonClickCommand connectButtonCommand;
    private ShipStaticInfoHolder shipStaticInfoHolder;
    private LocationInfoHolder locationInfoHolder;

    public MainMenuPresenter() {
        clientWindow = ContextHolder.getBean(IClientWindow.class);
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);
        locationInfoHolder = ContextHolder.getBean(LocationInfoHolder.class);
        shipStaticInfoHolder = ContextHolder.getBean(ShipStaticInfoHolder.class);

        connectButtonCommand = new IButtonClickCommand() {
            @Override
            public void execute(InputEvent e, float x, float y) {
                ICallback<Boolean> connectionCallback = new ICallback<Boolean>() {
                    @Override
                    public void onOperationComplete(Boolean isConnected) {
                        if (MainMenuPresenter.log.isInfoEnabled()) {
                            MainMenuPresenter.log.info(LogUtils.getObjectInfo(this)
                                    + " connection callback, result = " + isConnected);
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
        processTypesMap(startupInfo.getTypesMap());

        shipStaticInfoHolder.setShipStaticInfo(startupInfo.getShipStaticInfo());
        locationInfoHolder.setLocationInfo(startupInfo.getLocationInfo());

        clientWindow.setStageProvider(new FlightModeController());
    }

    private void processTypesMap(TypesMap typesMap) {
        if (MainMenuPresenter.log.isDebugEnabled()) {
            MainMenuPresenter.log.debug("Processing types map");
        }

        DictionaryTypesMapper dictionaryTypesMapper = ContextHolder
                .getBean(DictionaryTypesMapper.class);

        Map<Integer, String> itemTypes = new HashMap<>();
        for (TypesMap.Type itemType : typesMap.getItemTypesList()) {
            itemTypes.put(itemType.getId(), itemType.getName());
        }
        dictionaryTypesMapper.fillItemTypes(itemTypes);

        Map<Integer, String> weaponTypes = new HashMap<>();
        for (TypesMap.Type weaponType : typesMap.getWeaponTypesList()) {
            weaponTypes.put(weaponType.getId(), weaponType.getName());
        }
        dictionaryTypesMapper.fillWeaponTypes(weaponTypes);

        Map<Integer, String> locationObjectTypes = new HashMap<>();
        for (TypesMap.Type locationObjectType : typesMap
                .getLocationObjectTypesList()) {
            locationObjectTypes.put(locationObjectType.getId(), locationObjectType.getName());
        }
        dictionaryTypesMapper.fillLocationObjectTypes(locationObjectTypes);

        Map<Integer, String> bonusTypes = new HashMap<>();
        for (TypesMap.Type bonusType : typesMap.getBonusTypesList()) {
            bonusTypes.put(bonusType.getId(), bonusType.getName());
        }
        dictionaryTypesMapper.fillBonusTypes(bonusTypes);
    }

    private void processAuthResponse(AuthResponse authResponse) {
        boolean success = authResponse.getIsSuccess();
        if (MainMenuPresenter.log.isInfoEnabled()) {
            MainMenuPresenter.log.info("Authentication result = " + success);
        }

        if (success) {
            if (MainMenuPresenter.log.isDebugEnabled()) {
                MainMenuPresenter.log.debug("Switching to loading stage");
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
                    .setClickCommand(presenter.connectButtonCommand).build();


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
                    usernameTxt, passwordTxt, connectBtn), this);
            setKeyboardFocus(usernameTxt);
        }

        @Override
        protected Group getScaleGroup() {
            return innerTable;
        }
    }
}
