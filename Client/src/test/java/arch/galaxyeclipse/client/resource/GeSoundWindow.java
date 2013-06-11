package arch.galaxyeclipse.client.resource;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class GeSoundWindow {

    public static void main(String[] args) {
        new LwjglApplication(new GeTestSound(), "Music Example", 480, 320, false);
    }
}