package arch.galaxyeclipse.client.data;

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
}
