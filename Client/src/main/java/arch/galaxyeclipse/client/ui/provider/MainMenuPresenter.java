package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.data.LocationInfoHolder;
import arch.galaxyeclipse.client.data.ShipStaticInfoHolder;
import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.network.ServerPacketListener;
import arch.galaxyeclipse.client.ui.IButtonClickCommand;
import arch.galaxyeclipse.client.ui.view.AbstractGameStage;
import arch.galaxyeclipse.client.ui.view.MainMenuStage;
import arch.galaxyeclipse.client.window.IClientWindow;
import arch.galaxyeclipse.shared.common.ICallback;
import arch.galaxyeclipse.shared.common.LogUtils;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
public class MainMenuPresenter extends ServerPacketListener implements IStageProvider {
    private IClientNetworkManager networkManager;
    private IClientWindow clientWindow;

    private MainMenuStage view;
    private ShipStaticInfoHolder shipStaticInfoHolder;
    private LocationInfoHolder locationInfoHolder;

    private @Getter IButtonClickCommand connectButtonCommand;

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
                                    .setUsername(view.getUsername())
                                    .setPassword(view.getPassword()).build();

                            Packet authRequest = Packet.newBuilder()
                                    .setType(Packet.Type.AUTH_REQUEST)
                                    .setAuthRequest(request).build();

                            networkManager.sendPacket(authRequest);
                        }
                    }
                };
                networkManager.connect(connectionCallback);
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

    private void processStartupInfo(StartupInfoPacket startupInfo) {
        processTypesMap(startupInfo.getTypesMap());

        shipStaticInfoHolder.setShipStaticInfo(startupInfo.getShipStaticInfo());
        locationInfoHolder.setLocationInfo(startupInfo.getLocationInfo());

        clientWindow.setStageProvider(new FlightModeController());
    }

    private void processTypesMap(TypesMapPacket typesMap) {
        if (MainMenuPresenter.log.isDebugEnabled()) {
            MainMenuPresenter.log.debug("Processing types map");
        }

        DictionaryTypesMapper dictionaryTypesMapper = ContextHolder
                .getBean(DictionaryTypesMapper.class);

        Map<Integer, String> itemTypes = new HashMap<>();
        for (TypesMapPacket.Type itemType : typesMap.getItemTypesList()) {
            itemTypes.put(itemType.getId(), itemType.getName());
        }
        dictionaryTypesMapper.fillItemTypes(itemTypes);

        Map<Integer, String> weaponTypes = new HashMap<>();
        for (TypesMapPacket.Type weaponType : typesMap.getWeaponTypesList()) {
            weaponTypes.put(weaponType.getId(), weaponType.getName());
        }
        dictionaryTypesMapper.fillWeaponTypes(weaponTypes);

        Map<Integer, String> locationObjectTypes = new HashMap<>();
        for (TypesMapPacket.Type locationObjectType : typesMap
                .getLocationObjectTypesList()) {
            locationObjectTypes.put(locationObjectType.getId(), locationObjectType.getName());
        }
        dictionaryTypesMapper.fillLocationObjectTypes(locationObjectTypes);

        Map<Integer, String> bonusTypes = new HashMap<>();
        for (TypesMapPacket.Type bonusType : typesMap.getBonusTypesList()) {
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
}
