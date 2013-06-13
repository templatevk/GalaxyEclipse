package arch.galaxyeclipse.client.resource;


import arch.galaxyeclipse.client.network.GeServerPacketListener;
import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.client.window.IGeClientWindow;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import com.badlogic.gdx.audio.Music;
import lombok.Setter;

import java.util.List;

import static java.util.Arrays.asList;

class GeAudioManager extends GeServerPacketListener implements IGeAudioManager {

    private static final String SHOOT_SOUND_PATH = "shoot/%d"; // <weapon_id>
    // GeStageProviderType.<VALUE>.toString().toLowerCase()
    private static final String BACKGROUND_MUSIC_PATH = "background/%s";

    private @Setter float backMusicVolume;
    private @Setter float shootVolume;
    private @Setter float flyVolume = 1f;

    private IGeClientWindow clientWindow;
    private IGeResourceLoader resourceLoader;

    private Music prevBackMusic;

    public GeAudioManager() {
        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
        clientWindow = GeContextHolder.getBean(IGeClientWindow.class);
        GeContextHolder.getBean(IGeClientNetworkManager.class).addPacketListener(this);
    }

    @Override
    public void playBackMusic() {
        playBackMusic(backMusicVolume);
    }

    @Override
    public void playBackMusic(float volume) {
        String path = String.format(BACKGROUND_MUSIC_PATH, clientWindow
                .getCurrentStageProviderType().toString().toLowerCase());

        if (prevBackMusic != null) {
            prevBackMusic.stop();
        }

        Music music = resourceLoader.loadMusic(path);
        music.setLooping(true);
        music.play();

        prevBackMusic = music;
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
    public void setBackMusicVolume(float volume) {
    }

    @Override
    public void playShoot(GeProtocol.GeShipStaticInfoPacket.GeItemPacket weapon) {

    }

    @Override
    public void playShoot(GeProtocol.GeShipStaticInfoPacket.GeItemPacket weapon, float volume) {

    }

    @Override
    public void setShootVolume(float volume) {
    }

    @Override
    protected void onPacketReceivedImpl(GeProtocol.GePacket packet) {
        switch (packet.getType()) {
            case SHIP_STATE_RESPONSE:
                // TODO
                break;
        }
    }

    @Override
    public List<GeProtocol.GePacket.Type> getPacketTypes() {
        return asList(GeProtocol.GePacket.Type.SHIP_STATE_RESPONSE);
    }
}
