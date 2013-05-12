package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.util.Destroyable;
import arch.galaxyeclipse.shared.common.IDestroyable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

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
            if (CachingResourceLoader.log.isInfoEnabled()) {
                CachingResourceLoader.log.info("Loading region " + name);
            }


            try {
                region = super.findRegion(name);
                regions.put(name, region);
            } catch (Exception e) {
                throw new RuntimeException("Error loading region, try rerunning TexturePackerMain", e);
            }
		}
		return region;
	}

    @Override
    public Drawable createDrawable(String path) {
        return new TextureRegionDrawable(findRegion(path));
    }

    @Override
    public BitmapFont getFont(String path) {
        BitmapFont font = fonts.get(path);

        if (font == null) {
            if (CachingResourceLoader.log.isInfoEnabled()) {
                CachingResourceLoader.log.info("Loading font " + path);
            }

            Texture fontTexture = new Texture(Gdx.files.internal(path + "_0.png"));
            fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.MipMapLinearLinear);
            TextureRegion fontTextureRegion = new TextureRegion(fontTexture);
            font = new BitmapFont(Gdx.files.internal(path + ".fnt"),
                    fontTextureRegion, false);

            fonts.put(path, font);
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
