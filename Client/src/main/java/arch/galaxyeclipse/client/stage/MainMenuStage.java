package arch.galaxyeclipse.client.stage;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class MainMenuStage extends Stage {
	public MainMenuStage() {
		Skin skin = new Skin(Gdx.files.internal("skins/test.js"));
		
		Table table = new Table();
		addActor(table);
		table.setFillParent(true);
		TextButton button = new TextButton("GalaxyEclipse", skin.get(TextButtonStyle.class));
		button.setWidth(200);
		button.setHeight(100);
		button.setX(300);
		button.setY(300);
		table.addActor(button);
	}
}
