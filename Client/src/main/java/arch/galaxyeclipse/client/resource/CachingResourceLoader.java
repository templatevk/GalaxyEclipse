package arch.galaxyeclipse.client.resource;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import lombok.extern.slf4j.*;

import java.util.*;

/**
 * Texture atlas instance caching the regions, etc through HashMaps.
 */
@Slf4j
class CachingResourceLoader extends TextureAtlas implements IResourceLoader, IDestroyable {
	private Map<String, AtlasRegion> regions;
    private Map<String, BitmapFont> fonts;

	public CachingResourceLoader() {
		super(Gdx.files.internal("assets/textures/pack.atlas"));

        regions = new HashMap<>();
        fonts = new HashMap<>();

        Destroyable.addDestroyable(this);
	}
	
	@Override
	public AtlasRegion findRegion(String name) {
		AtlasRegion region = regions.get(name);

        if (region == null) {
            if (log.isInfoEnabled()) {
                log.info("Loading region " + name);
            }
			region = super.findRegion(name);
			regions.put(name, region);
		}
		return region;
	}

    @Override
    public BitmapFont getFont(String path) {
        BitmapFont font = fonts.get(path);

        if (font == null) {
            if (log.isInfoEnabled()) {
                log.info("Loading font " + path);
            }
            font = new BitmapFont(Gdx.files.internal("assets/font1.fnt"),
                    Gdx.files.internal("assets/font1.png"), false);
        }
        return font;
    }

    @Override
    public void destroy() {
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
        super.dispose();
    }
}
