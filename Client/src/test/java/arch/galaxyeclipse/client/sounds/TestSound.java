package arch.galaxyeclipse.client.sounds;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;

public class TestSound implements ApplicationListener {

    private Sound sound;

    @Override
    public void create() {
        sound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/flight.wav"));
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

            long soundId = sound.play();
            sound.setVolume(soundId, 1f);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void resume() {
    }
}