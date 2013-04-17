package arch.galaxyeclipse.client.resource;

import com.badlogic.gdx.graphics.g2d.*;

/**
 *
 */
public interface IResourceLoader {
    BitmapFont getFont(String path);

    TextureAtlas.AtlasRegion findRegion(String name);
}
