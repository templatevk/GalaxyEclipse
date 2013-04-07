package arch.galaxyeclipse.client.texture;

import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;

/**
 * Texture atlas instance caching the regions, etc through HashMaps.
 */
class CachingTextureAtlas extends TextureAtlas implements ITextureAtlas {
	private Map<String, AtlasRegion> regions = new HashMap<String, AtlasRegion>();

	public CachingTextureAtlas() {
		super(Gdx.files.internal("assets/textures/pack.atlas"));
	}
	
	@Override
	public AtlasRegion findRegion(String name) {
		AtlasRegion region = regions.get(name);
		if (region == null) {
			region = super.findRegion(name);
			regions.put(name, region);
		}
		return region;
	}
}
