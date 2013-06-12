package arch.galaxyeclipse.client.resource;

import arch.galaxyeclipse.client.network.GeServerPacketListener;
import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.client.window.IGeClientWindow;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import com.badlogic.gdx.audio.Music;

import java.util.List;

import static arch.galaxyeclipse.shared.context.GeContextHolder.getBean;
import static java.util.Arrays.asList;


public class GeFlySoundManager extends GeServerPacketListener {

    private static final String FLY_SOUND_PATH = // <ship_type_id>
            "assets/audio/fly/%d";

    private IGeClientWindow clientWindow;
    private IGeResourceLoader resourceLoader;
    private Music fly;
    private String path;

    public GeFlySoundManager() {
        //path = String.format(FLY_SOUND_PATH, );

        //getBean(IGeClientNetworkManager.class).addPacketListener(this);
    }


    @Override
    protected void onPacketReceivedImpl(GeProtocol.GePacket packet) {
        switch (packet.getType()) {
             case SHIP_STATE_RESPONSE:
                   playFly();
                 break;
        }
    }

    @Override
    public List<GeProtocol.GePacket.Type> getPacketTypes() {
        return asList(GeProtocol.GePacket.Type.SHIP_STATE_RESPONSE);
    }

    public void playFly() {
        fly = resourceLoader.loadMusic(path);
        fly.play();
    }

    public void playFly(float volume) {
        fly = resourceLoader.loadMusic(path);
        fly.setVolume(volume);
        fly.play();
    }

    public void stopFly() {
       fly.stop();
    }


}
