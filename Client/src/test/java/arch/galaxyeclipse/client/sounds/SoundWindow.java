package arch.galaxyeclipse.client.sounds;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SoundWindow {
    public static void main(String[] args) {
        /*LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "MyWindow";
        config.width = 680;
        config.height = 400;*/
        new LwjglApplication(new TestSound(), "Files Example", 480, 320, false);
    }
}