package arch.galaxyeclipse.client.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

import static com.badlogic.gdx.tools.imagepacker.TexturePacker2.process;

/**
 * Utility class to pack the pictures into .atlas.
 */
public class GeTexturePacker {
	public static void main(String args[]) {
		Settings settings = new Settings();
		settings.pot = true;
		settings.maxWidth = 8192;
		settings.maxHeight = 8192;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.filterMag = Texture.TextureFilter.MipMapLinearLinear;
		process(settings, "assets/textures", "assets/textures", "pack");
	}
}
