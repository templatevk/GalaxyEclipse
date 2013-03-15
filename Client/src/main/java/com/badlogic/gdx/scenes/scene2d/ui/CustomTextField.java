package com.badlogic.gdx.scenes.scene2d.ui;

public class CustomTextField extends TextField {
	private float prefWidth;
	private float prefHeight;
	
	public CustomTextField(String text, TextFieldStyle style, float prefWidth,
			float prefHeight) {
		super(text, style);
		this.prefWidth = prefWidth;
		this.prefHeight = prefHeight;
	}
	
	@Override
	public float getPrefWidth() {
		return prefWidth;
	}
	
	@Override
	public float getPrefHeight() {
		return prefHeight;
	}
}
