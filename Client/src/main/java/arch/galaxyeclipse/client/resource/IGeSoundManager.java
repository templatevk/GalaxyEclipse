package arch.galaxyeclipse.client.resource;

import arch.galaxyeclipse.client.ui.provider.GeStageProviderType;
import arch.galaxyeclipse.shared.common.IGeDisposable;
import arch.galaxyeclipse.shared.types.GeWeaponTypesMapperType;

/**
 *
 */
public interface IGeSoundManager extends IGeDisposable {

    void playFly();

    void setFlyVolume(float volume);

    void stopFly();

    void playShoot(GeWeaponTypesMapperType weaponTypesMapperType);

    void playShoot(GeWeaponTypesMapperType weaponTypesMapperType, float volume);

    void setShootVolume(float volume);

    void playBackMusic(GeStageProviderType stageProviderType);

    void playBackMusic(GeStageProviderType stageProviderType, float volume);

    void setBackMusicVolume(float volume);

    void pauseBackMusic();

    void stopBackMusic();
}
