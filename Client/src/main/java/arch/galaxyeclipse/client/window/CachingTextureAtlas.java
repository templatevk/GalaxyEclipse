package arch.galaxyeclipse.client.window;

import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;

public class CachingTextureAtlas extends TextureAtlas {
	private static final TextureAtlas INSTANCE = new CachingTextureAtlas();
	
	private Map<String, AtlasRegion> regions = new HashMap<String, AtlasRegion>();

	public CachingTextureAtlas() {
		super(Gdx.files.internal("textures/pack.atlas"));
	}
	
	public static TextureAtlas getInstance() {
		return INSTANCE;
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
