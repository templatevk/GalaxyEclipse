package arch.galaxyeclipse.client.resource;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 *
 */
public interface IGeResourceLoader {

    BitmapFont getFont(String path);

    TextureAtlas.AtlasRegion findRegion(String name);

    Drawable createDrawable(String path);

    Sound loadSound(String soundName);

    Music loadMusic(String musicName);
}
