package arch.galaxyeclipse.client.sounds;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;

public class TestSound implements ApplicationListener {

    private Sound shoot;
    @Override
    public void create() {
        shoot = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/shoot.mp3"));
    }

    @Override
    public void dispose() {
        shoot.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void render() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println("TESTMESSAGE");
            shoot.play();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void resume() {
    }

}