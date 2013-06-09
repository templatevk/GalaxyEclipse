package arch.galaxyeclipse.client.sounds;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestSound implements ApplicationListener {

    private Sound sound;

    @Override
    public void create() {
        FileHandle musicHandle = Gdx.files.internal("assets/sounds/test.ogg");
        sound = Gdx.audio.newSound(musicHandle);

    }

    @Override
    public void dispose() {
        sound.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void render() {

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println("playing...");

            //long soundId = sound.play();
            //sound.setVolume(soundId, 1f);
            sound.play();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void resume() {
    }
}