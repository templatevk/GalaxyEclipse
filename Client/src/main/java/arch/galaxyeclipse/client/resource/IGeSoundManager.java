package arch.galaxyeclipse.client.resource;

import arch.galaxyeclipse.client.ui.provider.GeStageProviderType;
import arch.galaxyeclipse.shared.common.IGeDisposable;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.types.GeWeaponTypesMapperType;

import static arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket.GeItemPacket;

/**
 *
 */
public interface IGeSoundManager {

    void playShoot(GeItemPacket weapon);
    void playShoot(GeItemPacket weapon, float volume);
    void setShootVolume(float volume);

    void playBackMusic();
    void playBackMusic(float volume);
    void pauseBackMusic();
    void stopBackMusic();
    void setBackMusicVolume(float volume);
}
