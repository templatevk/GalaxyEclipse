package arch.galaxyeclipse.client.resource;


import arch.galaxyeclipse.client.ui.provider.GeStageProviderType;
import arch.galaxyeclipse.client.util.GeDisposable;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.types.GeWeaponTypesMapperType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class GeSoundManager extends GeDisposable implements IGeSoundManager {

    private @Setter float defaultVolume;

    private IGeResourceLoader resourceLoader;
    private Map<MusicType, Music> musicMap;
    private Map<SoundType, Sound> soundMap;

    public GeSoundManager() {
        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
        musicMap = new HashMap<>();
        soundMap = new HashMap<>();

        loadAudio();
    }

    private void loadAudio() {
        musicMap.put(MusicType.BACKGROUND, resourceLoader.loadMusic("flight.mp3"));

        soundMap.put(SoundType.LASER, resourceLoader.loadSound("shoot.mp3"));
        soundMap.put(SoundType.ROCKET, resourceLoader.loadSound("shoot.mp3"));
        soundMap.put(SoundType.FLY, resourceLoader.loadSound("flight.mp3"));
    }

    @Override
    public void playFly() {
        soundMap.get(SoundType.FLY).play();
    }

    @Override
    public void setFlyVolume(float volume) {
        soundMap.get(SoundType.FLY).play(volume);
    }

    @Override
    public void stopFly() {
        soundMap.get(SoundType.FLY).stop();
    }

    @Override
    public void playShoot(GeWeaponTypesMapperType weaponTypesMapperType) {
        playShoot(weaponTypesMapperType, defaultVolume);
    }

    @Override
    public void playShoot(GeWeaponTypesMapperType weaponTypesMapperType, float volume) {
        switch (weaponTypesMapperType) {
            case LASER:
                soundMap.get(SoundType.LASER).play(volume);
                break;
            case ROCKET:
                soundMap.get(SoundType.ROCKET).play(volume);
                break;
        }
    }

    @Override
    public void setShootVolume(float volume) {
        for (Entry<SoundType, Sound> sound : soundMap.entrySet()) {
            long id = sound.getValue().play();
            sound.getValue().setVolume(id, volume);
        }
    }

    @Override
    public void playBackMusic(GeStageProviderType stageProviderType) {
        switch (stageProviderType) {
            case MAIN_MENU:
                musicMap.get(MusicType.BACKGROUND).play();
                break;
        }
    }

    @Override
    public void playBackMusic(GeStageProviderType stageProviderType, float volume) {
        switch (stageProviderType) {
            case MAIN_MENU:
                musicMap.get(MusicType.BACKGROUND).setVolume(volume);
                break;
        }
    }

    @Override
    public void setBackMusicVolume(float volume) {
        for (Entry<MusicType, Music> music : musicMap.entrySet()) {
            music.getValue().setVolume(volume);
        }
    }

    @Override
    public void pauseBackMusic() {
    }

    @Override
    public void stopBackMusic() {
    }

    @Override
    public void dispose() {
        for (Music music : musicMap.values()) {
            music.dispose();
        }
        for (Sound sound : soundMap.values()) {
            sound.dispose();
        }
    }

    private enum SoundType {
        LASER,
        ROCKET,
        FLY;
    }

    private enum MusicType {
        BACKGROUND;
    }
}
