package arch.galaxyeclipse.client.texture;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.*;

public interface ITextureAtlas {
	AtlasRegion findRegion(String name);
}
