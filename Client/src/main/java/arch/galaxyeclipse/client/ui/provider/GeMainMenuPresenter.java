package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.data.GeLocationInfoHolder;
import arch.galaxyeclipse.client.data.GeShipStaticInfoHolder;
import arch.galaxyeclipse.client.network.GeServerPacketListener;
import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.client.ui.IGeButtonClickCommand;
import arch.galaxyeclipse.client.ui.view.GeAbstractGameStage;
import arch.galaxyeclipse.client.ui.view.GeMainMenuStage;
import arch.galaxyeclipse.client.window.IGeClientWindow;
import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.common.IGeCallback;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
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
public class GeMainMenuPresenter extends GeServerPacketListener implements IGeStageProvider {

    private IGeClientNetworkManager networkManager;
    private IGeClientWindow clientWindow;

    private GeMainMenuStage view;
    private GeShipStaticInfoHolder shipStaticInfoHolder;
    private GeLocationInfoHolder locationInfoHolder;

    private
    @Getter
    IGeButtonClickCommand connectButtonCommand;

    public GeMainMenuPresenter() {
        clientWindow = GeContextHolder.getBean(IGeClientWindow.class);
        networkManager = GeContextHolder.getBean(IGeClientNetworkManager.class);
        locationInfoHolder = GeContextHolder.getBean(GeLocationInfoHolder.class);
        shipStaticInfoHolder = GeContextHolder.getBean(GeShipStaticInfoHolder.class);

        connectButtonCommand = new IGeButtonClickCommand() {
            @Override
            public void execute(InputEvent e, float x, float y) {
                IGeCallback<Boolean> connectionCallback = new IGeCallback<Boolean>() {
                    @Override
                    public void onOperationComplete(Boolean isConnected) {
                        if (GeMainMenuPresenter.log.isInfoEnabled()) {
                            GeMainMenuPresenter.log.info(GeLogUtils.getObjectInfo(this)
                                    + " connection callback, result = " + isConnected);
                        }

                        if (isConnected) {
                            GeAuthRequest request = GeAuthRequest.newBuilder()
                                    .setUsername(view.getUsername())
                                    .setPassword(view.getPassword()).build();

                            GePacket authRequest = GePacket.newBuilder()
                                    .setType(GePacket.Type.AUTH_REQUEST)
                                    .setAuthRequest(request).build();

                            networkManager.sendPacket(authRequest);
                        }
                    }
                };
                networkManager.connect(connectionCallback);
            }
        };

        networkManager.addPacketListener(this);

        view = new GeMainMenuStage(this);
    }

    @Override
    public GeAbstractGameStage getGameStage() {
        return view;
    }

    @Override
    public void detach() {
        networkManager.removePacketListener(this);
    }

    @Override
    protected void onPacketReceivedImpl(GePacket packet) {
        switch (packet.getType()) {
            case AUTH_RESPONSE:
                processAuthResponse(packet.getAuthResponse());
                break;
            case STARTUP_INFO:
                processStartupInfo(packet.getStartupInfo());
                break;
        }
    }

    private void processStartupInfo(GeStartupInfoPacket startupInfo) {
        processTypesMap(startupInfo.getTypesMap());

        shipStaticInfoHolder.setShipStaticInfo(startupInfo.getShipStaticInfo());
        locationInfoHolder.setLip(startupInfo.getLocationInfo());

        clientWindow.setStageProvider(new GeFlightModeController());
    }

    private void processTypesMap(GeTypesMapPacket typesMap) {
        if (GeMainMenuPresenter.log.isDebugEnabled()) {
            GeMainMenuPresenter.log.debug("Processing types map");
        }

        GeDictionaryTypesMapper dictionaryTypesMapper = GeContextHolder
                .getBean(GeDictionaryTypesMapper.class);

        Map<Integer, String> itemTypes = new HashMap<>();
        for (GeTypesMapPacket.Type itemType : typesMap.getItemTypesList()) {
            itemTypes.put(itemType.getId(), itemType.getName());
        }
        dictionaryTypesMapper.fillItemTypes(itemTypes);

        Map<Integer, String> weaponTypes = new HashMap<>();
        for (GeTypesMapPacket.Type weaponType : typesMap.getWeaponTypesList()) {
            weaponTypes.put(weaponType.getId(), weaponType.getName());
        }
        dictionaryTypesMapper.fillWeaponTypes(weaponTypes);

        Map<Integer, String> locationObjectTypes = new HashMap<>();
        for (GeTypesMapPacket.Type locationObjectType : typesMap
                .getLocationObjectTypesList()) {
            locationObjectTypes.put(locationObjectType.getId(), locationObjectType.getName());
        }
        dictionaryTypesMapper.fillLocationObjectTypes(locationObjectTypes);

        Map<Integer, String> bonusTypes = new HashMap<>();
        for (GeTypesMapPacket.Type bonusType : typesMap.getBonusTypesList()) {
            bonusTypes.put(bonusType.getId(), bonusType.getName());
        }
        dictionaryTypesMapper.fillBonusTypes(bonusTypes);
    }

    private void processAuthResponse(GeAuthResponse authResponse) {
        boolean success = authResponse.getIsSuccess();
        if (GeMainMenuPresenter.log.isInfoEnabled()) {
            GeMainMenuPresenter.log.info("Authentication result = " + success);
        }

        if (success) {
            if (GeMainMenuPresenter.log.isDebugEnabled()) {
                GeMainMenuPresenter.log.debug("Switching to loading stage");
            }
            // TODO uncomment when stable
            // clientWindow.setStage(new GeLoadingStage());
        }
    }

    @Override
    public List<GePacket.Type> getPacketTypes() {
        return Arrays.asList(GePacket.Type.AUTH_RESPONSE,
                GePacket.Type.STARTUP_INFO);
    }
}
