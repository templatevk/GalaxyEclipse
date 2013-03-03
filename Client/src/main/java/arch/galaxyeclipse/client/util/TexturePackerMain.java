package arch.galaxyeclipse.client.util;

import com.badlogic.gdx.tools.imagepacker.*;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class TexturePackerMain {
	public static void main(String args[]) {
		Settings settings = new Settings();
		settings.pot = true;
		settings.maxWidth = 4096;
		settings.maxHeight = 4096;
		TexturePacker2.process(settings, "textures", "textures", "pack");
	}
}
