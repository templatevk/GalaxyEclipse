package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class MainMenuStage extends GameStage {
	private static final float TABLE_SPACING = 10;
	private static final float DEFAULT_TEXTFIELD_WIDTH = 370;
	private static final float DEFAULT_TEXTFIELD_HEIGHT = 100;
	private static final float DEFAULT_BUTTON_DOWN_OFFSET = 2;
	
	private Button connectBtn;
	private TextField usernameTxt;
	private TextField passwordTxt;
	private Table rootTable;
	private Table innerTable;
	
	public MainMenuStage() {
		TextureAtlas atlas = CachingTextureAtlas.getInstance();
		
		BitmapFont font = new BitmapFont(Gdx.files.internal("assets/font1.fnt"), 
				Gdx.files.internal("assets/font1.png"), false);
		
		Drawable carret = new TextureRegionDrawable(atlas.findRegion("ui/carret"));
		TextFieldStyle textFieldStyle = new TextFieldStyle(font, Color.RED, carret, 
				carret, new TextureRegionDrawable(atlas.findRegion("ui/textField")));
		textFieldStyle.fontColor = Color.WHITE;
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(atlas.findRegion("ui/btnUp"));
		style.down = new TextureRegionDrawable(atlas.findRegion("ui/btnDown"));
		style.font = font;
		style.fontColor = Color.BLACK;
		style.downFontColor = Color.DARK_GRAY;
		style.pressedOffsetX = DEFAULT_BUTTON_DOWN_OFFSET;
		style.pressedOffsetY = -DEFAULT_BUTTON_DOWN_OFFSET;
		
		rootTable = new Table();
		rootTable.setFillParent(true);
		rootTable.setBackground(new TextureRegionDrawable(atlas.findRegion("ui/menu")));
		rootTable.debug();
		addActor(rootTable);
	
		innerTable = new Table();
		innerTable.setBackground(new TextureRegionDrawable(atlas.findRegion("ui/group")));
		innerTable.setBounds(0, 0, 400, 200);
		innerTable.setTransform(true);
		innerTable.debug();
		rootTable.setTransform(false);
		rootTable.add(innerTable);
		
		usernameTxt = new CustomTextField("", textFieldStyle, DEFAULT_TEXTFIELD_WIDTH,
				DEFAULT_TEXTFIELD_HEIGHT);
		usernameTxt.setMessageText("Enter your username");
		usernameTxt.setLayoutEnabled(true);
		
		passwordTxt = new CustomTextField("", textFieldStyle, DEFAULT_TEXTFIELD_WIDTH,
				DEFAULT_TEXTFIELD_HEIGHT);
		passwordTxt.setPasswordMode(true);
		passwordTxt.setPasswordCharacter('*');
		passwordTxt.setMessageText("Enter your password");
		
		connectBtn = new TextButton("Connect", style);
		
		innerTable.add(usernameTxt).expand(true, false).space(TABLE_SPACING);
		innerTable.row();
		innerTable.add(passwordTxt).expand(true, false).space(TABLE_SPACING);
		innerTable.row();
		innerTable.add(connectBtn).expand(true, false).space(TABLE_SPACING);
		innerTable.setOrigin(innerTable.getPrefWidth() / 2, 
				innerTable.getPrefHeight() / 2);
	}
	
	@Override
	protected Group getScaleGroup() {
		return innerTable;
	}
}
