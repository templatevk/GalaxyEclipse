package arch.galaxyeclipse.client.stage;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public class CustomTextField extends TextField {
	private float prefWidth;
	
	public CustomTextField(String text, TextFieldStyle style, float prefWidth) {
		super(text, style);
		this.prefWidth = prefWidth;
	}
	
	@Override
	public float getPrefWidth() {
		return prefWidth;
	}
}
