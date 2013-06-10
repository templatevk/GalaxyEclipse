package arch.galaxyeclipse.client.sounds;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SoundWindow {
    public static void main(String[] args) {
        new LwjglApplication(new TestSound(), "Music Example", 480, 320, false);
    }
}