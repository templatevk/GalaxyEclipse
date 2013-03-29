package arch.galaxyeclipse.client.stage;

import java.net.*;
import java.util.*;
import java.util.List;

import org.apache.log4j.*;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.texture.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.inject.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.AuthRequest;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet.Type;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class MainMenuStage extends AbstractGameStage implements IServerPacketListener {
	private static final Logger log = Logger.getLogger(MainMenuStage.class);
	
	private static final float TABLE_SPACING = 10;
	private static final float DEFAULT_TEXTFIELD_WIDTH = 370;
	private static final float DEFAULT_TEXTFIELD_HEIGHT = 100;
	private static final float DEFAULT_BUTTON_DOWN_OFFSET = 2;
	
	private IClientNetworkManager networkManager;
	
	private Button connectBtn;
	private TextField usernameTxt;
	private TextField passwordTxt;
	private Table rootTable;
	private Table innerTable;
	private boolean isGuiInitialized = false;
	
	public MainMenuStage() {
		
	}
	
	private void initGuiOnce() {
		if (isGuiInitialized) {
			return;
		}
		isGuiInitialized = true;
		
		ITextureAtlas atlas = SpringContextHolder.CONTEXT
				.getBean(ITextureAtlasFactory.class).createAtlas();
		
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
		rootTable.setBackground(new TextureRegionDrawable(atlas.findRegion("ui/menu_login")));
		rootTable.debug();
		addActor(rootTable);
	
		innerTable = new Table();
		//innerTable.setBackground(new TextureRegionDrawable(atlas.findRegion("ui/group")));
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
		connectBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				networkManager.connect(new InetSocketAddress(
						SharedInfo.HOST, SharedInfo.PORT), new ICallback<Boolean>() {
							@Override
							public void onOperationComplete(Boolean isConnected) {
								log.info(getClass().getSimpleName() + " connection callback "
										+ " result = " + isConnected);
								if (isConnected) {
									AuthRequest request = AuthRequest.newBuilder()
											.setUsername("")
											.setPassword("").build();
									Packet packet = Packet.newBuilder()
											.setType(Type.AUTH_REQUEST)
											.setAuthRequest(request).build();
									networkManager.sendPacket(packet);
								}
							}
						});
			}
		});
		
		innerTable.add(usernameTxt).expand(true, false).space(TABLE_SPACING);
		innerTable.row();
		innerTable.add(passwordTxt).expand(true, false).space(TABLE_SPACING);
		innerTable.row();
		innerTable.add(connectBtn).expand(true, false).space(TABLE_SPACING);
		innerTable.setOrigin(innerTable.getPrefWidth() / 2, 
				innerTable.getPrefHeight() / 2);
		
		networkManager = SpringContextHolder.CONTEXT.getBean(IClientNetworkManager.class);
		networkManager.addListener(this);
	}
	
	@Override
	public void resize(int width, int height) {
		initGuiOnce();
		super.resize(width, height);
	}
	
	@Override
	protected Group getScaleGroup() {
		return innerTable;
	}

	@Override
	public void onPacketReceived(Packet packet) {
		log.info(this + " received packet " + packet.getType());
		switch (packet.getType()) {
		case AUTH_RESPONSE:
			log.info("Authentication result = " + packet.getAuthResponse().getIsSuccess());
			break;
		}
	}

	@Override
	public List<Type> getPacketTypes() {
		return Arrays.asList(Type.AUTH_RESPONSE);
	}
}
