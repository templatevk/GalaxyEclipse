package arch.galaxyeclipse.client.sounds;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


public class TestSound implements ApplicationListener  {
    public TestSound(){}
    Music music;
    Sound shoot;

    @Override
    public void create() {

        music =  Gdx.audio.newMusic(Gdx.files.getFileHandle("assets/sounds/flight.mp3", Files.FileType.Internal));

        shoot = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/shoot.mp3"));
        System.out.println("playing from create method..");
        shoot.play();
        music.setVolume(0.5f);
        music.play();
        music.setLooping(true);
    }

    @Override
    public void resize(int i, int i2) {

    }

    @Override
    public void render() {
       if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
           System.out.println("playing..");
           shoot.play();
       }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        music.dispose();
        shoot.dispose();
    }
}