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

import static com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
import static com.badlogic.gdx.graphics.Texture.TextureFilter.MipMapLinearLinear;

/**
 * Texture atlas instance caching the regions, etc through HashMaps.
 */
@Slf4j
class GeCachingResourceLoader extends TextureAtlas implements IGeResourceLoader, IGeDisposable {
    private static final String FONTS_LOCATIONS = "assets/fonts/";

    private Map<String, AtlasRegion> regions;
    private Map<String, BitmapFont> fonts;
    private Map<String, Sound> sounds;
    private Map<String, Music> music;

	public GeCachingResourceLoader() {
		super(Gdx.files.internal("assets/textures/pack.atlas"));

        regions = new HashMap<>();
        fonts = new HashMap<>();

        GeDisposable.addDestroyable(this);
	}
	
	@Override
	public AtlasRegion findRegion(String name) {
		AtlasRegion region = regions.get(name);

        if (region == null) {
            if (GeCachingResourceLoader.log.isInfoEnabled()) {
                GeCachingResourceLoader.log.info("Loading region " + name);
            }


            try {
                region = super.findRegion(name);
                regions.put(name, region);
            } catch (Exception e) {
                throw new RuntimeException("Error loading region, try rerunning GeTexturePacker", e);
            }
        }
        return region;
    }

    @Override
    public Drawable createDrawable(String path) {
        return new TextureRegionDrawable(findRegion(path));
    }

    @Override
    public Sound loadSound(String soundName) {
        Sound tmpSound = sounds.get(soundName);

        if (tmpSound == null) {
            if (CachingResourceLoader.log.isInfoEnabled()) {
                CachingResourceLoader.log.info("Loading sound " + soundName);
            }

            try {
                tmpSound = Gdx.audio.newSound(Gdx.files.internal(AUDIO_LOCATION + soundName));
                sounds.put(soundName, tmpSound);
            } catch (Exception e) {
                throw new RuntimeException("Error loading sounds", e);
            }
        }
        return tmpSound;
    }

    @Override
    public Music loadMusic(String musicName) {
        Music tmpMusic = music.get(musicName);

        if (tmpMusic == null) {
            if (CachingResourceLoader.log.isInfoEnabled()) {
                CachingResourceLoader.log.info("Loading music " + musicName);
            }

            try {
                tmpMusic = Gdx.audio.newMusic(Gdx.files.internal(AUDIO_LOCATION + musicName));
                music.put(musicName, tmpMusic);
            } catch (Exception e) {
                throw new RuntimeException("Error loading music", e);
            }
        }
        return tmpMusic;
    }

    @Override
    public BitmapFont getFont(String path) {
        BitmapFont font = fonts.get(path);

        if (font == null) {
            if (GeCachingResourceLoader.log.isInfoEnabled()) {
                GeCachingResourceLoader.log.info("Loading font " + path);
            }

            Texture fontTexture = new Texture(Gdx.files.internal(FONTS_LOCATIONS + path + "_0.png"));
            fontTexture.setFilter(Linear, MipMapLinearLinear);
            TextureRegion fontTextureRegion = new TextureRegion(fontTexture);
            font = new BitmapFont(Gdx.files.internal(FONTS_LOCATIONS + path + ".fnt"),
                    fontTextureRegion, false);

            fonts.put(path, font);
        }
        return font;
    }

    @Override
    public void dispose() {
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }

        for (Sound sound : sounds.values()) {
            sound.dispose();
        }

        for (Music mus : music.values()) {
            mus.dispose();
        }
        super.dispose();
    }
}
