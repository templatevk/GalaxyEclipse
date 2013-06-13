package arch.galaxyeclipse.client.resource;

/**
 *
 */
public interface IGeAudioManager {

    void playShoot();
    void playShoot(float volume);

    void setShootVolume(float volume);
    void playBackMusic();
    void playBackMusic(float volume);
    void pauseBackMusic();
    void stopBackMusic();

    void setBackMusicVolume(float volume);
}
