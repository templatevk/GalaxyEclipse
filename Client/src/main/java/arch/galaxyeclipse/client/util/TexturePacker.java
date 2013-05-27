package arch.galaxyeclipse.client.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

/**
 * Utility class to pack the pictures into .atlas.
 */
public class TexturePacker {
	public static void main(String args[]) {
		Settings settings = new Settings();
		settings.pot = true;
		settings.maxWidth = 8192;
		settings.maxHeight = 8192;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.filterMag = Texture.TextureFilter.MipMapLinearLinear;
		TexturePacker2.process(settings, "assets/textures", "assets/textures", "pack");
	}
}
