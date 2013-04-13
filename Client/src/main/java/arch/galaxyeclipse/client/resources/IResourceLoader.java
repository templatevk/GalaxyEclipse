package arch.galaxyeclipse.client.resources;

import com.badlogic.gdx.graphics.g2d.*;

/**
 *
 */
public interface IResourceLoader {
    BitmapFont getFont(String path);

    TextureAtlas.AtlasRegion findRegion(String name);
}
