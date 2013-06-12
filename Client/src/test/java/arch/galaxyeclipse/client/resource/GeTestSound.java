package arch.galaxyeclipse.client.resource;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


public class GeTestSound implements ApplicationListener {

    private Music music;
    private Music music2;
    private Sound shoot;

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.getFileHandle("assets/audio/fly/1.mp3", Files.FileType.Internal));
        music2 = Gdx.audio.newMusic(Gdx.files.getFileHandle("assets/audio/shoot/3.mp3", Files.FileType.Internal));

        shoot = Gdx.audio.newSound(Gdx.files.internal("assets/audio/shoot/4.mp3"));
        System.out.println("playing from create method..");
        music.setVolume(0.5f);
        //music.play();
        //music.setLooping(true);

       // music2.play();
       // music2.setLooping(true);
    }

    @Override
    public void resize(int i, int i2) {

    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
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