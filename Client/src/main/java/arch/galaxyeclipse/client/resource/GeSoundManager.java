package arch.galaxyeclipse.client.resource;


import arch.galaxyeclipse.client.ui.provider.GeStageProviderType;
import arch.galaxyeclipse.client.util.GeDisposable;
import arch.galaxyeclipse.client.window.IGeClientWindow;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.types.GeWeaponTypesMapperType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import lombok.Setter;

import java.util.Map.Entry;

class GeSoundManager implements IGeSoundManager {

    private static final String SHOOT_SOUND_PATH = // <weapon_id>
            "assets/audio/shoot/%d";

    private static final String BACKGROUND_MUSIC_PATH =
            // GeStageProviderType.<VALUE>.toString().toLowerCase()
            "assets/audio/background/%s";

    private @Setter float backMusicVolume;
    private @Setter float shootVolume;
    private @Setter float flyVolume;

    private IGeClientWindow clientWindow;
    private IGeResourceLoader resourceLoader;

    public GeSoundManager() {
        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
        clientWindow = GeContextHolder.getBean(IGeClientWindow.class);
    }


    @Override
    public void playBackMusic() {
        playBackMusic(backMusicVolume);
    }

    @Override
    public void playBackMusic(float volume) {
        String path = String.format(BACKGROUND_MUSIC_PATH,
                clientWindow.getCurrentStageProviderType().toString().toLowerCase());
        Music music = resourceLoader.loadMusic(path);
        music.play();
        // TODO
    }

    @Override
    public void pauseBackMusic() {
        // TODO
    }

    @Override
    public void stopBackMusic() {
        // TODO
    }

    @Override
    public void playShoot(GeProtocol.GeShipStaticInfoPacket.GeItemPacket weapon) {

    }

    @Override
    public void playShoot(GeProtocol.GeShipStaticInfoPacket.GeItemPacket weapon, float volume) {

    }
}
